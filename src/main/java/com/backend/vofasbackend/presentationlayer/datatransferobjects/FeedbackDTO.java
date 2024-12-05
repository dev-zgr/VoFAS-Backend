package com.backend.vofasbackend.presentationlayer.datatransferobjects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.*;

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
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FeedbackDTO {

    @Schema(description = "Unique identifier for the feedback", example = "1")
    private Long feedbackId;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime feedbackReceivedAt;

    @Schema(description = "file path that is store ", example = "/Users/ozgurkamali/Desktop/VoFAS_Feedback/feedback_6.mp3")
    private String filePath;

    @Schema(description = "Current state of the feedback", example = "TRANSCRIBED")
    private String feedbackState;

    @JsonManagedReference
    @Schema(description = "Transcription details of the feedback")
    private TranscriptionDTO transcriptionDTO;

    @JsonManagedReference
    @Schema(description = "Sentiment analysis results of the feedback")
    private SentimentAnalysisDTO sentimentAnalysisDTO;

    @JsonManagedReference
    @Schema(description = "Source information of the feedback (Kiosk details)")
    private KioskDTO feedbackSource;

    @Schema(description = "Validation token details of the feedback")
    private ValidationTokenDTO validationTokenDTO;

}

