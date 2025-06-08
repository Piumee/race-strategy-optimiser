
public class RaceTrack {
    private String name;

    // Length of a single lap in kilometers
    private double trackLengthKm;

    // Total race distance in kilometers (used to calculate total laps)
    private int totalDistanceKm;

    // Ambient temperature at the race (in Celsius), affects tyre wear
    private double temperatureC;

    // Indicates whether the track is wet (rain conditions)
    private boolean isWet;

    // Number of sharp or medium-speed corners, increases cornering demand and tyre wear
    private int numberOfCurves;

    // Number of tight double-apex or S-bend chicanes, affects braking and grip requirements
    private int numberOfChicanes;

    // True if the track has long straights (favors low-drag, high top-speed builds)
    private boolean hasLongStraights;

    // Total elevation gain in meters (impacts acceleration/braking performance)
    private int elevationGain;

    public RaceTrack(String name, double trackLengthKm, int totalDistanceKm, double temperatureC,
                     boolean isWet, int numberOfCurves, int numberOfChicanes,
                     boolean hasLongStraights, int elevationGain) {
        this.name = name;
        this.trackLengthKm = trackLengthKm;
        this.totalDistanceKm = totalDistanceKm;
        this.temperatureC = temperatureC;
        this.isWet = isWet;
        this.numberOfCurves = numberOfCurves;
        this.numberOfChicanes = numberOfChicanes;
        this.hasLongStraights = hasLongStraights;
        this.elevationGain = elevationGain;
    }

    // Getters
    public String getName() { return name; }
    public double getTrackLengthKm() { return trackLengthKm; }
    public int getTotalDistanceKm() { return totalDistanceKm; }
    public double getTemperatureC() { return temperatureC; }
    public boolean isWet() { return isWet; }
    public int getNumberOfCurves() { return numberOfCurves; }
    public int getNumberOfChicanes() { return numberOfChicanes; }
    public boolean hasLongStraights() { return hasLongStraights; }
    public int getElevationGain() { return elevationGain; }

    public int getDifficultyScore() {
        int score = 0;

        // Elevation: +1 per 75m, max 3
        score += Math.min(3, elevationGain / 75);

        // Curves: +1 per 5 curves, max 3
        score += Math.min(3, numberOfCurves / 5);

        // Chicanes: +1 if >= 3
        if (numberOfChicanes >= 3) score++;

        // Wet track: +2 if wet
        if (isWet) score += 2;

        return Math.min(score, 10); // Ensure it caps at 10
    }
}
