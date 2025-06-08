package components;

/**
 * Traction control level options for race cars.
 * Defines electronic assistance levels affecting wheel slip and handling.
 */
public enum TractionControlLevel {
    /** No traction control (maximum driver challenge) */
    TC_OFF,

    /** Low traction control (light assistance) */
    TC_LOW,

    /** Medium traction control (balanced assistance) */
    TC_MEDIUM,

    /** High traction control (maximum assistance) */
    TC_HIGH
}
