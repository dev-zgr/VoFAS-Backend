package com.backend.vofasbackend.servicelayer.implementations;

import com.backend.vofasbackend.contants.FeedbackConstants;
import com.backend.vofasbackend.datalayer.entities.FeedbackEntity;
import com.backend.vofasbackend.datalayer.entities.TranscriptionEntity;
import com.backend.vofasbackend.datalayer.enums.FeedbackStateEnum;
import com.backend.vofasbackend.datalayer.repositories.FeedbackRepository;
import com.backend.vofasbackend.datalayer.repositories.TranscriptionRepository;
import com.backend.vofasbackend.exceptions.exceptions.UnsupportedMediaTypeException;
import com.backend.vofasbackend.servicelayer.interfaces.FeedbackService;
import jakarta.annotation.PostConstruct;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Optional;
import java.util.UUID;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final Sinks.Many<FeedbackEntity> feedbackSink = Sinks.many().multicast().onBackpressureBuffer();

    private final FeedbackRepository feedbackRepository;
    private final TranscriptionRepository transcriptionRepository;
    private final LinkedList<FeedbackEntity> feedbackBuffer = new LinkedList<>();

    private final OpenAiAudioTranscriptionModel transcriptionModel;

    @Value("${VoFAS.store.path}")
    private String filePath;

    @PostConstruct
    public void init() {
        Path directoryPath = Paths.get(filePath);
        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
                System.out.println("Directory created at: " + directoryPath);
            } catch (IOException e) {
                System.err.println("Failed to create directory: " + e.getMessage());
                System.exit(-1);
            }
        } else {
            System.out.println("Directory already exists at: " + directoryPath);
        }
    }

    @Autowired
    public FeedbackServiceImpl(FeedbackRepository feedbackRepository, TranscriptionRepository transcriptionRepository, OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel){
        this.feedbackRepository = feedbackRepository;
        this.transcriptionRepository = transcriptionRepository;
        this.transcriptionModel = openAiAudioTranscriptionModel;
    }


    @Override
    public Mono<Void> saveFeedback(MultipartFile file, UUID validationToken) throws UnsupportedMediaTypeException {

        String contentType = file.getContentType();
        String filename = file.getOriginalFilename();

        if(!(contentType.equals("audio/mpeg") || contentType.equals("audio/mp4") || contentType.equals("audio/wav") ||
                filename.toLowerCase().endsWith(".mp3") || filename.toLowerCase().endsWith(".m4a") || filename.toLowerCase().endsWith(".wav"))){
            throw new UnsupportedMediaTypeException(FeedbackConstants.MESSAGE_415);
        }
        return persistFeedbackToDatabase(validationToken)
                .flatMap(feedbackID ->
                        saveFile(file, feedbackID)
                                .then(transcribeFeedback(feedbackID))
                                .subscribeOn(Schedulers.boundedElastic())
                )
                .then();

    }

    public Mono<Long> persistFeedbackToDatabase(UUID validationToken){
        return Mono.fromCallable(() -> {
            FeedbackEntity feedbackEntity = new FeedbackEntity();
            feedbackEntity.setFeedbackReceivedAt(LocalDateTime.now());
            feedbackEntity.setFeedbackState(FeedbackStateEnum.RECEIVED);
            feedbackEntity.setTranscription(null);
            feedbackEntity.setSentimentAnalysis(null);
            feedbackEntity.setFeedbackSource(null);
            feedbackEntity.setValidationToken(null);
            FeedbackEntity savedFeedback = feedbackRepository.save(feedbackEntity);
            return savedFeedback.getFeedbackId();
        });
    }


    private Mono<Long> saveFile(MultipartFile file, Long feedbackId) {
        return Mono.fromCallable(() ->{
            try {
                String originalFilename = file.getOriginalFilename();
                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String newFilename = "feedback_" + feedbackId + fileExtension;
                Path path = Paths.get(filePath, newFilename);
                Files.createDirectories(path.getParent());
                file.transferTo(path.toFile());
                Optional<FeedbackEntity> feedbackEntityOptional = feedbackRepository.getFeedbackEntityByFeedbackId(feedbackId);
                if(feedbackEntityOptional.isPresent()){
                    FeedbackEntity feedbackEntity = feedbackEntityOptional.get();
                    feedbackEntity.setFilePath(path.toString());
                    feedbackRepository.save(feedbackEntity);
                }
                return feedbackId;
                // TODO take a look here
            } catch (IOException e) {
                throw new RuntimeException("Failed to save feedback file", e);
            }
        });
    }

    public Mono<Void> transcribeFeedback(Long feedbackID){
        return Mono.fromRunnable(()->{

            Optional<FeedbackEntity> savedFeedback = feedbackRepository.getFeedbackEntityByFeedbackId(feedbackID);
            if(savedFeedback.isPresent()){
                FeedbackEntity feedbackEntity = savedFeedback.get();
                feedbackEntity.setFeedbackState(FeedbackStateEnum.WAITING_FOR_TRANSCRIPTION);
                Resource resource = new FileSystemResource(feedbackEntity.getFilePath());
                TranscriptionEntity transcriptionEntity = new TranscriptionEntity();
                transcriptionEntity.setTranscriptionRequestedAt(LocalDateTime.now());
                AudioTranscriptionResponse response = transcriptionModel.call(new AudioTranscriptionPrompt(resource));
                transcriptionEntity.setTranscriptionReceivedAt(LocalDateTime.now());
                transcriptionEntity.setTranscription(response.getResult().getOutput());
                transcriptionEntity.setFeedback(feedbackEntity);
                feedbackEntity.setTranscription(transcriptionEntity);
                feedbackEntity.setFeedbackState(FeedbackStateEnum.TRANSCRIBED);
                transcriptionRepository.save(transcriptionEntity);
                feedbackRepository.save(feedbackEntity);
            }
        //TODO look for potential risks & errors
        });
    }


    // Add new feedback to the buffer and emit it to subscribers
    public void addFeedback(FeedbackEntity feedback) {
        synchronized (feedbackBuffer) {
            if (feedbackBuffer.size() >= 5) {
                feedbackBuffer.removeFirst(); // Remove the oldest feedback if size exceeds 5
            }
            feedbackBuffer.addLast(feedback); // Add the new feedback to the buffer
        }

        feedbackSink.tryEmitNext(feedback); // Emit feedback to subscribers
    }


    @Override
    public Flux<FeedbackEntity> getFeedbackStream() {
        synchronized (feedbackBuffer) {
            // Emit existing feedbacks first, then subscribe to new ones in real-time
            return Flux.concat(Flux.fromIterable(feedbackBuffer), feedbackSink.asFlux());
        }
    }



}


