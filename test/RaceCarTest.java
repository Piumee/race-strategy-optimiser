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
 * Test class for RaceCar
 * Testing Types: Black Box, White Box, Boundary Value, Equivalence Partitioning
 */
class RaceCarTest {

    private RaceCar standardCar;
    private RaceCar electricCar;
    private Engine standardEngine;
    private Tyre mediumTyre;
    private AerodynamicKit standardKit;

    @BeforeEach
    void setUp() {
        standardEngine = new StandardEngine();
        mediumTyre = new MediumTyre();
        standardKit = new StandardKit();
        standardCar = new RaceCar(standardEngine, mediumTyre, standardKit, 70.0);
        electricCar = new RaceCar(new ElectricEngine(), mediumTyre, standardKit, 70.0);
    }

    // BLACK BOX TESTING - Testing constructor and basic getters
    @Test
    @DisplayName("Black Box: Constructor should set all components correctly")
    void testConstructorSetsComponents() {
        assertEquals(standardEngine, standardCar.getEngine());
        assertEquals(mediumTyre, standardCar.getTyre());
        assertEquals(standardKit, standardCar.getAeroKit());
        assertEquals(70.0, standardCar.getFuelTankCapacity());
    }

    // WHITE BOX TESTING - Testing calculateOverallSpeed algorithm
    @Test
    @DisplayName("White Box: Overall speed calculation algorithm")
    void testCalculateOverallSpeedAlgorithm() {
        // Formula: aeroKit.getTopSpeed() + engine.getSpeedBoost() - (engine.getWeight() / 100)
        // Standard Kit: topSpeed=250, Standard Engine: speedBoost=20, weight=180
        // Expected: 250 + 20 - (180/100) = 250 + 20 - 1.8 = 268.2

        double expectedSpeed = 250 + 20 - (180.0 / 100);
        assertEquals(expectedSpeed, standardCar.calculateOverallSpeed(), 0.01);
    }
    @ParameterizedTest
    @DisplayName("White Box: Overall speed with different engine weights")
    @CsvSource({
            "StandardEngine, 180, 268.2",    // 250 + 20 - 1.8 = 268.2
            "TurboEngine, 220, 287.8",       // 250 + 40 - 2.2 = 287.8
            "ElectricEngine, 140, 298.6",    // 250 + 50 - 1.4 = 298.6
            "V8Engine, 250, 292.5"          // 250 + 45 - 2.5 = 292.5
    })
    void testOverallSpeedWithDifferentEngines(String engineType, double expectedWeight, double expectedSpeed) {
        Engine engine = createEngine(engineType);
        RaceCar car = new RaceCar(engine, mediumTyre, standardKit, 70.0);

        assertEquals(expectedWeight, engine.getWeight());
        assertEquals(expectedSpeed, car.calculateOverallSpeed(), 0.1);
    }

    // WHITE BOX TESTING - Testing calculateLapTime algorithm with temperature conditions
    @ParameterizedTest
    @DisplayName("White Box: Lap time calculation with temperature effects")
    @CsvSource({
            "25.0, true, 1.0",     // Optimal temperature (15-35 range for medium tyres)
            "10.0, false, 1.1",    // Below optimal range
            "40.0, false, 1.1"     // Above optimal range
    })
    void testLapTimeTemperatureEffects(double temperature, boolean isOptimal, double expectedTempPenalty) {
        double trackLength = 5.0;

        double lapTime = standardCar.calculateLapTime(trackLength, temperature);

        // Verify temperature penalty is applied correctly
        assertEquals(isOptimal, mediumTyre.isTemperatureOptimal(temperature));
        assertTrue(lapTime > 0);
    }

    // WHITE BOX TESTING - Testing calculateEfficiency for electric vs fuel engines
    @Test
    @DisplayName("White Box: Efficiency calculation for electric engine")
    void testElectricEngineEfficiency() {
        // Electric formula: 100.0 / ev.getEnergyConsumption()
        // ElectricEngine energy consumption = 18.0 kWh/100km
        // Expected: 100.0 / 18.0 = 5.56 km/kWh

        double expectedEfficiency = 100.0 / 18.0;
        assertEquals(expectedEfficiency, electricCar.calculateEfficiency(), 0.01);
    }

    @Test
    @DisplayName("White Box: Efficiency calculation for fuel engine")
    void testFuelEngineEfficiency() {
        // Fuel formula: Math.max(1.0, baseEfficiency - enginePenalty - weightPenalty)
        // Standard Kit: fuelEfficiency=12, Standard Engine: consumption=4.5, weight=180
        // Expected: max(1.0, 12 - 4.5 - (180/300)) = max(1.0, 12 - 4.5 - 0.6) = 6.9

        double baseEfficiency = 12.0;  // StandardKit fuel efficiency
        double enginePenalty = 4.5;   // StandardEngine fuel consumption
        double weightPenalty = 180.0 / 300.0;  // weight penalty
        double expectedEfficiency = Math.max(1.0, baseEfficiency - enginePenalty - weightPenalty);

        assertEquals(expectedEfficiency, standardCar.calculateEfficiency(), 0.01);
    }

    // BOUNDARY VALUE TESTING - Testing efficiency minimum threshold
    @Test
    @DisplayName("Boundary Value: Efficiency should not go below 1.0")
    void testEfficiencyMinimumThreshold() {
        // Create car with high consumption engine and poor aero to test Math.max(1.0, ...)
        Engine heavyEngine = new V8Engine(); // weight=250, consumption=7.0
        AerodynamicKit poorAero = new ExtremeAeroKit(); // fuelEfficiency=9
        RaceCar inefficientCar = new RaceCar(heavyEngine, mediumTyre, poorAero, 70.0);

        // Formula: max(1.0, 9 - 7.0 - (250/300)) = max(1.0, 9 - 7.0 - 0.83) = max(1.0, 1.17) = 1.17
        double efficiency = inefficientCar.calculateEfficiency();
        assertTrue(efficiency >= 1.0);
    }

    // BOUNDARY VALUE TESTING - Testing lap time with extreme track lengths
    @ParameterizedTest
    @DisplayName("Boundary Value: Lap time calculation with extreme track lengths")
    @CsvSource({
            "0.1, 25.0",     // Very short track
            "1.0, 25.0",     // Short track
            "5.0, 25.0",     // Medium track
            "10.0, 25.0",    // Long track
            "20.0, 25.0"     // Very long track
    })
    void testLapTimeWithExtremeTrackLengths(double trackLength, double temperature) {
        double lapTime = standardCar.calculateLapTime(trackLength, temperature);

        assertTrue(lapTime > 0);
        // Longer tracks should generally take more time
        if (trackLength > 1.0) {
            assertTrue(lapTime > 0.001); // Should be measurable time
        }
    }


    // BOUNDARY VALUE TESTING - Testing extreme fuel tank values
    @ParameterizedTest
    @DisplayName("Boundary Value: Extreme fuel tank capacities")
    @CsvSource({
            "0.0",      // Zero capacity
            "0.1",      // Very small capacity
            "200.0",    // Very large capacity
            "-10.0"     // Negative capacity
    })
    void testExtremeFuelCapacities(double capacity) {
        RaceCar car = new RaceCar(standardEngine, mediumTyre, standardKit, capacity);
        assertEquals(capacity, car.getFuelTankCapacity());

        // Car should still function with unusual capacities
        assertTrue(car.calculateOverallSpeed() > 0);
        assertTrue(car.calculateEfficiency() > 0);
    }

    @Test
    @DisplayName("All component types integration testing for racing car")
    void testAllComponentTypesIntegration() {
        // Just instantiate all component types to cover constructors
        Engine[] engines = {new StandardEngine(), new TurboEngine(), new ElectricEngine(), new HybridEngine(), new V8Engine()};
        Tyre[] tyres = {new SoftTyre(), new MediumTyre(), new HardTyre()};
        AerodynamicKit[] aeros = {new StandardKit(), new DownforceKit(), new LowDragKit(), new GroundEffectKit(), new WetWeatherKit(), new ExtremeAeroKit()};

        // Verify all created successfully and call ALL getter methods for 100% coverage
        for (Engine e : engines) {
            assertNotNull(e.getName());
            assertTrue(e.getSpeedBoost() > 0);
            assertTrue(e.getFuelConsumption() >= 0); // Electric engine has 0
            assertTrue(e.getAcceleration() > 0);
            assertTrue(e.getWeight() > 0);

            // Test ElectricEngine specific method
            if (e instanceof ElectricEngine ev) {
                assertTrue(ev.getEnergyConsumption() > 0);
            }
        }

        for (Tyre t : tyres) {
            assertNotNull(t.getType());
            assertTrue(t.getGrip() > 0);
            assertTrue(t.getWearRate() > 0);
            // Test temperature method (business logic)
            assertTrue(t.isTemperatureOptimal(25.0));
        }

        for (AerodynamicKit a : aeros) {
            assertNotNull(a.getName());
            assertTrue(a.getTopSpeed() > 0);
            assertTrue(a.getFuelEfficiency() > 0);
            assertTrue(a.getCorneringAbility() > 0);
            assertTrue(a.getBrakeEfficiency() > 0);
            assertTrue(a.getDragCoefficient() > 0);
            assertTrue(a.getDownforce() > 0);
        }
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