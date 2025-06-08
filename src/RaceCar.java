import aerodynamic.AerodynamicKit;
import engine.Engine;
import tyre.Tyre;
import components.SuspensionSetup;
import components.BrakeCompound;
import components.GearboxRatio;
import components.TractionControlLevel;

/**
 * Represents a race car configuration including powertrain, tyres,
 * aerodynamic setup, fuel capacity, suspension, brakes, gearbox,
 * and traction control settings.
 */
public class RaceCar {
    public Engine engine;
    public Tyre tyre;
    public AerodynamicKit aeroKit;
    private double fuelTankCapacity;
    private SuspensionSetup suspension;
    private BrakeCompound brakes;
    private GearboxRatio gearbox;
    private TractionControlLevel tractionControl;

    /**
     * Constructs a RaceCar with all configurable components.
     */
    public RaceCar(Engine engine,
                   Tyre tyre,
                   AerodynamicKit aeroKit,
                   double fuelTankCapacity,
                   SuspensionSetup suspension,
                   BrakeCompound brakes,
                   GearboxRatio gearbox,
                   TractionControlLevel tractionControl) {
        this.engine = engine;
        this.tyre = tyre;
        this.aeroKit = aeroKit;
        this.fuelTankCapacity = fuelTankCapacity;
        this.suspension = suspension;
        this.brakes = brakes;
        this.gearbox = gearbox;
        this.tractionControl = tractionControl;
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

    // 🔥 Improved logic: combine AeroKit's fuelEfficiency and Engine's consumption
    public double calculateFuelEfficiency() {
        double baseEfficiency = aeroKit.getFuelEfficiency();
        double enginePenalty = engine.getFuelConsumption();
        double weightPenalty = engine.getWeight() / 300.0;
        return Math.max(1.0, baseEfficiency - enginePenalty - weightPenalty);
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

    public SuspensionSetup getSuspension() {
        return suspension;
    }

    public BrakeCompound getBrakes() {
        return brakes;
    }

    public GearboxRatio getGearbox() {
        return gearbox;
    }

    public TractionControlLevel getTractionControl() {
        return tractionControl;
    }

    @Override
    public String toString() {
        return String.format(
                "Engine: %s%nTyre: %s%nAero Kit: %s%nFuel Tank: %.1f L%n" +
                        "Suspension: %s%nBrakes: %s%nGearbox: %s%nTraction Control: %s",
                engine.getName(),
                tyre.getType(),
                aeroKit.getName(),
                fuelTankCapacity,
                suspension,
                brakes,
                gearbox,
                tractionControl
        );
    }
}
