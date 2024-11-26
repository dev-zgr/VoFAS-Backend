package com.backend.vofasbackend.presentationlayer.datatransferobjects;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object (DTO) for the KioskEntity.
 * This DTO is used for transferring kiosk data between layers, such as from service to controller.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(
        name = "KioskDTO",
        description = "DTO representing the Kiosk entity, used for transferring kiosk data."
)
public class KioskDTO {

    /**
     * Unique identifier for the kiosk.
     */
    @Schema(description = "Unique identifier for the kiosk.", example = "1")
    private Long kioskId;

    /**
     * The name of the kiosk.
     */
    @Schema(description = "The name of the kiosk.", example = "Kiosk 1")
    private String kioskName;

    /**
     * A brief description of the kiosk.
     */
    @Schema(description = "A brief description of the kiosk.", example = "Outdoor kiosk at the city park.")
    private String kioskDescription;

    /**
     * The current state of the kiosk.
     */
    @Schema(description = "The current state of the kiosk.", example = "ACTIVE")
    private String kioskState;

    /**
     * The unique key for the kiosk.
     */
    @Schema(description = "The unique key for the kiosk.", example = "cfd7d2b8-88f2-437f-9ab1-c3de47b5d0a0")
    private String kioskKey;

    /**
     * List of validation tokens associated with the kiosk.
     */
    @Schema(description = "List of validation tokens associated with the kiosk.", example = "[]")
    @JsonManagedReference
    private List<ValidationTokenDTO> validationTokens;
}