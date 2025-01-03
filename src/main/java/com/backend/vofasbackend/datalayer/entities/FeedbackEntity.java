package com.backend.vofasbackend.datalayer.entities;

import com.backend.vofasbackend.datalayer.enums.FeedbackStateEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Entity representing a feedback submission, typically containing an audio file
 * from a user, its transcription, and the corresponding processing states.
 * <p>
 * This entity tracks the lifecycle of the feedback, including when the feedback
 * was uploaded, when the transcription and sentiment analysis processes were initiated
 * and completed, and the feedback's current state in the processing pipeline.
 * <p>
 * The feedback may also be linked with a validation token and a specific kiosk where
 * the feedback was collected.
 */
@Entity
@Table(name = "feedback_table")
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackEntity {

    /**
     * Unique identifier for the feedback.
     * This is the primary key for the feedback entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_id", nullable = false, unique = true, updatable = false)
    private Long feedbackID;

    /**
     * The timestamp when the feedback was uploaded by the user.
     * This represents the initial submission time.
     */
    @Column(name = "feedback_received_at", nullable = false, updatable = false)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime feedbackReceivedAt;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_hash")
    String file_hash;

    @Column(name = "feedback_duration")
    private Duration feedbackDuration;

    /**
     * The current state of the processing for the feedback.
     * This tracks the status of actions like transcription, sentiment analysis, etc.
     */
    @Column(name = "feedback_state", nullable = false)
    @Enumerated(EnumType.STRING)
    private FeedbackStateEnum feedbackState;

    /**
     * One-to-one relationship with the Transcription entity.
     * Transcription is linked via the "transcription_id" column.
     */
    @OneToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "transcription_id", referencedColumnName = "transcription_id")
    private TranscriptionEntity transcription;

    @OneToOne(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "sentiment_id", referencedColumnName = "sentiment_id")
    SentimentAnalysisEntity sentimentAnalysis;

    /**
     * The kiosk where the feedback was collected.
     * This is a reference to the kiosk entity that the feedback belongs to.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kiosk_id")
    private KioskEntity feedbackSource;


    /**
     * The validation token associated with this feedback.
     * The validation token is used to authenticate the feedback submission.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "validation_token_id")
    private ValidationTokenEntity validationToken;



}
