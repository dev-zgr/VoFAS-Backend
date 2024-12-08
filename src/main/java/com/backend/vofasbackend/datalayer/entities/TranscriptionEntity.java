package com.backend.vofasbackend.datalayer.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * Represents transcription data in the system.
 * Mapped to the "transcription_table" in the database.
 * Contains timestamps for request and receipt, and a one-to-one relationship with {@link FeedbackEntity}.
 */
@Entity
@Table(name = "transcription_table")
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TranscriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transcription_id", nullable = false, unique = true, updatable = false)
    private Long transcriptionID;

    /**
     * The transcription of the audio feedback.
     * This field is populated after the audio is sent for transcription.
     */
    @Lob
    @Column(name = "transcription", nullable = false, updatable = false)
    private String transcription;

    /**
     * The timestamp when the transcription request was sent for the audio file.
     * This is used to track when the transcription process started.
     */
    @Column(name = "transcription_requested_at", nullable = false)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime transcriptionRequestedAt;

    /**
     * The timestamp when the transcription result was received.
     * This is used to track the completion of the transcription process.
     */
    @Column(name = "transcription_received_at", nullable = false)
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime transcriptionReceivedAt;


    /**
     * One-to-one relationship with Feedback entity.
     * Feedback is linked via the "transcription" field in the Feedback entity.
     */
    @OneToOne(mappedBy = "transcription")
    private FeedbackEntity feedback;
}