import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

import aerodynamic.*;
import engine.*;
import tyre.*;

/**
 * Test class for ConfigurationValidator
 * Testing Types: Black Box, White Box, Boundary Value, Equivalence Partitioning
 */
class ConfigurationValidatorTest {

    private RaceTrack dryTrack;
    private RaceTrack wetTrack;
    private RaceTrack longTrack;

    @BeforeEach
    void setUp() {
        dryTrack = new RaceTrack("Test Dry", 4.0, 150, 25.0, false, 8, 2, true, 50);
        wetTrack = new RaceTrack("Test Wet", 3.5, 120, 18.0, true, 12, 4, false, 80);
        longTrack = new RaceTrack("Test Long", 6.0, 200, 30.0, false, 15, 5, true, 120);
    }

    // BLACK BOX TESTING - Testing based on specifications without knowing internal implementation
    @Test
    @DisplayName("Black Box: Valid configuration should return true")
    void testValidConfiguration() {
        RaceCar validCar = new RaceCar(new StandardEngine(), new MediumTyre(), new StandardKit(), 70.0);
        assertTrue(ConfigurationValidator.isValid(validCar, dryTrack));
    }

    // BLACK BOX TESTING - Testing known invalid combination from requirements
    @Test
    @DisplayName("Black Box: Invalid combination - Wet Weather Kit with Hard Tyres")
    void testInvalidWetWeatherKitWithHardTyres() {
        RaceCar invalidCar = new RaceCar(new StandardEngine(), new HardTyre(), new WetWeatherKit(), 70.0);
        assertFalse(ConfigurationValidator.isValid(invalidCar, dryTrack));
    }

    // WHITE BOX TESTING - Testing internal logic paths and conditions
    @ParameterizedTest
    @DisplayName("White Box: Warning conditions should return true but trigger warnings")
    @CsvSource({
            "TurboEngine, SoftTyre, StandardKit, 9.0",      // Turbo + Soft on long track (>8km)
            "ElectricEngine, MediumTyre, ExtremeAeroKit, 5.0", // Electric + Extreme Aero
            "V8Engine, MediumTyre, GroundEffectKit, 5.0"      // Heavy engine (250kg > 230) + Ground Effect
    })
    void testWarningConditionsInternalLogic(String engineType, String tyreType, String aeroType, double trackLength) {
        Engine engine = createEngine(engineType);
        Tyre tyre = createTyre(tyreType);
        AerodynamicKit aero = createAero(aeroType);
        RaceTrack testTrack = new RaceTrack("Test", trackLength, 150, 25.0, false, 8, 2, true, 50);

        RaceCar warningCar = new RaceCar(engine, tyre, aero, 70.0);
        assertTrue(ConfigurationValidator.isValid(warningCar, testTrack));
    }

    // WHITE BOX TESTING - Testing specific condition: Low Drag + Wet Track
    @Test
    @DisplayName("White Box: Low Drag Kit on wet track warning condition")
    void testLowDragKitOnWetTrack() {
        RaceCar car = new RaceCar(new StandardEngine(), new MediumTyre(), new LowDragKit(), 70.0);
        assertTrue(ConfigurationValidator.isValid(car, wetTrack));
    }

    // BOUNDARY VALUE TESTING - Testing edge cases and limits
    @ParameterizedTest
    @DisplayName("Boundary Value: Track length boundary for Turbo + Soft warning (8km threshold)")
    @CsvSource({
            "7.9, true",   // Just under 8km - no warning expected
            "8.0, true",   // Exactly 8km - warning expected
            "8.1, true"    // Just over 8km - warning expected
    })
    void testTrackLengthBoundary(double trackLength, boolean expectedValid) {
        RaceTrack boundaryTrack = new RaceTrack("Boundary", trackLength, 150, 25.0, false, 8, 2, true, 50);
        RaceCar turboCar = new RaceCar(new TurboEngine(), new SoftTyre(), new StandardKit(), 70.0);

        assertEquals(expectedValid, ConfigurationValidator.isValid(turboCar, boundaryTrack));
    }

    // BOUNDARY VALUE TESTING - Testing engine weight boundary (230kg threshold)
    @ParameterizedTest
    @DisplayName("Boundary Value: Engine weight boundary for Ground Effect warning (230kg threshold)")
    @CsvSource({
            "HybridEngine, true",    // 200kg - under threshold
            "TurboEngine, true",     // 220kg - under threshold
            "V8Engine, true"         // 250kg - over threshold (warning expected)
    })
    void testEngineWeightBoundary(String engineType, boolean expectedValid) {
        Engine engine = createEngine(engineType);
        RaceCar car = new RaceCar(engine, new MediumTyre(), new GroundEffectKit(), 70.0);

        assertEquals(expectedValid, ConfigurationValidator.isValid(car, dryTrack));
    }

    // EQUIVALENCE PARTITIONING - Testing different categories of inputs
    @ParameterizedTest
    @DisplayName("Equivalence Partitioning: Different tyre types with Wet Weather Kit")
    @CsvSource({
            "SoftTyre, true",     // Valid combination
            "MediumTyre, true",   // Valid combination
            "HardTyre, false"     // Invalid combination
    })
    void testTyreTypesWithWetWeatherKit(String tyreType, boolean expectedValid) {
        Tyre tyre = createTyre(tyreType);
        RaceCar car = new RaceCar(new StandardEngine(), tyre, new WetWeatherKit(), 70.0);

        assertEquals(expectedValid, ConfigurationValidator.isValid(car, dryTrack));
    }



    // Helper methods for object creation
    private Engine createEngine(String type) {
        return switch (type) {
            case "StandardEngine" -> new StandardEngine();
            case "TurboEngine" -> new TurboEngine();
            case "ElectricEngine" -> new ElectricEngine();
            case "V8Engine" -> new V8Engine();
            case "HybridEngine" -> new HybridEngine();
            default -> new StandardEngine();
        };
    }

    private Tyre createTyre(String type) {
        return switch (type) {
            case "SoftTyre" -> new SoftTyre();
            case "MediumTyre" -> new MediumTyre();
            case "HardTyre" -> new HardTyre();
            default -> new MediumTyre();
        };
    }

    private AerodynamicKit createAero(String type) {
        return switch (type) {
            case "StandardKit" -> new StandardKit();
            case "DownforceKit" -> new DownforceKit();
            case "LowDragKit" -> new LowDragKit();
            case "GroundEffectKit" -> new GroundEffectKit();
            case "WetWeatherKit" -> new WetWeatherKit();
            case "ExtremeAeroKit" -> new ExtremeAeroKit();
            default -> new StandardKit();
        };
    }
}