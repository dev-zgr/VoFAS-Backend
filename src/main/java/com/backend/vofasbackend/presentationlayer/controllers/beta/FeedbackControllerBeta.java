package com.backend.vofasbackend.presentationlayer.controllers.beta;

import com.backend.vofasbackend.contants.FeedbackConstants;
import com.backend.vofasbackend.datalayer.enums.ValidationTokenStateEnum;
import com.backend.vofasbackend.presentationlayer.datatransferobjects.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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

/**
 * {@code FeedbackControllerBeta} is a REST controller that provides API endpoints for managing feedback-related operations
 * in the VoFAS system (Beta version). The controller exposes functionalities for:
 * <ul>
 *   <li>Streaming feedbacks in real-time using Server-Sent Events (SSE).</li>
 *   <li>Retrieving feedbacks with pagination, sorting, and filtering capabilities.</li>
 *   <li>Uploading feedback files (e.g., MP4) for processing and storage.</li>
 * </ul>
 * <p>
 * This controller is intended for the beta version of the VoFAS system and may contain features that are subject to change.
 * The endpoints support the development of the VoFAS frontend, offering functionalities such as real-time feedback streaming
 * and file uploads.
 * </p>
 */
@Tag(
        name = "FeedBack Controller:Beta",
        description = "This controller provides beta functionality to support the development of the VoFAS frontend."
)
@Tag(
        name = "FeedBack Controller:Beta",
        description = "This controller provides beta functionality to support the development of the VoFAS frontend."
)
@RestController
@RequestMapping(value = "/api/beta")
@CrossOrigin(origins = "${VoFAS.crossorigin.url}")
public class FeedbackControllerBeta {

    @Operation(
            summary = "This API endpoint streams the latest processed feedbacks",
            description = "Streams the latest departments"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Reactive Connection between client and VoFAS backend established successfully and " +
                            "recently processed feedbacks are streaming to VoFAS Frontend."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "There are is live feedback to stream nor no feedbacks to send to user",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error. This response maybe caused by any Java exception",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            ),

    })
    @GetMapping(value = "/feedback/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<Flux<FeedbackDTO>> streamFeedbacks() {
        List<FeedbackDTO> feedbackList = createRandomFeedbacks();
        Sinks.Many<FeedbackDTO> feedbackSink = Sinks.many().unicast().onBackpressureBuffer();
        Flux.interval(Duration.ofSeconds(1))
                .take(10) // Stop after 10 emissions
                .doOnNext(tick -> {
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

    @Operation(
            summary = "fetches feedbacks in static manner",
            description = "This API endpoint statically retrieves feedbacks from the systems. Also provides parameters" +
                    "to retrieve feedbacks by pages & filtering & sorting"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Reactive Connection between client and VoFAS backend established successfully and " +
                            "recently processed feedbacks are streaming to VoFAS Frontend."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "This value is returned if there is no feedback to fetch",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error. This response maybe caused by any Java exception",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            ),

    })
    @GetMapping(value = "/feedback", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getFeedbacks(
            @Parameter(description = "The page number for pagination", example = "1")
            @RequestParam(name = "page-no", defaultValue = "0") int pageNumber,

            @Parameter(description = "Field by which feedbacks will be sorted", example = "feedbackId")
            @RequestParam(name = "sort-by", defaultValue = "feedbackId", required = false) String sortBy,

            @Parameter(description = "Specifies if the sorting is ascending (true) or descending (false)", example = "true")
            @RequestParam(name = "ascending", defaultValue = "false", required = false) boolean ascending,

            @Parameter(description = "A keyword to filter feedbacks", example = "feedbackState:positive")
            @RequestParam(name = "filter", defaultValue = "", required = false) String filter
    ) {
        Page<FeedbackDTO> feedbackPage = new PageImpl<>(createRandomFeedbacks(), PageRequest.of(0, 2), createRandomFeedbacks().size());
        int statusCode = new Random().nextInt(3);

        return switch (statusCode) {
            case 0 ->
                    ResponseEntity.ok(feedbackPage);
            case 1 ->
                    ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                            new ErrorResponseDTO("api/beta/feedback",
                                    FeedbackConstants.STATUS_400,
                                    FeedbackConstants.MESSAGE_400,
                                    LocalDateTime.now()
                            )
                    );
            case 2 ->
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                            new ErrorResponseDTO("api/beta/feedback",
                                    FeedbackConstants.STATUS_500,
                                    FeedbackConstants.MESSAGE_500,
                                    LocalDateTime.now()
                            )
                    );
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        };
    }

    @Operation(
            summary = "Fetch a specific feedback by its ID",
            description = "This API endpoint retrieves a single feedback by its unique `feedbackID`." +
                    " It allows fetching the feedback and provides error responses when no feedback is found or if an internal server error occurs."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Feedback successfully retrieved and returned to the client."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No feedback found with the provided `feedbackID`. This value is returned when the requested feedback does not exist.",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error. This response is triggered when an unexpected server-side error occurs (e.g., database connection failure, etc.).",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            ),
    })
    @GetMapping(value = "/feedback/{feedbackID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getFeedbackByFeedbackID(@PathVariable Long feedbackID) {
        int statusCode = new Random().nextInt(3);
        return switch (statusCode) {
            case 0 ->
                    ResponseEntity.ok().body(createFeedback(feedbackID, "Sample feedback message.",
                            "POSITIVE", 101L, new ValidationTokenDTO()));
            case 1 ->
                    ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                            new ErrorResponseDTO("api/beta/feedback",
                                    FeedbackConstants.STATUS_400,
                                    FeedbackConstants.MESSAGE_400,
                                    LocalDateTime.now()
                            )
                    );
            case 2 ->
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                            new ErrorResponseDTO("api/beta/feedback",
                                    FeedbackConstants.STATUS_500,
                                    FeedbackConstants.MESSAGE_500,
                                    LocalDateTime.now()
                            )
                    );
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        };
    }



    @Operation(
            summary = "Upload feedback file for processing",
            description = "This API endpoint allows the user to upload a file (e.g., MP4) associated with feedback." +
                    " The file is processed and associated with the feedback record identified by the `validation-token`."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized. The request does not include valid authentication credentials (e.g., missing or invalid authorization token).",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden. The user does not have permission to upload files for feedback, possibly due to insufficient roles or invalid `validation-token`.",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "415",
                    description = "Unsupported Media Type. The file type uploaded is not supported. Only specific file types (e.g., MP4) are allowed.",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error. This response is triggered when the server encounters an unexpected error during file processing (e.g., file storage issues, processing failure).",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "201",
                    description = "File successfully uploaded and feedback associated with the provided validation token has been created.",
                    content = @Content(
                            schema = @Schema(implementation = ResponseDTO.class)
                    )
            )
    })
    @PostMapping(path = "/feedback/{validation-token}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseDTO> uploadFeedbackFile(
            @PathVariable(name = "validation-token") UUID validationToken,
            @RequestPart(name = "file") MultipartFile file // The MP4 file
    ) {
        String fileName = file.getOriginalFilename();
        long fileSize = file.getSize();

        int statusCode = new Random().nextInt(5);

        switch (statusCode) {
            case 0: // 201 Created
                ResponseDTO successResponse = new ResponseDTO("Success", "File uploaded successfully");
                return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);
            case 1: // 500 Internal Server Error
                ResponseDTO errorResponse500 = new ResponseDTO("Error", "Internal server error occurred");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse500);
            case 2: // 403 Forbidden
                ResponseDTO errorResponse403 = new ResponseDTO("Forbidden", "You do not have permission to upload the file");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse403);
            case 3: // 401 Unauthorized
                ResponseDTO errorResponse401 = new ResponseDTO("Unauthorized", "Authentication failed or invalid token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse401);
            case 4: // 415 Unsupported Media Type
                ResponseDTO errorResponse415 = new ResponseDTO("Unsupported Media Type", "The file type is not supported");
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(errorResponse415);
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    private FeedbackDTO createFeedback(Long feedbackId, String transcription, String feedbackState, Long kioskId, ValidationTokenDTO validationToken) {
        FeedbackDTO feedbackDTO = new FeedbackDTO();
        feedbackDTO.setFeedbackId(feedbackId);
        TranscriptionDTO transcriptionDTO = new TranscriptionDTO();
        transcriptionDTO.setTranscription(transcription);
        feedbackDTO.setTranscriptionDTO(transcriptionDTO);
        feedbackDTO.setFeedbackState(feedbackState);
        KioskDTO kioskDTO = new KioskDTO();
        kioskDTO.setKioskId(kioskId);
        feedbackDTO.setValidationTokenDTO(new ValidationTokenDTO());
        return feedbackDTO;
    }

    private List<FeedbackDTO> createRandomFeedbacks(){
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
        return feedbackList;
    }
}
