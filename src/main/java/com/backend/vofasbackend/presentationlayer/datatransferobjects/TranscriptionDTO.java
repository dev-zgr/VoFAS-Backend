package com.backend.vofasbackend.presentationlayer.datatransferobjects;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;


/**
 * Represents the transcription data for feedback, including the transcription text and timestamps.
 */
@Schema(
        name = "TranscriptionDTO",
        description = "Schema to hold the transcription data related to the feedback, including the transcription text and related timestamps."
)
@Data
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TranscriptionDTO {

    /**
     * The transcription text for the feedback.
     */
    @Schema(description = "The transcription text for the feedback.", example = "This is the feedback transcription.")
    private String transcription;

    /**
     * SHA-256 Hash of transcription
     */
    @Schema(description = "The hash for the transcription text", example = "43njk42k53h3454khk")
    private String transcriptionHash;

    /**
     * The timestamp when the transcription was requested.
     */
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Schema(description = "The timestamp when the transcription was requested.", example = "05-12-2024 14:30", type = "string", format = "date-time")
    private LocalDateTime transcriptionRequestedAt;

    /**
     * The timestamp when the transcription was received.
     */
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Schema(description = "The timestamp when the transcription was received.", example = "05-12-2024 14:45:00", type = "string", format = "date-time")
    private LocalDateTime transcriptionReceivedAt;
}
