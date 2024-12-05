package com.backend.vofasbackend.datalayer.entities;

import com.backend.vofasbackend.datalayer.enums.KioskStateEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a kiosk in the system where Validation tokens (ie qr codes) are created.
 * A kiosk has a unique ID, name, description, state, and password.
 * It can be linked with multiple feedbacks and validation tokens.
 */
@Entity
@Table(name = "kiosk_table")
@Data
@ToString
@EqualsAndHashCode
public class KioskEntity {

    /**
     * Unique identifier for the kiosk.
     * This is the primary key for the Kiosk entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kiosk_id", nullable = false, unique = true, updatable = false)
    private Long kioskID;

    /**
     * The name of the kiosk.
     * This field can be used for display purposes.
     */
    @Column(name = "kiosk_name", nullable = false, length = 64)
    private String kioskName;

    /**
     * A brief description of the kiosk.
     * This field provides additional information about the kiosk.
     */
    @Column(name = "kiosk_description", nullable = false, length = 256)
    private String kioskDescription;

    /**
     * The current state of the kiosk.
     * This field represents the operational or status state of the kiosk.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "kiosk_state", nullable = false)
    private KioskStateEnum kioskStateEnum;

    /**
     * The key for the kiosk.
     * This key is used to match kiosk with the system.
     */
    @Column(name = "kiosk_key", nullable = false, unique = true, updatable = false)
    @Max(999999)
    @Min(0)
    private Integer kioskKey;

    /**
     * List of feedback entities associated with the kiosk.
     * This represents the feedback collected by this specific kiosk.
     */
    @OneToMany(mappedBy = "feedbackSource", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FeedbackEntity> feedbackEntities;

    /**
     * List of validation tokens associated with the kiosk.
     * This field represents tokens used to validate kiosk feedback.
     */
    @OneToMany(mappedBy = "parentKiosk", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ValidationTokenEntity> validationTokens;

    /**
     * No-argument constructor for JPA.
     * This constructor is required for JPA to create instances of the entity.
     */
    public KioskEntity() {
        this.kioskName = null;
        this.kioskDescription = null;
        this.kioskStateEnum = null;
        this.kioskKey = null;
        this.feedbackEntities = new ArrayList<>();
        this.validationTokens = new ArrayList<>();
    }

    /**
     * Adds a validation token to the kiosk.
     * This method associates a validation token with the current kiosk by adding it to the list of validation tokens.
     *
     * @param validationToken the validation token to be added to the kiosk
     * @throws IllegalArgumentException if the validation token is null
     */
    public void addValidationToken(ValidationTokenEntity validationToken) {
        if (validationToken == null) {
            throw new IllegalArgumentException("Validation token cannot be null");
        }
        this.validationTokens.add(validationToken);
    }

    /**
     * Adds a feedback to the kiosk.
     * This method associates a feedback entity with the current kiosk by adding it to the list of feedbacks.
     *
     * @param feedbackEntity the feedback entity to be added to the kiosk
     * @throws IllegalArgumentException if the feedback entity is null
     */
    public void addFeedback(FeedbackEntity feedbackEntity) {
        if (feedbackEntity == null) {
            throw new IllegalArgumentException("Feedback entity cannot be null");
        }
        this.feedbackEntities.add(feedbackEntity);
    }

}

