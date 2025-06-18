import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for RaceTrack
 * Testing Types: Black Box, White Box, Boundary Value, Equivalence Partitioning
 */
@DisplayName("Comprehensive RaceTrack Class Tests")
class RaceTrackTest {

    private RaceTrack basicTrack;

    @BeforeEach
    void setUp() {
        basicTrack = new RaceTrack("Test Track", 4.0, 160, 25.0, false, 10, 2, true, 100);
    }

    // BLACK BOX TESTING - Testing constructor and getter functionality
    @Test
    @DisplayName("Constructor should set all properties correctly")
    void testConstructorSetsProperties() {
        assertEquals("Test Track", basicTrack.getName());
        assertEquals(4.0, basicTrack.getTrackLengthKm());
        assertEquals(160, basicTrack.getTotalDistanceKm());
        assertEquals(25.0, basicTrack.getTemperatureC());
        assertFalse(basicTrack.isWet());
        assertEquals(10, basicTrack.getNumberOfCurves());
        assertEquals(2, basicTrack.getNumberOfChicanes());
        assertTrue(basicTrack.hasLongStraights());
        assertEquals(100, basicTrack.getElevationGain());
    }

    // WHITE BOX TESTING - Testing internal difficulty calculation algorithm
    @ParameterizedTest
    @DisplayName("Difficulty score calculation algorithm test")
    @CsvSource({
            "0, 0, 0, false, 0",     // Minimum difficulty (all components = 0)
            "225, 15, 5, true, 10",  // Maximum difficulty (should cap at 10)
            "75, 5, 3, false, 3",    // Medium: elevation(1) + curves(1) + chicanes(1) = 3
            "150, 10, 1, true, 6",   // elevation(2) + curves(2) + chicanes(0) + wet(2) = 6
            "300, 25, 8, true, 10"   // Over-maximum (should cap at 10)
    })
    void testDifficultyScoreCalculationAlgorithm(int elevation, int curves, int chicanes, boolean isWet, int expectedScore) {
        RaceTrack track = new RaceTrack("Test", 4.0, 160, 25.0, isWet, curves, chicanes, false, elevation);
        assertEquals(expectedScore, track.getDifficultyScore());
    }

    // WHITE BOX TESTING - Testing individual components of difficulty algorithm
    @Test
    @DisplayName("Individual difficulty components calculation")
    void testDifficultyAlgorithmComponents() {
        // Test elevation component: score += Math.min(3, elevationGain / 75)
        RaceTrack elevationTrack = new RaceTrack("Elevation", 4.0, 160, 25.0, false, 0, 0, false, 150);
        assertEquals(2, elevationTrack.getDifficultyScore()); // 150/75 = 2

        // Test curves component: score += Math.min(3, numberOfCurves / 5)
        RaceTrack curvesTrack = new RaceTrack("Curves", 4.0, 160, 25.0, false, 10, 0, false, 0);
        assertEquals(2, curvesTrack.getDifficultyScore()); // 10/5 = 2

        // Test chicanes component: if (numberOfChicanes >= 3) score++
        RaceTrack chicanesTrack = new RaceTrack("Chicanes", 4.0, 160, 25.0, false, 0, 3, false, 0);
        assertEquals(1, chicanesTrack.getDifficultyScore()); // 3 chicanes = 1 point

        // Test wet condition: if (isWet) score += 2
        RaceTrack wetTrack = new RaceTrack("Wet", 4.0, 160, 25.0, true, 0, 0, false, 0);
        assertEquals(2, wetTrack.getDifficultyScore()); // wet = 2 points
    }



    // BOUNDARY VALUE TESTING - Testing difficulty score cap at 10
    @Test
    @DisplayName("Maximum difficulty score should cap at 10")
    void testMaximumDifficultyScoreCap() {
        // Create track that would exceed 10 without cap: elevation(3) + curves(3) + chicanes(1) + wet(2) = 9
        // Adding more should still cap at 10
        RaceTrack extremeTrack = new RaceTrack("Extreme", 4.0, 160, 25.0, true, 100, 50, false, 1000);
        assertEquals(10, extremeTrack.getDifficultyScore());
    }

    // EQUIVALENCE PARTITIONING - Testing different categories of track properties
    @ParameterizedTest
    @DisplayName("Equivalence Partitioning: Track length categories")
    @CsvSource({
            "0.1, 1",      // Very short track
            "3.5, 150",    // Short track
            "5.0, 200",    // Medium track
            "8.0, 350",    // Long track
            "12.0, 500"    // Very long track
    })
    void testTrackLengthCategories(double trackLength, int totalDistance) {
        RaceTrack track = new RaceTrack("Length Test", trackLength, totalDistance, 25.0, false, 5, 1, false, 50);

        assertEquals(trackLength, track.getTrackLengthKm());
        assertEquals(totalDistance, track.getTotalDistanceKm());
        assertTrue(track.getDifficultyScore() >= 0);
    }

    @ParameterizedTest
    @DisplayName("Equivalence Partitioning: Temperature categories")
    @CsvSource({
            "-10.0",    // Very cold
            "5.0",      // Cold
            "25.0",     // Moderate
            "40.0",     // Hot
            "60.0"      // Very hot
    })
    void testTemperatureCategories(double temperature) {
        RaceTrack track = new RaceTrack("Temp Test", 4.0, 160, temperature, false, 5, 1, false, 50);
        assertEquals(temperature, track.getTemperatureC());
    }


    // NEGATIVE TESTING - Testing with unusual or edge case inputs
    @Test
    @DisplayName("Negative Testing: Negative values should be handled")
    void testNegativeValues() {
        RaceTrack negativeTrack = new RaceTrack("Negative", 4.0, 160, -20.0, false, -5, -2, false, -100);

        // Constructor should accept negative values
        assertEquals(-20.0, negativeTrack.getTemperatureC());
        assertEquals(-5, negativeTrack.getNumberOfCurves());
        assertEquals(-2, negativeTrack.getNumberOfChicanes());
        assertEquals(-100, negativeTrack.getElevationGain());

        // Difficulty score should handle negatives gracefully
        int score = negativeTrack.getDifficultyScore();
        assertTrue(score >= 0 && score <= 10); // it fails as it allow negative values to calculation and generate difficulty level outside the 0-10 range
    }


    @Test
    @DisplayName("Negative Testing: Empty track name should be handled")
    void testEmptyTrackName() {
        RaceTrack emptyNameTrack = new RaceTrack("", 4.0, 160, 25.0, false, 5, 1, false, 50);
        assertEquals("", emptyNameTrack.getName());
    }

    // BLACK BOX TESTING - Testing boolean properties
    @Test
    @DisplayName("Black Box: Boolean properties should work correctly")
    void testBooleanProperties() {
        RaceTrack wetTrack = new RaceTrack("Wet Test", 4.0, 160, 15.0, true, 5, 1, false, 50);
        RaceTrack dryTrack = new RaceTrack("Dry Test", 4.0, 160, 25.0, false, 5, 1, true, 50);

        assertTrue(wetTrack.isWet());
        assertFalse(wetTrack.hasLongStraights());

        assertFalse(dryTrack.isWet());
        assertTrue(dryTrack.hasLongStraights());
    }
}