package engine;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Type: Black-Box Testing
 * Purpose: Validate that each Engine subclass returns expected static values
 */
@DisplayName("Engine Subclass Tests - Black Box Testing")
class EngineTest {

    @ParameterizedTest(name = "{0} -> Boost={2}, Fuel={3}, Accel={4}, Weight={5}")
    @CsvSource({
            "Standard, Standard Engine,   20.0, 4.5, 4.0, 180",
            "Turbo,    Turbocharged Engine,      40.0, 6.5, 3.2, 220",
            "Hybrid,   Hybrid Engine,     35.0, 4.0, 3.5, 200",
            "V8,       V8 Engine,         45.0, 7.0, 3.8, 250"
    })
    @DisplayName("Validate Internal Combustion Engines (with getName)")
    void testEngineAttributes(String type, String expectedName, double expectedBoost, double expectedFuel, double expectedAccel, int expectedWeight) {
        Engine engine = switch (type) {
            case "Standard" -> new StandardEngine();
            case "Turbo"    -> new TurboEngine();
            case "Hybrid"   -> new HybridEngine();
            case "V8"       -> new V8Engine();
            default         -> throw new IllegalArgumentException("Unknown engine: " + type);
        };

        assertAll(type + " Engine Tests",
                () -> assertEquals(expectedName, engine.getName()),
                () -> assertEquals(expectedBoost, engine.getSpeedBoost()),
                () -> assertEquals(expectedFuel, engine.getFuelConsumption()),
                () -> assertEquals(expectedAccel, engine.getAcceleration()),
                () -> assertEquals(expectedWeight, engine.getWeight())
        );
    }

    @Test
    @DisplayName("Validate Electric Engine Attributes")
    void testElectricEngine() {
        ElectricEngine ev = new ElectricEngine();

        assertAll("ElectricEngine",
                () -> assertEquals("Electric Engine", ev.getName()),
                () -> assertEquals(50.0, ev.getSpeedBoost()),
                () -> assertEquals(0.0, ev.getFuelConsumption()), // for compatibility
                () -> assertEquals(2.9, ev.getAcceleration()),
                () -> assertEquals(140.0, ev.getWeight()),
                () -> assertEquals(18.0, ev.getEnergyConsumption())
        );
    }
}
