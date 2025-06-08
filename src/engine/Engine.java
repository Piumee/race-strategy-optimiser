package engine;

public abstract class Engine {
    protected String name;
    protected double speedBoost;
    protected double fuelConsumption;
    protected double acceleration;
    protected double weight;

    public Engine(String name, double speedBoost, double fuelConsumption, double acceleration, double weight) {
        this.name = name;
        this.speedBoost = speedBoost;
        this.fuelConsumption = fuelConsumption;
        this.acceleration = acceleration;
        this.weight = weight;
    }

    public String getName() { return name; }
    public double getSpeedBoost() { return speedBoost; }
    public double getFuelConsumption() { return fuelConsumption; }
    public double getAcceleration() { return acceleration; }
    public double getWeight() { return weight; }
}