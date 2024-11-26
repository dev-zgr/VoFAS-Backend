package com.backend.vofasbackend.presentationlayer.datatransferobjects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO class for transferring feedback data.
 * This class is used to carry the feedback data without exposing the entire entity.
 */
@Schema(
        name = "FeedbackDTO",
        description = "Schema to hold Feedback information"
)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FeedbackDTO {

    @Schema(description = "Unique identifier for the feedback", example = "1")
    private Long feedbackId;

    @Schema(description = "The transcription of the audio feedback", example = "The transcription of the feedback audio")
    private String transcription;

    @Schema(description = "The current processing state of the feedback", example = "FEEDBACK_RECEIVED")
    private String processingState;

    @Schema(description = "The final state of the feedback after processing (e.g., Positive, Neutral, Negative)", example = "POSITIVE")
    private String feedbackState;

    @Schema(description = "The timestamp when the feedback was uploaded by the user", example = "2024-11-26T12:30:00")
    private LocalDateTime feedbackUploadedAt;

    @Schema(description = "The timestamp when the transcription request was sent for the audio file", example = "2024-11-26T12:45:00")
    private LocalDateTime feedbackTranscriptionSentAt;

    @Schema(description = "The timestamp when the transcription result was received", example = "2024-11-26T12:50:00")
    private LocalDateTime feedbackTranscriptionReceivedAt;

    @Schema(description = "The timestamp when the feedback was sent for sentiment analysis", example = "2024-11-26T12:55:00")
    private LocalDateTime feedbackSentForSentimentAnalysisAt;

    @Schema(description = "The timestamp when the sentiment analysis result was received", example = "2024-11-26T13:00:00")
    private LocalDateTime feedbackReceivedFromSentimentAnalysisAt;

    @Schema(description = "The ID of the kiosk where the feedback was collected", example = "5")
    private Long kioskId;

    @Schema(description = "The validation token ID associated with this feedback", example = "uuid-example-token")
    @JsonManagedReference
    private ValidationTokenDTO validationToken;
}

