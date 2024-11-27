package com.backend.vofasbackend.datalayer.entities;

import com.backend.vofasbackend.datalayer.enums.FeedbackStateEnum;
import com.backend.vofasbackend.datalayer.enums.ProcessingStateEnum;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.ManyToAny;

import java.time.LocalDateTime;

/**
 * Entity representing a feedback submission, typically containing an audio file
 * from a user, its transcription, and the corresponding processing states.
 *
 * This entity tracks the lifecycle of the feedback, including when the feedback
 * was uploaded, when the transcription and sentiment analysis processes were initiated
 * and completed, and the feedback's current state in the processing pipeline.
 *
 * The feedback may also be linked with a validation token and a specific kiosk where
 * the feedback was collected.
 */
@Entity
@Table(name = "feedback_table")
@Data
@ToString
@EqualsAndHashCode
public class FeedbackEntity {

    /**
     * Unique identifier for the feedback.
     * This is the primary key for the feedback entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "feedback_id", nullable = false, unique = true)
    private Long feedbackId;

    /**
     * The audio file submitted by the user as part of the feedback.
     * This field stores the feedback in binary format.
     */
    @Lob
    @Column(name = "feedback_file", nullable = false)
    private Byte[] audioFile;

    /**
     * The transcription of the audio feedback.
     * This field is populated after the audio is sent for transcription.
     */
    @Column(name = "transcription")
    private String transcription;

    /**
     * The current state of the processing for the feedback.
     * This tracks the status of actions like transcription, sentiment analysis, etc.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "processing_state", nullable = false)
    private ProcessingStateEnum processingState;

    /**
     * The overall state of the feedback.
     * This field represents the final status of the feedback after processing like Positive, Neutral or Negative.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "feedback_state", nullable = false)
    private FeedbackStateEnum feedbackState;

    /**
     * The timestamp when the feedback was uploaded by the user.
     * This represents the initial submission time.
     */
    @Column(name = "feedback_uploaded_at", nullable = false)
    private LocalDateTime feedbackUploadedAt;

    /**
     * The timestamp when the transcription request was sent for the audio file.
     * This is used to track when the transcription process started.
     */
    @Column(name = "feedback_transcription_sent_at")
    private LocalDateTime feedbackTranscriptionSentAt;

    /**
     * The timestamp when the transcription result was received.
     * This is used to track the completion of the transcription process.
     */
    @Column(name = "feedback_transcription_received_at")
    private LocalDateTime feedbackTranscriptionReceivedAt;

    /**
     * The timestamp when the feedback was sent for sentiment analysis.
     * This is used to track when sentiment analysis started.
     */
    @Column(name = "feedback_sent_for_sentiment_analysis_at")
    private LocalDateTime feedbackSentForSentimentAnalysisAt;

    /**
     * The timestamp when the sentiment analysis result was received.
     * This is used to track when the sentiment analysis process was completed.
     */
    @Column(name = "feedback_received_from_sentiment_analysis_at")
    private LocalDateTime feedbackReceivedFromSentimentAnalysisAt;

    /**
     * The kiosk where the feedback was collected.
     * This is a reference to the kiosk entity that the feedback belongs to.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kiosk_id")
    private KioskEntity kiosk;

    /**
     * The validation token associated with this feedback.
     * The validation token is used to authenticate the feedback submission.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "validation_token_id")
    private ValidationTokenEntity validationToken;

    /**
     * Default constructor for Feedback Entity
     */
    public FeedbackEntity() {
        this.audioFile = null;
        this.transcription = null;
        this.processingState = null;
        this.feedbackId = null;
        this.feedbackUploadedAt = null;
        this.feedbackTranscriptionSentAt = null;
        this.feedbackTranscriptionReceivedAt = null;
        this.feedbackSentForSentimentAnalysisAt = null;
        this.feedbackReceivedFromSentimentAnalysisAt = null;
        this.kiosk = null;
        this.validationToken = null;
    }


}
