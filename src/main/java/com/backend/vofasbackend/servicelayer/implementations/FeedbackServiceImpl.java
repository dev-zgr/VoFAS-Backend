package com.backend.vofasbackend.servicelayer.implementations;

import com.backend.vofasbackend.contants.FeedbackConstants;
import com.backend.vofasbackend.datalayer.entities.FeedbackEntity;
import com.backend.vofasbackend.datalayer.entities.TranscriptionEntity;
import com.backend.vofasbackend.datalayer.enums.FeedbackStateEnum;
import com.backend.vofasbackend.datalayer.enums.SentimentStateEnum;
import com.backend.vofasbackend.datalayer.repositories.FeedbackRepository;
import com.backend.vofasbackend.datalayer.repositories.TranscriptionRepository;
import com.backend.vofasbackend.exceptions.exceptions.InvalidFilterOptionException;
import com.backend.vofasbackend.exceptions.exceptions.ResourceNotFoundException;
import com.backend.vofasbackend.exceptions.exceptions.UnsupportedMediaTypeException;
import com.backend.vofasbackend.presentationlayer.datatransferobjects.*;
import com.backend.vofasbackend.servicelayer.interfaces.FeedbackService;
import com.backend.vofasbackend.servicelayer.mappers.FeedbackMapper;
import com.backend.vofasbackend.servicelayer.mappers.KioskMapper;
import com.backend.vofasbackend.servicelayer.mappers.ValidationTokenMapper;
import com.backend.vofasbackend.servicelayer.tools.HashTool;
import com.mpatric.mp3agic.Mp3File;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.UUID;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final Sinks.Many<FeedbackDTO> feedbackSink;
    private final FeedbackRepository feedbackRepository;
    private final TranscriptionRepository transcriptionRepository;
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
    public FeedbackServiceImpl(FeedbackRepository feedbackRepository, TranscriptionRepository transcriptionRepository, OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel) {
        this.feedbackRepository = feedbackRepository;
        this.transcriptionRepository = transcriptionRepository;
        this.transcriptionModel = openAiAudioTranscriptionModel;
        feedbackSink = Sinks.many().multicast().onBackpressureBuffer();
    }


    @Override
    public Mono<Void> saveFeedback(MultipartFile file, UUID validationToken) throws UnsupportedMediaTypeException {

        String contentType = file.getContentType();
        String filename = file.getOriginalFilename();

        if (!(contentType.equals("audio/mpeg") || contentType.equals("audio/mp4") || contentType.equals("audio/wav") ||
                filename.toLowerCase().endsWith(".mp3") || filename.toLowerCase().endsWith(".m4a") || filename.toLowerCase().endsWith(".wav"))) {
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

    public Mono<Long> persistFeedbackToDatabase(UUID validationToken) {
        return Mono.fromCallable(() -> {
            FeedbackEntity feedbackEntity = new FeedbackEntity();
            feedbackEntity.setFeedbackReceivedAt(LocalDateTime.now());
            feedbackEntity.setFeedbackState(FeedbackStateEnum.RECEIVED);
            feedbackEntity.setTranscription(null);
            feedbackEntity.setSentimentAnalysis(null);
            feedbackEntity.setFeedbackSource(null);
            feedbackEntity.setValidationToken(null);
            FeedbackEntity savedFeedback = feedbackRepository.save(feedbackEntity);
            return savedFeedback.getFeedbackID();
        });
    }


    private Mono<Long> saveFile(MultipartFile file, Long feedbackId) {
        return Mono.fromCallable(() -> {
            try {
                String originalFilename = file.getOriginalFilename();
                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String newFilename = "feedback_" + feedbackId + fileExtension;
                Path path = Paths.get(filePath, newFilename);
                Files.createDirectories(path.getParent());
                file.transferTo(path.toFile());

                String contentType = file.getContentType();
                String filename = file.getOriginalFilename();
                Duration feedbackDuration = null;
                if(contentType.equals("audio/mpeg") || filename.toLowerCase().endsWith(".mp3")){
                    Mp3File mp3File = new Mp3File(path);
                    feedbackDuration = Duration.ofSeconds(mp3File.getLengthInSeconds());
                }else if(contentType.equals("audio/wav") || filename.toLowerCase().endsWith(".wav")){
                    AudioFileFormat audioFileFormat = AudioSystem.getAudioFileFormat(path.toFile());
                    feedbackDuration = Duration.ofSeconds(Math.round(audioFileFormat.getFrameLength()  / audioFileFormat.getFormat().getFrameRate()));
                }


                    Optional<FeedbackEntity> feedbackEntityOptional = feedbackRepository.getFeedbackEntityByFeedbackID(feedbackId);
                if (feedbackEntityOptional.isPresent()) {
                    FeedbackEntity feedbackEntity = feedbackEntityOptional.get();
                    feedbackEntity.setFilePath(path.toString());
                    feedbackEntity.setFile_hash(HashTool.hashFile(path.toFile()));
                    feedbackEntity.setFeedbackDuration(feedbackDuration);
                    feedbackRepository.save(feedbackEntity);
                }
                return feedbackId;
                // TODO take a look here
            } catch (IOException e) {
                throw new RuntimeException("Failed to save feedback file", e);
            }
        });
    }

    public Mono<Void> transcribeFeedback(Long feedbackID) {
        return Mono.fromRunnable(() -> {

            Optional<FeedbackEntity> savedFeedback = feedbackRepository.getFeedbackEntityByFeedbackID(feedbackID);
            if (savedFeedback.isPresent()) {
                FeedbackEntity feedbackEntity = savedFeedback.get();
                feedbackEntity.setFeedbackState(FeedbackStateEnum.WAITING_FOR_TRANSCRIPTION);
                Resource resource = new FileSystemResource(feedbackEntity.getFilePath());
                TranscriptionEntity transcriptionEntity = new TranscriptionEntity();
                transcriptionEntity.setTranscriptionRequestedAt(LocalDateTime.now());
                AudioTranscriptionResponse response = transcriptionModel.call(new AudioTranscriptionPrompt(resource));
                transcriptionEntity.setTranscriptionReceivedAt(LocalDateTime.now());
                transcriptionEntity.setTranscription(response.getResult().getOutput());
                transcriptionEntity.setFeedback(feedbackEntity);
                transcriptionEntity.setTranscriptionHash(HashTool.hashString(transcriptionEntity.getTranscription()));
                feedbackEntity.setTranscription(transcriptionEntity);
                feedbackEntity.setFeedbackState(FeedbackStateEnum.TRANSCRIBED);
                transcriptionRepository.save(transcriptionEntity);
                feedbackRepository.save(feedbackEntity);
                sinkFeedback(feedbackEntity);
            }
            //TODO look for potential risks & errors
        });
    }

    @Override
    public Flux<FeedbackDTO> getFeedbackStream() {
        return feedbackSink.asFlux();
    }

    @Override
    @Transactional
    public FeedbackDTO getFeedbackById(Long feedbackID, boolean getFeedbackSource, boolean getValidation) throws ResourceNotFoundException {
        Optional<FeedbackEntity> feedback = feedbackRepository.getFeedbackEntityByFeedbackID(feedbackID);
        if (feedback.isPresent()) {
            FeedbackEntity feedbackEntity = feedback.get();
            FeedbackDTO feedbackDTO = FeedbackMapper.mapFeedbackEntityToFeedbackDTO(feedbackEntity, new FeedbackDTO(), new TranscriptionDTO(), new SentimentAnalysisDTO());
            if (getFeedbackSource && feedbackEntity.getFeedbackSource() != null) {
                feedbackDTO.setFeedbackSource(KioskMapper.mapKioskEntityToKioskDTO(feedbackEntity.getFeedbackSource(), new KioskDTO()));
            }
            if (getValidation && feedbackEntity.getValidationToken() != null) {
                feedbackDTO.setValidationTokenDTO(ValidationTokenMapper.mapValidationTokenEntityToValidationTokenDTO(feedbackEntity.getValidationToken(), new ValidationTokenDTO()));
            }
            return feedbackDTO;
        } else {
            throw new ResourceNotFoundException(
                    FeedbackEntity.class.getTypeName(),
                    "feedbackID",
                    feedbackID.toString()
            );
        }
    }

    @Override
    @Transactional
    public Page<FeedbackDTO> getFeedbacks(int pageNumber, String sortBy, boolean ascending, String startDate, String endDate, String feedbackState, String sentimentState) throws InvalidFilterOptionException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        if (!"feedbackID".equalsIgnoreCase(sortBy) && !"feedbackReceivedAt".equalsIgnoreCase(sortBy)) {
            throw new InvalidFilterOptionException("sortBy", sortBy);
        }
        Sort.Direction direction = ascending ? Sort.Direction.ASC : Sort.Direction.DESC;
        LocalDateTime start = null;
        if (startDate != null && !startDate.isBlank()) {
            try {
                start = LocalDate.parse(startDate, formatter).atStartOfDay();
            } catch (DateTimeParseException e) {
                throw new InvalidFilterOptionException("startDate", startDate);
            }
        }
        LocalDateTime end = null;
        if (endDate != null && !endDate.isBlank()) {
            try {
                end = LocalDate.parse(endDate, formatter).atStartOfDay();
            } catch (DateTimeParseException e) {
                throw new InvalidFilterOptionException("endDate", endDate);
            }
        }
        if (start != null && end != null && end.isBefore(start)) {
            throw new InvalidFilterOptionException("dateRange", "endDate must be later than or equal to startDate");
        }
        FeedbackStateEnum feedbackStateEnum = null;
        if (feedbackState != null && !feedbackState.isBlank()) {
            try {
                feedbackStateEnum = FeedbackStateEnum.valueOf(feedbackState.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new InvalidFilterOptionException("feedbackState", feedbackState);
            }
        }
        SentimentStateEnum sentimentStateEnum = null;
        if (sentimentState != null && !sentimentState.isBlank()) {
            try {
                sentimentStateEnum = SentimentStateEnum.valueOf(sentimentState.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new InvalidFilterOptionException("sentimentState", sentimentState);
            }
        }
        Pageable pageable = PageRequest.of(pageNumber, 10, Sort.by(direction, sortBy));
        Page<FeedbackEntity> feedbackEntities = feedbackRepository.findFeedbacksByCriteria(
                feedbackStateEnum,
                null,
                sentimentStateEnum,
                start,
                end,
                pageable
        );

//        feedbackEntities = feedbackRepository.getFeedbackEntitiesByFeedbackStateAndTranscription(feedbackStateEnum.toString(), pageable);
        return feedbackEntities.map(feedbackEntity -> {
            FeedbackDTO feedbackDTO = FeedbackMapper.mapFeedbackEntityToFeedbackDTO(feedbackEntity, new FeedbackDTO(), new TranscriptionDTO(), new SentimentAnalysisDTO());
            if (feedbackEntity.getFeedbackSource() != null) {
                feedbackDTO.setFeedbackSource(KioskMapper.mapKioskEntityToKioskDTO(feedbackEntity.getFeedbackSource(), new KioskDTO()));
            }
            if (feedbackEntity.getValidationToken() != null) {
                feedbackDTO.setValidationTokenDTO(ValidationTokenMapper.mapValidationTokenEntityToValidationTokenDTO(feedbackEntity.getValidationToken(), new ValidationTokenDTO()));
            }
            return feedbackDTO;
        });
    }




    private void sinkFeedback(FeedbackEntity feedbackEntity) {
        FeedbackDTO feedbackDTO = FeedbackMapper.mapFeedbackEntityToFeedbackDTO(feedbackEntity, new FeedbackDTO(), new TranscriptionDTO(), new SentimentAnalysisDTO());
        if (feedbackEntity.getFeedbackSource() != null) {
            feedbackDTO.setFeedbackSource(KioskMapper.mapKioskEntityToKioskDTO(feedbackEntity.getFeedbackSource(), new KioskDTO()));
        }
        if (feedbackEntity.getValidationToken() != null) {
            feedbackDTO.setValidationTokenDTO(ValidationTokenMapper.mapValidationTokenEntityToValidationTokenDTO(feedbackEntity.getValidationToken(), new ValidationTokenDTO()));
        }
        feedbackSink.tryEmitNext(feedbackDTO);
    }

}


