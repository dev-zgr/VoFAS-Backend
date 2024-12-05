package com.backend.vofasbackend.presentationlayer.datatransferobjects;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) for the ValidationTokenEntity.
 * This DTO is used for transferring validation token data between layers, such as from service to controller.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "ValidationTokenDTO",
        description = "DTO representing the ValidationToken entity, used for transferring validation token data."
)
public class ValidationTokenDTO {

    /**
     * The unique identifier for the validation token.
     */
    @Schema(description = "The unique identifier for the validation token.", example = "cfd7d2b8-88f2-437f-9ab1-c3de47b5d0a0")
    private String validationToken;

    /**
     * The ID of the kiosk associated with the validation token.
     */
    @Schema(description = "The ID of the kiosk associated with the validation token.")
    @JsonBackReference
    private Long kiosk;

    /**
     * The state of the validation token.
     */
    @Schema(description = "The current state of the validation token.", example = "VALID")
    private String validationTokenState;

    /**
     * The timestamp when the validation token was created.
     */
    @Schema(description = "The timestamp when the validation token was created.", example = "2024-11-01T12:00:00")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime tokenCreateAt;

    /**
     * The timestamp when the validation token was used.
     */
    @Schema(description = "The timestamp when the validation token was used.", example = "2024-11-01T15:30:00")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime tokenUsedAt;

    /**
     * The ID of the feedback associated with this validation token.
     */
    @Schema(description = "The ID of the feedback associated with this validation token.", example = "123")
    private Long feedback;
}

