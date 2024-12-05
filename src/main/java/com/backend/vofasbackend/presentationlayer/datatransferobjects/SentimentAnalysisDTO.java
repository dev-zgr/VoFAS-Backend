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
 * Represents the sentiment analysis data associated with a feedback, including sentiment state and timestamps.
 */
@Schema(
        name = "SentimentAnalysisDTO",
        description = "Schema to hold the sentiment analysis data for the feedback, including the sentiment state and related timestamps."
)
@Data
@ToString
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SentimentAnalysisDTO {


    /**
     * The state of the sentiment analysis for the feedback.
     */
    @Schema(description = "The state of the sentiment analysis for the feedback.", example = "Positive")
    private String sentimentState;

    /**
     * The timestamp when the sentiment analysis was requested.
     */
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    @Schema(description = "The timestamp when the sentiment analysis was requested.", example = "05-12-2024 14:30", type = "string", format = "date-time")
    private LocalDateTime analysisRequestedAt;

    /**
     * The timestamp when the sentiment analysis was received.
     */
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Schema(description = "The timestamp when the sentiment analysis was received.", example = "05-12-2024 14:45:00", type = "string", format = "date-time")
    private LocalDateTime analysisReceivedAt;


}
