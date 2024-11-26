package com.backend.vofasbackend.datalayer.enums;

/**
 * Enum representing the various states of a kiosk.
 * This enum tracks the current operational state of a kiosk.
 * It is used to manage kiosk lifecycle and authentication status.
 *
 * - ACTIVE: The kiosk is fully operational and ready for use.
 * - PENDING_AUTHENTICATION: The kiosk is waiting for authentication to become active.
 * - DISABLED: The kiosk is no longer operational or has been disabled for some reason.
 */
public enum KioskState {
    /**
     * Kiosk is active and fully functional.
     */
    ACTIVE,

    /**
     * Kiosk is awaiting authentication before it can be activated.
     */
    PENDING_AUTHENTICATION,

    /**
     * Kiosk has been disabled and is no longer operational.
     */
    DISABLED
}
