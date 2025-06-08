package components;

/**
 * Brake compound options for race cars.
 * Defines temperature performance characteristics for different stint lengths.
 */
public enum BrakeCompound {
    /** Low-temp compound: quick warm-up, suited for short stints */
    LOW_TEMPERATURE,

    /** Medium-temp compound: balanced performance and wear */
    MEDIUM_TEMPERATURE,

    /** High-temp compound: handles high loads for long stints */
    HIGH_TEMPERATURE
}
