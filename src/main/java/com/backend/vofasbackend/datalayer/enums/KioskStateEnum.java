package com.backend.vofasbackend.datalayer.enums;

/**
 * Enum representing the various states of a kiosk.
 * This enum tracks the current operational state of a kiosk.
 * It is used to manage kiosk lifecycle and authentication status.
 *
 * <p>The endpoints in this controller allow for actions such as:</p>
 * <ul>
 *     <li>ACTIVE: The kiosk is fully operational and ready for use.</li>
 *      <li>PENDING_AUTHENTICATION: The kiosk is waiting for authentication to become active.</li>
 *      <li>DISABLED: The kiosk is no longer operational or has been disabled for some reason.</li>
 *  </ul>
 */
public enum KioskStateEnum {
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
