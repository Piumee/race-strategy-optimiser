package components;

/**
 * Suspension setup options for race cars.
 * Defines stiffness levels affecting handling and bump compliance.
 */
public enum SuspensionSetup {
    /** Softer suspension: better over bumps, slower weight transfer */
    SOFT_SUSPENSION,

    /** Balanced suspension */
    MEDIUM_SUSPENSION,

    /** Stiffer suspension: sharper responses, less compliance */
    HARD_SUSPENSION
}
