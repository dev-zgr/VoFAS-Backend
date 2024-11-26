package com.backend.vofasbackend.datalayer.enums;

/**
 * Enum representing the possible states of a validation token.
 * This enum tracks the status of the validation token used for authenticating or validating feedback.
 *
 * - VALID: The token is still valid and can be used for authentication or validation.
 * - USED: The token has been used and is no longer valid for authentication or validation.
 * - INVALID: The token is considered invalid, either due to expiration or other issues.
 */
public enum ValidationTokenState {
    /**
     * Token is valid and can be used for authentication or validation.
     */
    VALID,

    /**
     * Token has been used and can no longer be used for authentication or validation.
     */
    USED,

    /**
     * Token is invalid due to expiration, incorrect usage, or other issues.
     */
    INVALID
}