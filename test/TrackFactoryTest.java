import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

/**
 * Test class for TrackFactory - Minimal testing for simple factory method
 * Testing Types: Black Box, Boundary Value
 */
@DisplayName("Track Factory - Essential Testing")
class TrackFactoryTest {

    // BLACK BOX TESTING - Testing that all 5 preset tracks are created correctly
    @ParameterizedTest
    @DisplayName("All preset predefined tracks should be created with correct properties")
    @CsvSource({
            "0, Desert Sprint Circuit, 4.2, 126, 38.0, false, true",
            "1, Mountain Twistway, 5.1, 180, 16.0, true, false",
            "2, High-Speed Oval, 7.0, 210, 28.0, false, true",
            "3, Urban Street Loop, 3.6, 108, 30.0, false, false",
            "4, Grand Prix Complex, 5.8, 174, 24.0, true, true"
    })
    void testAllPresetTracks(int index, String expectedName, double expectedLength,
                             int expectedDistance, double expectedTemp, boolean expectedWet,
                             boolean expectedStraights) {
        List<RaceTrack> tracks = TrackFactory.getPresetTracks();

        // Verify list has correct size
        assertEquals(5, tracks.size());

        // Verify specific track properties
        RaceTrack track = tracks.get(index);
        assertEquals(expectedName, track.getName());
        assertEquals(expectedLength, track.getTrackLengthKm(), 0.1);
        assertEquals(expectedDistance, track.getTotalDistanceKm());
        assertEquals(expectedTemp, track.getTemperatureC(), 0.1);
        assertEquals(expectedWet, track.isWet());
        assertEquals(expectedStraights, track.hasLongStraights());

        // All tracks should be valid objects
        assertNotNull(track);
        assertTrue(track.getTrackLengthKm() > 0);
        assertTrue(track.getTotalDistanceKm() > 0);
    }


}