package engine;

public class ElectricEngine extends Engine {

    public ElectricEngine() {
        super("Electric Engine", 50.0, 0.0, 2.9, 140); // No fuel, high speed boost
    }

    @Override
    public String getName() {
        return "Electric Engine";
    }

    public double getEnergyConsumption() {
        return 18.0; // Just an example value in kWh per 100km
    }

}
