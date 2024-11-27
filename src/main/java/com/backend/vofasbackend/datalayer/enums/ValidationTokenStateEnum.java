package com.backend.vofasbackend.datalayer.enums;

/**
 * Enum representing the possible states of a validation token.
 * This enum tracks the status of the validation token used for authenticating or validating feedback.
 * <ul>
 *     <li>VALID: The token is still valid and can be used for authentication or validation.</li>
 *     <li>USED: The token has been used and is no longer valid for authentication or validation.</li>
 *     <li>EXPIRED: The token is considered invalid, either due to expiration or other issues.</li>
 * <ul/>
 */
public enum ValidationTokenStateEnum {
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
    EXPIRED
}
