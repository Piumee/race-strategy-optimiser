package aerodynamic;

public abstract class AerodynamicKit {
    protected String name;
    protected double dragCoefficient;
    protected double downforce;
    protected double topSpeed;
    protected double fuelEfficiency;
    protected int corneringAbility;
    protected double brakeEfficiency;

    public AerodynamicKit(String name, double dragCoefficient, double downforce, double topSpeed,
                          double fuelEfficiency, int corneringAbility, double brakeEfficiency) {
        this.name = name;
        this.dragCoefficient = dragCoefficient;
        this.downforce = downforce;
        this.topSpeed = topSpeed;
        this.fuelEfficiency = fuelEfficiency;
        this.corneringAbility = corneringAbility;
        this.brakeEfficiency = brakeEfficiency;
    }

    public String getName() { return name; }
    public double getDragCoefficient() { return dragCoefficient; }
    public double getDownforce() { return downforce; }
    public double getTopSpeed() { return topSpeed; }
    public double getFuelEfficiency() { return fuelEfficiency; }
    public int getCorneringAbility() { return corneringAbility; }
    public double getBrakeEfficiency() { return brakeEfficiency; }
}
