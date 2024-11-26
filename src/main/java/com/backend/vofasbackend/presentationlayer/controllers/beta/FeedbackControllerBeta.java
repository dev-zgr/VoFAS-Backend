package com.backend.vofasbackend.presentationlayer.controllers.beta;

import com.backend.vofasbackend.contants.FeedbackConstants;
import com.backend.vofasbackend.datalayer.enums.ValidationTokenStateEnum;
import com.backend.vofasbackend.presentationlayer.datatransferobjects.FeedbackDTO;
import com.backend.vofasbackend.presentationlayer.datatransferobjects.ResponseDTO;
import com.backend.vofasbackend.presentationlayer.datatransferobjects.ValidationTokenDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Tag(
        name = "FeedBack Controller Beta",
        description = "This controller provides beta functionality to support the development of the VoFAS frontend."
)
@RestController
@RequestMapping(value = "/api/beta")
@CrossOrigin(origins = "${VoFAS.crossorigin.url}")
public class FeedbackControllerBeta {

    @GetMapping(value = "/feedback/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<Flux<FeedbackDTO>> streamFeedbacks() {

        ValidationTokenDTO validationTokenDTO = new ValidationTokenDTO();
        validationTokenDTO.setValidationToken(UUID.randomUUID().toString());
        validationTokenDTO.setKiosk(null); // Set appropriate KioskDTO if needed
        validationTokenDTO.setValidationTokenState(ValidationTokenStateEnum.USED.toString());
        validationTokenDTO.setTokenCreateAt(LocalDateTime.now());

        List<FeedbackDTO> feedbackList = Arrays.asList(
                createFeedback(1L, "The feedback from the kiosk was great.", "POSITIVE", 101L, validationTokenDTO),
                createFeedback(2L, "The kiosk was easy to use, but needs improvement.", "NEUTRAL", 102L, validationTokenDTO),
                createFeedback(3L, "The service was terrible, I am disappointed.", "NEGATIVE", 103L, validationTokenDTO),
                createFeedback(4L, "It was a great experience, very user-friendly.", "POSITIVE", 104L, validationTokenDTO),
                createFeedback(5L, "It could be better if the instructions were clearer.", "NEUTRAL", 105L, validationTokenDTO),
                createFeedback(6L, "Worst experience ever. The kiosk didn't work at all.", "NEGATIVE", 106L, validationTokenDTO)
        );

        Sinks.Many<FeedbackDTO> feedbackSink = Sinks.many().unicast().onBackpressureBuffer();

        // Emit feedback to the sink every second, limit to 10 emissions
        Flux.interval(Duration.ofSeconds(1))
                .take(10) // Stop after 10 emissions
                .doOnNext(tick -> {
                    // Loop over feedback list and emit one feedback at a time
                    if (!feedbackList.isEmpty()) {
                        FeedbackDTO feedback = feedbackList.get((int) (tick % feedbackList.size())); // Loop through the list
                        feedbackSink.tryEmitNext(feedback);
                    }
                })
                .subscribe();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(feedbackSink.asFlux());
    }

    @GetMapping(value = "/feedback", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getFeedbacks(
            @RequestParam(name = "page-no", defaultValue = "0", required = true) int pageNumber,
            @RequestParam(name = "sort-by", defaultValue = "feedbackId", required = false) String sortBy,
            @RequestParam(name = "ascending", defaultValue = "false", required = false) boolean ascending,
            @RequestParam(name = "filter", defaultValue = "", required = false) String filter
    ) {
        // Dummy predefined data
        ValidationTokenDTO validationTokenDTO = new ValidationTokenDTO();
        validationTokenDTO.setValidationToken(UUID.randomUUID().toString());
        validationTokenDTO.setKiosk(null);
        validationTokenDTO.setValidationTokenState(ValidationTokenStateEnum.USED.toString());
        validationTokenDTO.setTokenCreateAt(LocalDateTime.now());

        List<FeedbackDTO> feedbackList = Arrays.asList(
                createFeedback(1L, "The feedback from the kiosk was great.", "POSITIVE", 101L, validationTokenDTO),
                createFeedback(2L, "The kiosk was easy to use, but needs improvement.", "NEUTRAL", 102L, validationTokenDTO),
                createFeedback(3L, "The service was terrible, I am disappointed.", "NEGATIVE", 103L, validationTokenDTO),
                createFeedback(4L, "It was a great experience, very user-friendly.", "POSITIVE", 104L, validationTokenDTO),
                createFeedback(5L, "It could be better if the instructions were clearer.", "NEUTRAL", 105L, validationTokenDTO),
                createFeedback(6L, "Worst experience ever. The kiosk didn't work at all.", "NEGATIVE", 106L, validationTokenDTO)
        );

        // Apply filter
        List<FeedbackDTO> filteredFeedbacks = feedbackList.stream()
                .filter(feedback -> feedback.getTranscription().toLowerCase().contains(filter.toLowerCase()))
                .toList();

        // Apply sorting
        Sort sort = Sort.by(ascending ? Sort.Order.asc(sortBy) : Sort.Order.desc(sortBy));
        List<FeedbackDTO> sortedFeedbacks = filteredFeedbacks.stream()
                .sorted((f1, f2) -> ascending ? f1.getFeedbackId().compareTo(f2.getFeedbackId()) : f2.getFeedbackId().compareTo(f1.getFeedbackId()))
                .collect(Collectors.toList());

        // Pagination
        int start = Math.min(pageNumber * 2, sortedFeedbacks.size());
        int end = Math.min((pageNumber + 1) * 2, sortedFeedbacks.size());
        List<FeedbackDTO> paginatedFeedbacks = sortedFeedbacks.subList(start, end);
        Page<FeedbackDTO> feedbackPage = new PageImpl<>(paginatedFeedbacks, PageRequest.of(pageNumber, 2, sort), sortedFeedbacks.size());

        // Randomly decide the response status
        int statusCode = new Random().nextInt(3);

        switch (statusCode) {
            case 0: // 200 OK
                return ResponseEntity.ok(feedbackPage);
            case 1:
                throw new RuntimeException(FeedbackConstants.MESSAGE_400);
            case 2:
                throw new RuntimeException(FeedbackConstants.MESSAGE_500);
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value = "/feedback/{feedbackID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getFeedbackByFeedbackID(@PathVariable Long feedbackID) {
        // Randomly decide the response status
        int statusCode = new Random().nextInt(3);

        switch (statusCode) {
            case 0:
                FeedbackDTO feedback = createFeedback(feedbackID, "Sample feedback message.",
                        "POSITIVE", 101L, new ValidationTokenDTO());
                return ResponseEntity.ok(feedback);
            case 1:
                throw new RuntimeException(FeedbackConstants.MESSAGE_400);
            case 2:
                throw new RuntimeException(FeedbackConstants.MESSAGE_500);
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping(path = "/feedback/{validation-token}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseDTO> uploadFeedbackFile(
            @PathVariable(name = "validation-token") UUID validationToken,
            @RequestPart(name = "file") MultipartFile file // The MP4 file
    ) {
        // Extract file info
        String fileName = file.getOriginalFilename();
        long fileSize = file.getSize();

        // Process the file (e.g., save it to the server or database)
        // Example: Save the file to a directory or cloud storage.

        // For simplicity, just log the file info for now
        System.out.println("Received file: " + fileName + " (Size: " + fileSize + " bytes)");


        // Return a success response
        ResponseDTO responseDTO = new ResponseDTO("Success", "File uploaded successfully");
        return ResponseEntity.ok(responseDTO);
    }


    private FeedbackDTO createFeedback(Long feedbackId, String transcription, String feedbackState, Long kioskId, ValidationTokenDTO validationToken) {
        FeedbackDTO feedbackDTO = new FeedbackDTO();
        feedbackDTO.setFeedbackId(feedbackId);
        feedbackDTO.setTranscription(transcription);
        feedbackDTO.setFeedbackState(feedbackState);
        feedbackDTO.setKioskId(kioskId);
        feedbackDTO.setValidationToken(validationToken);
        return feedbackDTO;
    }
}
