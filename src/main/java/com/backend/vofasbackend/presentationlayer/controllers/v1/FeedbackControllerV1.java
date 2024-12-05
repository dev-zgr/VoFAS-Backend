package com.backend.vofasbackend.presentationlayer.controllers.v1;

import com.backend.vofasbackend.contants.FeedbackConstants;
import com.backend.vofasbackend.datalayer.entities.FeedbackEntity;
import com.backend.vofasbackend.presentationlayer.datatransferobjects.ErrorResponseDTO;
import com.backend.vofasbackend.presentationlayer.datatransferobjects.ResponseDTO;
import com.backend.vofasbackend.servicelayer.interfaces.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.util.UUID;

/**
 * The {@code FeedbackControllerV1} class provides the API endpoints for managing feedback-related operations
 * in the VoFAS system. This controller exposes functionalities to retrieve feedback, upload feedback files,
 * and apply sorting, filtering, and pagination to feedback data. It is designed to support the VoFAS frontend
 * by facilitating the processing and handling of feedback submissions, including multimedia feedback (e.g., MP4 files).
 *
 * <p>The endpoints in this controller allow for actions such as:</p>
 * <ul>
 *     <li>Fetching feedback by feedback ID.</li>
 *     <li>Uploading files associated with feedback submissions.</li>
 *     <li>Sorting and filtering feedback based on various criteria.</li>
 *     <li>Handling errors related to feedback processing.</li>
 * </ul>
 *
 * <p>This class is part of the API version 1 and supports cross-origin requests to ensure accessibility from
 * different domains or frontends.</p>
 *
 * @version 1.0
 * @since 2024-11-27
 */

@Tag(
        name = "FeedBack Controller:V1",
        description = "This controller provides functionalities to support the development of the VoFAS frontend."
)
@RestController
@RequestMapping(value = "/api/v1")
@CrossOrigin(origins = "${VoFAS.crossorigin.url}")
public class FeedbackControllerV1 {

    private FeedbackService feedbackService;

    public FeedbackControllerV1(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @Operation(
            summary = "This API endpoint streams the latest processed feedbacks",
            description = "Streams the latest feedbacks"
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
    public Flux<FeedbackEntity> streamFeedbacks() {
        return feedbackService.getFeedbackStream();
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
        return null;
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
    public ResponseEntity<Object> getFeedbackByFeedbackID(
            @Parameter(description = "The unique identifier of the feedback", example = "123")
            @PathVariable Long feedbackID
    ) {
        return null;
    }


    @Operation(
            summary = "Upload feedback file for processing",
            description = "This API endpoint allows the user to upload a file (e.g., MP4) associated with feedback." +
                    " The file is processed and associated with the feedback record identified by the `validation-token`."
    )
    @ApiResponses({
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
            @RequestPart(name = "file") MultipartFile file
    ) {
            feedbackService.saveFeedback(file, validationToken).subscribe();
            ResponseDTO responseDTO = new ResponseDTO(FeedbackConstants.STATUS_201, FeedbackConstants.MESSAGE_201);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(responseDTO);
    }


}
