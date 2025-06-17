package tyre;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Type: Black-Box Testing
 * Purpose: Validate that each Tyre subclass returns the correct temperature range and wear rate
 */
@DisplayName("Tyre Subclass Tests - Black Box Testing")
class TyreTest {

    @ParameterizedTest(name = "{0} Tyre -> grip={1}, wearRate={2}")
    @CsvSource({
            "Soft, 0.9, 0.25",
            "Medium, 0.80, 0.15",
            "Hard, 0.6, 0.1"
    })
    @DisplayName("Validate type, grip, and wearRate")
    void testTyreAttributes(String type, double expectedGrip, double expectedWear) {
        Tyre tyre = switch (type) {
            case "Soft"   -> new SoftTyre();
            case "Medium" -> new MediumTyre();
            case "Hard"   -> new HardTyre();
            default       -> throw new IllegalArgumentException("Unknown tyre: " + type);
        };

        assertAll(type + " Tyre Tests",
                () -> assertEquals(type, tyre.getType(), "Type should match"),
                () -> assertEquals(expectedGrip, tyre.getGrip(), "Grip should match"),
                () -> assertEquals(expectedWear, tyre.getWearRate(), "Wear rate should match")
        );
    }


    /**
     * Boundary value testing
     * Purpose: Verify temperature range logic by directly exercising min/max boundaries
     */
    @DisplayName("All Tyres – Testing for isTemperatureOptimal() for all boundary values")
    @ParameterizedTest(name = "{0} Tyre at {1}°C => optimal? {2}")
    @CsvSource({
            "Soft, 20, true", "Soft, 30, true", "Soft, 19.9, false", "Soft, 30.1, false",
            "Medium, 15, true", "Medium, 35, true", "Medium, 14.9, false", "Medium, 35.1, false",
            "Hard, 10, true", "Hard, 40, true", "Hard, 9.9, false", "Hard, 40.1, false"
    })
    void testAllTyreTemperatures(String type, double temperature, boolean expected) {
        Tyre tyre = switch (type) {
            case "Soft"   -> new SoftTyre();
            case "Medium" -> new MediumTyre();
            case "Hard"   -> new HardTyre();
            default       -> throw new IllegalArgumentException("Unknown tyre: " + type);
        };

        assertEquals(expected, tyre.isTemperatureOptimal(temperature),
                () -> type + " Tyre failed at " + temperature + "°C");
    }
}
