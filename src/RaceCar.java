import aerodynamic.AerodynamicKit;
import engine.ElectricEngine;
import engine.Engine;
import tyre.Tyre;


public class RaceCar {
    public Engine engine;
    public Tyre tyre;
    public AerodynamicKit aeroKit;
    private double fuelTankCapacity; // or battery capacity (if electric)

    public RaceCar(Engine engine, Tyre tyre, AerodynamicKit aeroKit, double fuelTankCapacity) {
        this.engine = engine;
        this.tyre = tyre;
        this.aeroKit = aeroKit;
        this.fuelTankCapacity = fuelTankCapacity;
    }

    // Overall top-end speed estimate with aero and engine combined
    public double calculateOverallSpeed() {
        return aeroKit.getTopSpeed() + engine.getSpeedBoost() - (engine.getWeight() / 100);
    }

    // Lap time calculation based on temp fit, aero cornering, brake impact
    public double calculateLapTime(double trackLength, double temperature) {
        double tempPenalty = tyre.isTemperatureOptimal(temperature) ? 1.0 : 1.1;
        double corneringModifier = 1.0 - (aeroKit.getCorneringAbility() / 20.0);
        double brakeModifier = 1.0 - aeroKit.getBrakeEfficiency();
        double baseTime = trackLength / calculateOverallSpeed();
        return baseTime * tempPenalty * corneringModifier * brakeModifier;
    }

    // 🔥 Improved logic: combine AeroKit's fuelEfficiency and Engine's consumption and added EnergyConsumption
    public double calculateEfficiency() {
        if (engine instanceof ElectricEngine ev) {
            return 100.0 / ev.getEnergyConsumption(); // km per kWh
        } else {
            double baseEfficiency = aeroKit.getFuelEfficiency();
            double enginePenalty = engine.getFuelConsumption();
            double weightPenalty = engine.getWeight() / 300.0;
            return Math.max(1.0, baseEfficiency - enginePenalty - weightPenalty); // km/l
        }
    }

    public Engine getEngine() {
        return engine;
    }

    public Tyre getTyre() {
        return tyre;
    }

    public AerodynamicKit getAeroKit() {
        return aeroKit;
    }

    public double getFuelTankCapacity() {
        return fuelTankCapacity;
    }
}
