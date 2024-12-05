package com.backend.vofasbackend.datalayer.entities;

import com.backend.vofasbackend.datalayer.enums.SentimentStateEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * Entity representing sentiment analysis data for feedback.
 * Tracks the transcription and analysis process, including timestamps
 * for when the analysis was requested and received.
 */
@Entity
@Table(name = "sentiment_table")
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class SentimentAnalysisEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sentiment_id", nullable = false, unique = true, updatable = false)
    private Long sentimentID;

    /**
     * The transcription of the audio feedback.
     * Populated after the audio is sent for transcription.
     */
    @Column(name = "sentiment_state", nullable = false, updatable = false, unique = false)
    @Enumerated(EnumType.STRING)
    private SentimentStateEnum sentimentState;

    /**
     * Timestamp when the sentiment analysis request was initiated.
     * Tracks the start of the sentiment analysis process.
     */
    @Column(name = "analysis_requested_at", nullable = false, updatable = false, unique = false)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime analysisRequestedAt;

    /**
     * Timestamp when the sentiment analysis result was received.
     * Tracks the completion of the sentiment analysis process.
     */
    @Column(name = "analysis_received_at", nullable = false,updatable = false, unique = false)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime analysisReceivedAt;

    /**
     * One-to-one relationship with Feedback entity.
     * This field links the SentimentAnalysis to a specific Feedback.
     */
    @OneToOne(mappedBy = "sentimentAnalysis")
    private FeedbackEntity feedback;
}
