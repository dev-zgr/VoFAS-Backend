package com.backend.vofasbackend.datalayer.entities;


import com.backend.vofasbackend.datalayer.enums.ValidationTokenStateEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing a validation token associated with a kiosk.
 * The validation token is used to authenticate or validate feedback and is linked to a specific kiosk and feedback.
 */
@Entity
@Table(name = "validation_tokens")
@Data
public class ValidationTokenEntity {

    /**
     * The unique validation token identifier.
     * This is the primary key for the ValidationToken entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "validation_token", nullable = false, unique = true)
    private UUID validationToken;

    /**
     * The kiosk associated with this validation token.
     * A validation token is assigned to a specific kiosk, and it can be used to validate feedback from that kiosk.
     */
    @ManyToOne
    @JoinColumn(name = "kiosk_id", referencedColumnName = "kiosk_id", nullable = false)
    private KioskEntity parentKiosk;

    /**
     * The state of the validation token.
     * This field represents the current status of the token (e.g., active, expired, used).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "validation_token_state", nullable = false)
    private ValidationTokenStateEnum validationTokenStateEnum;

    /**
     * The timestamp when the validation token was created.
     * This field tracks when the token was initially generated.
     */
    @Column(name = "token_create_at", nullable = false)
    private LocalDateTime tokenCreateAt;

    /**
     * The timestamp when the validation token was used.
     * This field tracks when the token was used for feedback validation.
     */
    @Column(name = "token_used_at")
    private LocalDateTime tokenUsedAt;

    /**
     * The feedback entity associated with this validation token.
     * This field is used to link a feedback entity with the validation token for validation purposes.
     */
    @OneToOne
    @JoinColumn(name = "feedback_id", referencedColumnName = "feedback_id")
    private FeedbackEntity userToken;

    /**
     * No-argument constructor for JPA.
     * This constructor is required for JPA to create instances of the entity.
     */
    public ValidationTokenEntity() {
        // Default initialization (optional)
        this.validationToken = UUID.randomUUID();
        this.validationTokenStateEnum = ValidationTokenStateEnum.VALID;
        this.tokenCreateAt = LocalDateTime.now();
        this.tokenUsedAt = null;
    }
}
