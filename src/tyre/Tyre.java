package tyre;

public abstract class Tyre {
    protected String type;
    protected double wearRate;
    protected double grip;
    protected double minOptimalTemp;
    protected double maxOptimalTemp;

    public Tyre(String type, double wearRate, double grip, double minOptimalTemp, double maxOptimalTemp) {
        this.type = type;
        this.wearRate = wearRate;
        this.grip = grip;
        this.minOptimalTemp = minOptimalTemp;
        this.maxOptimalTemp = maxOptimalTemp;
    }

    public String getType() { return type; }
    public double getWearRate() { return wearRate; }
    public double getGrip() { return grip; }

    public boolean isTemperatureOptimal(double temperature) {
        return temperature >= minOptimalTemp && temperature <= maxOptimalTemp;
    }
}