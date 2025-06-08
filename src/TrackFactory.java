import java.util.ArrayList;
import java.util.List;

public class TrackFactory {

    public static List<RaceTrack> getPresetTracks() {
        List<RaceTrack> tracks = new ArrayList<>();

        tracks.add(new RaceTrack(
                "Desert Sprint Circuit",      // Track Name
                4.2,                          // trackLengthKm – Short laps favor quick acceleration
                126,                          // totalDistanceKm – 30 laps total (4.2 * 30)
                38.0,                         // temperatureC – Very hot, affects tyre wear
                false,                        // isWet – Dry weather conditions
                6,                            // numberOfCurves – Few medium corners
                1,                            // numberOfChicanes – Almost none, minimal braking
                true,                         // hasLongStraights – Favors high-speed setups
                20                            // elevationGain – Mostly flat
        ));

        tracks.add(new RaceTrack(
                "Mountain Twistway",          // Narrow, high-altitude twist circuit
                5.1,                          // Longer technical track
                180,                          // Total distance = 30 laps
                16.0,                         // Cold, low track temperature
                true,                         // Wet – likely rainy and slippery
                14,                           // Many sharp curves
                5,                            // Multiple chicanes – favors strong braking
                false,                        // No long straights – low drag not useful
                180                           // Significant elevation gain – strains engines
        ));

        tracks.add(new RaceTrack(
                "High-Speed Oval",            // Classic F1-style power track
                7.0,
                210,                          // 30 laps
                28.0,                         // Warm but not extreme
                false,
                3,                            // Very few turns
                0,                            // No chicanes at all
                true,                         // Pure top-speed focus
                0                             // Completely flat
        ));

        tracks.add(new RaceTrack(
                "Urban Street Loop",          // Tight, hot, technical street circuit
                3.6,
                108,                          // Short race (30 laps)
                30.0,                         // Hot ambient temperature
                false,
                12,                           // Many mid-speed corners
                7,                            // Heavy on chicanes – good brakes needed
                false,                        // No long straights
                40                            // Moderate elevation
        ));

        tracks.add(new RaceTrack(
                "Grand Prix Complex",         // Balanced international-style circuit
                5.8,
                174,                          // 30 laps
                24.0,                         // Temperate
                true,                         // Slightly wet, intermediate conditions
                10,                           // A mix of sweeping corners
                3,                            // Few technical sections
                true,                         // Also includes high-speed sectors
                70                            // Mild elevation adds depth
        ));

        return tracks;
    }
}
