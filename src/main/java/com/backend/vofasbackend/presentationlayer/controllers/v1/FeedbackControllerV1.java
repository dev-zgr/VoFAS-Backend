package com.backend.vofasbackend.presentationlayer.controllers.v1;

import com.backend.vofasbackend.presentationlayer.datatransferobjects.ErrorResponseDTO;
import com.backend.vofasbackend.presentationlayer.datatransferobjects.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Tag(
        name = "FeedBack Controller Version 1",
        description = "This controller provides functionalities to support the development of the VoFAS frontend."
)
@RestController
@RequestMapping(value = "/api/v1")
@CrossOrigin(origins = "${VoFAS.crossorigin.url}")
public class FeedbackControllerV1 {

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
    public ResponseEntity<Object> streamFeedbacks() {
        return null;
    }

    @GetMapping(value = "/feedback", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getFeedbacks(
            @RequestParam(name = "page-no", defaultValue = "0", required = true) int pageNumber,
            @RequestParam(name = "sort-by", defaultValue = "feedbackId", required = false) String sortBy,
            @RequestParam(name = "ascending", defaultValue = "false", required = false) boolean ascending,
            @RequestParam(name = "filter", defaultValue = "", required = false) String filter
    ) {
        return null;
    }

    @GetMapping(value = "/feedback/{feedbackID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getFeedbackByFeedbackID(@PathVariable Long feedbackID) {
        return null;
    }


    @PostMapping(path = "/feedback/{validation-token}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseDTO> uploadFeedbackFile(
            @PathVariable(name = "validation-token") UUID validationToken,
            @RequestPart(name = "file") MultipartFile file // The MP4 file
    ) {
        return null;
    }

}
