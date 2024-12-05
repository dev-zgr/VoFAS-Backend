package com.backend.vofasbackend.datalayer.enums;

/**
 * Enum representing the operational states of a kiosk.
 * It is used to track the lifecycle and authentication status of a kiosk.
 */
public enum KioskStateEnum {

    /**
     * Kiosk is operational and ready for use.
     */
    ACTIVE,

    /**
     * Kiosk is awaiting authentication to become operational.
     */
    AUTHENTICATION_PENDING,

    /**
     * Kiosk is disabled and not operational.
     */
    DISABLED
}

