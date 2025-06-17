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
 * Test class for RaceStrategySimulator
 * Optimized for maximum coverage with minimal test cases
 * Testing Types: Black Box, White Box, Boundary Value, Equivalence Partitioning, Integration
 */
@DisplayName("Race Strategy Simulator - Complete Algorithm Coverage and Integration Testing")
class RaceStrategySimulatorTest {

    private RaceCar standardCar;
    private RaceCar electricCar;
    private RaceCar heavyCar;
    private RaceTrack dryTrack;
    private RaceTrack wetTrack;

    @BeforeEach
    void setUp() {
        standardCar = new RaceCar(new StandardEngine(), new MediumTyre(), new StandardKit(), 70.0);
        electricCar = new RaceCar(new ElectricEngine(), new SoftTyre(), new GroundEffectKit(), 80.0);
        heavyCar = new RaceCar(new V8Engine(), new SoftTyre(), new ExtremeAeroKit(), 50.0);

        dryTrack = new RaceTrack("Dry Circuit", 4.0, 160, 25.0, false, 8, 2, true, 50);
        wetTrack = new RaceTrack("Wet Circuit", 3.5, 120, 18.0, true, 12, 4, false, 80);
    }

    // WHITE BOX TESTING - Covers ALL engine recommendation logic branches
    @ParameterizedTest
    @DisplayName("Engine recommendation algorithm - all decision paths")
    @CsvSource({
            "true, 15, 4.0, ElectricEngine",     // wet + many curves -> Electric
            "true, 5, 4.0, ElectricEngine",      // wet alone -> Electric
            "false, 20, 4.0, HybridEngine",      // dry + many curves (>15) -> Hybrid
            "false, 5, 4.0, TurboEngine",        // dry + long straights -> Turbo
            "false, 10, 4.0, StandardEngine"     // default case -> Standard
    })
    void testEngineRecommendationAllBranches(boolean isWet, int curves, double trackLength, String expectedEngine) {
        boolean hasLongStraights = curves <= 8;
        RaceTrack track = new RaceTrack("Test", trackLength, 160, 25.0, isWet, curves, 2, hasLongStraights, 50);

        RaceCar recommendation = RaceStrategySimulator.getRecommendedSetup(track);
        assertEquals(expectedEngine, recommendation.getEngine().getClass().getSimpleName());
    }

    // WHITE BOX TESTING - Covers ALL tyre recommendation logic branches
    @ParameterizedTest
    @DisplayName("Tyre recommendation algorithm - all decision paths")
    @CsvSource({
            "15.0, 5, SoftTyre",      // cold temperature (<20) -> Soft
            "35.0, 5, HardTyre",      // hot temperature (>30) -> Hard
            "25.0, 15, HardTyre",     // many curves (>10) -> Hard
            "25.0, 8, MediumTyre"     // default case -> Medium
    })
    void testTyreRecommendationAllBranches(double temperature, int curves, String expectedTyre) {
        RaceTrack track = new RaceTrack("Test", 4.0, 160, temperature, false, curves, 2, false, 50);

        RaceCar recommendation = RaceStrategySimulator.getRecommendedSetup(track);
        assertEquals(expectedTyre, recommendation.getTyre().getClass().getSimpleName());
    }

    // WHITE BOX TESTING - Covers ALL aero recommendation logic branches
    @ParameterizedTest
    @DisplayName("Aero recommendation algorithm - all decision paths")
    @CsvSource({
            "8, 5, false, DownforceKit",     // many chicanes (>3) -> Downforce
            "15, 2, false, DownforceKit",    // many curves (>12) -> Downforce
            "5, 2, true, LowDragKit",        // long straights + few curves (<6) -> Low Drag
            "8, 2, false, StandardKit"       // default case -> Standard
    })
    void testAeroRecommendationAllBranches(int curves, int chicanes, boolean hasLongStraights, String expectedAero) {
        RaceTrack track = new RaceTrack("Test", 4.0, 160, 25.0, false, curves, chicanes, hasLongStraights, 50);

        RaceCar recommendation = RaceStrategySimulator.getRecommendedSetup(track);
        assertEquals(expectedAero, recommendation.getAeroKit().getClass().getSimpleName());
    }

    // WHITE BOX TESTING - Covers ALL fuel capacity logic branches
    @ParameterizedTest
    @DisplayName("Fuel capacity recommendation - all decision paths")
    @CsvSource({
            "200, 100.0",    // long distance (>180km) -> 100L
            "170, 90.0",     // medium-long distance (>150km) -> 90L
            "100, 70.0",     // short distance (<120km) -> 70L
            "140, 80.0"      // default case -> 80L
    })
    void testFuelCapacityAllBranches(int totalDistance, double expectedCapacity) {
        RaceTrack track = new RaceTrack("Test", 4.0, totalDistance, 25.0, false, 8, 2, false, 50);

        RaceCar recommendation = RaceStrategySimulator.getRecommendedSetup(track);
        assertEquals(expectedCapacity, recommendation.getFuelTankCapacity());
    }

    // WHITE BOX TESTING - Covers ALL explainSetupChoice branches
    @ParameterizedTest
    @DisplayName("Setup explanation - all track name branches")
    @CsvSource({
            "Desert Grand Prix, turbo",
            "Mountain Circuit, hybrid",
            "Urban Street Race, electric",
            "Oval Speedway, v8",
            "Monaco Grand Prix, balanced",
            "Random Track Name, balanced"  // default case
    })
    void testExplainSetupChoiceAllBranches(String trackName, String expectedKeyword) {
        RaceTrack track = new RaceTrack(trackName, 4.0, 160, 25.0, false, 8, 2, false, 50);

        String explanation = RaceStrategySimulator.explainSetupChoice(track);
        assertNotNull(explanation);
        assertTrue(explanation.toLowerCase().contains(expectedKeyword.toLowerCase()));
    }

    // INTEGRATION TESTING - Covers electric engine simulation branch
    @Test
    @DisplayName("Race simulation with electric engine - covers electric-specific logic")
    void testSimulateRaceElectricEngine() {
        RaceStrategySimulator simulator = new RaceStrategySimulator(electricCar, dryTrack);

        // Should execute electric-specific code path without exceptions
        assertDoesNotThrow(() -> simulator.simulateRace());
    }

    // INTEGRATION TESTING - Covers fuel engine simulation branch + complex recommendation logic
    @Test
    @DisplayName("Race simulation with fuel engine - covers recommendation warning branches")
    void testSimulateRaceFuelEngineWithComplexScenario() {
        // Track that triggers multiple warning conditions in simulateRace()
        RaceTrack demandingTrack = new RaceTrack("Demanding", 4.0, 200, 35.0, true, 15, 5, false, 150);
        RaceStrategySimulator simulator = new RaceStrategySimulator(heavyCar, demandingTrack);

        // Covers fuel branch, warning conditions, and recommendation comparison logic
        assertDoesNotThrow(() -> simulator.simulateRace());
    }

    // BOUNDARY VALUE TESTING - Tests algorithm consistency and edge cases
    @Test
    @DisplayName("Recommendation consistency for identical tracks")
    void testRecommendationConsistency() {
        RaceTrack track1 = new RaceTrack("Consistency", 4.0, 160, 25.0, false, 8, 2, true, 50);
        RaceTrack track2 = new RaceTrack("Consistency", 4.0, 160, 25.0, false, 8, 2, true, 50);

        RaceCar rec1 = RaceStrategySimulator.getRecommendedSetup(track1);
        RaceCar rec2 = RaceStrategySimulator.getRecommendedSetup(track2);

        // Same input should produce identical recommendations
        assertEquals(rec1.getEngine().getClass(), rec2.getEngine().getClass());
        assertEquals(rec1.getTyre().getClass(), rec2.getTyre().getClass());
        assertEquals(rec1.getAeroKit().getClass(), rec2.getAeroKit().getClass());
        assertEquals(rec1.getFuelTankCapacity(), rec2.getFuelTankCapacity());
    }

    // BOUNDARY VALUE TESTING - Tests extreme track conditions
    @Test
    @DisplayName("Algorithm robustness with extreme track values")
    void testExtremeTrackConditions() {
        RaceTrack extremeTrack = new RaceTrack("Extreme", 0.5, 50, 100.0, true, 100, 50, false, 1000);

        // Should handle extreme values without crashing
        assertDoesNotThrow(() -> {
            RaceCar recommendation = RaceStrategySimulator.getRecommendedSetup(extremeTrack);
            assertNotNull(recommendation.getEngine());
            assertNotNull(recommendation.getTyre());
            assertNotNull(recommendation.getAeroKit());
            assertTrue(recommendation.getFuelTankCapacity() > 0);
        });
    }

    // NEGATIVE TESTING - Covers null input handling branches
    @Test
    @DisplayName("Error handling for invalid inputs")
    void testNullInputHandling() {
        assertThrows(NullPointerException.class, () ->
                new RaceStrategySimulator(null, dryTrack));

        assertThrows(NullPointerException.class, () ->
                new RaceStrategySimulator(standardCar, null));
    }

    // BLACK BOX TESTING - Tests engine recommendation for long straights tracks
    @Test
    @DisplayName("Engine recommendation should favor turbo engines for long straight tracks")
    void testEngineRecommendationForLongStraights() {
        RaceTrack longStraightsTrack = new RaceTrack("Speedway", 4.0, 160, 25.0, false, 8, 2, true, 50);

        RaceCar recommendation = RaceStrategySimulator.getRecommendedSetup(longStraightsTrack);

        // Long straights should recommend speed-optimized engines
        assertEquals("TurboEngine", recommendation.getEngine().getClass().getSimpleName(),
                "Long straights tracks should recommend turbo engines for maximum speed");
    }

    @Test
    @DisplayName("Short track length should recommend electric engine")
    void testShortTrackRecommendation() {
        // Covers the trackLength < 3.5 branch
        RaceTrack shortTrack = new RaceTrack("Short", 3.0, 100, 25.0, false, 8, 2, false, 50);

        RaceCar recommendation = RaceStrategySimulator.getRecommendedSetup(shortTrack);
        assertEquals("ElectricEngine", recommendation.getEngine().getClass().getSimpleName());
    }

    @Test
    @DisplayName("Complex recommendation comparison triggers all warning branches")
    void testComplexRecommendationWarnings() {
        // Car with mismatched components to trigger all warning branches
        RaceCar mismatchedCar = new RaceCar(new TurboEngine(), new SoftTyre(), new LowDragKit(), 40.0);
        RaceTrack wetDemandingTrack = new RaceTrack("Wet Demanding", 4.0, 200, 10.0, true, 18, 6, false, 200);

        RaceStrategySimulator simulator = new RaceStrategySimulator(mismatchedCar, wetDemandingTrack);

        // This should trigger multiple warning conditions in simulateRace()
        assertDoesNotThrow(() -> simulator.simulateRace());
    }

    @Test
    @DisplayName("Simulate race with decent car setup - well-suited message")
    void testSimulateRaceWithDecentCarSetup() {
        // Create a car that's different from optimal but doesn't trigger specific warnings
        RaceTrack moderateTrack = new RaceTrack("Moderate", 4.0, 140, 25.0, false, 6, 1, false, 30);

        // Use components that are reasonable but not optimal
        RaceCar decentCar = new RaceCar(new HybridEngine(), new MediumTyre(), new DownforceKit(), 80.0);

        RaceStrategySimulator simulator = new RaceStrategySimulator(decentCar, moderateTrack);

        // Should trigger the "well-suited for track conditions" branch
        assertDoesNotThrow(() -> simulator.simulateRace());
    }


    // WHITE BOX TESTING - Cover specific condition branches in simulateRace()
    @ParameterizedTest
    @DisplayName("Track difficulty conditions - high wear and fuel demanding scenarios")
    @CsvSource({
            "100, false, false, false",  // Low elevation, dry -> test highWearTrack=false, fuelDemandingTrack=false
            "300, true, true, true",     // High elevation, wet -> test all conditions true
            "50, false, true, false"     // Short distance, dry, few curves -> mixed conditions
    })
    void testTrackDifficultyConditions(int elevation, boolean isWet, boolean expectFuelDemanding, boolean expectHighWear) {
        int curves = expectHighWear ? 15 : 5;
        int distance = expectFuelDemanding ? 180 : 120;

        RaceTrack testTrack = new RaceTrack("Specific", 4.0, distance, 25.0, isWet, curves, 2, false, elevation);
        RaceCar testCar = new RaceCar(new StandardEngine(), new MediumTyre(), new StandardKit(), 70.0);

        RaceStrategySimulator simulator = new RaceStrategySimulator(testCar, testTrack);

        // Should execute different condition branches based on track characteristics
        assertDoesNotThrow(() -> simulator.simulateRace());
    }


}