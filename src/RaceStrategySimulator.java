import aerodynamic.*;
import engine.*;
import tyre.*;
import components.SuspensionSetup;
import components.BrakeCompound;
import components.GearboxRatio;
import components.TractionControlLevel;

public class RaceStrategySimulator {
    private final RaceCar car;
    private final RaceTrack track;

    public RaceStrategySimulator(RaceCar car, RaceTrack track) {
        this.car = car;
        this.track = track;
    }

    public void simulateRace() {
        System.out.println("\n=== Race Strategy Simulation ===");

        // 1️⃣ Selected Car Setup
        System.out.println("\n🚗 Selected Car Setup:");
        System.out.println("- Engine:             " + car.getEngine().getName());
        System.out.println("- Tyres:              " + car.getTyre().getType());
        System.out.println("- Aero Kit:           " + car.getAeroKit().getName());
        System.out.printf("- Fuel Tank:          %.1f L\n", car.getFuelTankCapacity());
        System.out.println("- Suspension:         " + car.getSuspension());
        System.out.println("- Brake Compound:     " + car.getBrakes());
        System.out.println("- Gearbox Ratio:      " + car.getGearbox());
        System.out.println("- Traction Control:   " + car.getTractionControl());

        // 2️⃣ Basic Race Metrics
        double fuelEfficiency = car.calculateFuelEfficiency();
        double totalDistance  = track.getTotalDistanceKm();
        double fuelNeeded     = totalDistance / fuelEfficiency;
        int    fuelStops      = (int) Math.ceil(fuelNeeded / car.getFuelTankCapacity());

        double lapLength      = track.getTrackLengthKm();
        double lapTime        = car.calculateLapTime(lapLength, track.getTemperatureC());
        int    totalLaps      = (int)(totalDistance / lapLength);
        double totalRaceTime  = lapTime * totalLaps;

        double wearRate       = car.getTyre().getWearRate();
        int    tyreChanges    = (int)(totalLaps * wearRate);

        System.out.printf("\nEstimated Lap Time:        %.2f minutes\n", lapTime * 60);
        System.out.printf("Estimated Total Race Time: %.2f minutes\n", totalRaceTime * 60);
        System.out.printf("Fuel Efficiency:           %.2f km/l\n", fuelEfficiency);
        System.out.printf("Estimated Fuel Needed:     %.2f L\n", fuelNeeded);
        System.out.printf("Fuel Stops Required:       %d\n", fuelStops);
        System.out.printf("Tyre Changes Estimated:    %d\n", tyreChanges);

        // 3️⃣ Compare to Optimal Setup
        RaceCar optimal = getRecommendedSetup(track);
        boolean engineChanged = !car.getEngine().getClass().equals(optimal.getEngine().getClass());
        boolean tyreChanged   = !car.getTyre().getClass().equals(optimal.getTyre().getClass());
        boolean aeroChanged   = !car.getAeroKit().getClass().equals(optimal.getAeroKit().getClass());
        boolean fuelChanged   = Math.abs(car.getFuelTankCapacity() - optimal.getFuelTankCapacity()) > 1e-6;
        boolean suspChanged   = !car.getSuspension().equals(optimal.getSuspension());
        boolean brakeChanged  = !car.getBrakes().equals(optimal.getBrakes());
        boolean gearChanged   = !car.getGearbox().equals(optimal.getGearbox());
        boolean tcChanged     = !car.getTractionControl().equals(optimal.getTractionControl());

        if (engineChanged || tyreChanged || aeroChanged || fuelChanged
                || suspChanged || brakeChanged || gearChanged || tcChanged) {
            System.out.println("\n⚠️  Some choices differ from the optimal setup:");
            if (engineChanged) {
                System.out.println("   • Engine:           Recommended → " + optimal.getEngine().getName());
            }
            if (tyreChanged) {
                System.out.println("   • Tyres:            Recommended → " + optimal.getTyre().getType());
            }
            if (aeroChanged) {
                System.out.println("   • Aero Kit:         Recommended → " + optimal.getAeroKit().getName());
            }
            if (fuelChanged) {
                System.out.printf("   • Fuel Tank:        Recommended → %.1f L\n", optimal.getFuelTankCapacity());
            }
            if (suspChanged) {
                System.out.println("   • Suspension:       Recommended → " + optimal.getSuspension());
            }
            if (brakeChanged) {
                System.out.println("   • Brake Compound:   Recommended → " + optimal.getBrakes());
            }
            if (gearChanged) {
                System.out.println("   • Gearbox Ratio:    Recommended → " + optimal.getGearbox());
            }
            if (tcChanged) {
                System.out.println("   • Traction Control: Recommended → " + optimal.getTractionControl());
            }
        } else {
            System.out.println("\n✅ Your configuration matches the optimal setup for this track.");
        }

        // 4️⃣ Final Optimal Setup Summary
        System.out.println("\n🛠️  Optimal Setup for This Track:");
        System.out.println(optimal);
    }

    /**
     * Returns a recommended RaceCar built with sensible defaults for the given track.
     */
    public static RaceCar getRecommendedSetup(RaceTrack track) {
        // Engine
        Engine engine;
        if (track.isWet() && track.getNumberOfCurves() > 10) {
            engine = new ElectricEngine();
        } else if (track.isWet() || track.getTrackLengthKm() < 3.5) {
            engine = new ElectricEngine();
        } else if (track.getNumberOfCurves() > 15) {
            engine = new HybridEngine();
        } else if (track.hasLongStraights()) {
            engine = new TurboEngine();
        } else {
            engine = new StandardEngine();
        }

        // Tyre
        Tyre tyre;
        if (track.getTemperatureC() < 20) {
            tyre = new SoftTyre();
        } else if (track.getTemperatureC() > 30 || track.getNumberOfCurves() > 10) {
            tyre = new HardTyre();
        } else {
            tyre = new MediumTyre();
        }

        // Aero Kit
        AerodynamicKit aero;
        if (track.getNumberOfChicanes() > 3 || track.getNumberOfCurves() > 12) {
            aero = new DownforceKit();
        } else if (track.hasLongStraights() && track.getNumberOfCurves() < 6) {
            aero = new LowDragKit();
        } else {
            aero = new StandardKit();
        }

        // Fuel Tank
        double fuelTank;
        double totalDistance = track.getTotalDistanceKm();
        if (totalDistance > 180) {
            fuelTank = 100;
        } else if (totalDistance > 150) {
            fuelTank = 90;
        } else if (totalDistance < 120) {
            fuelTank = 70;
        } else {
            fuelTank = 80;
        }

        // Suspension
        SuspensionSetup suspension = track.getDifficultyScore() <= 3
                ? SuspensionSetup.HARD_SUSPENSION
                : track.getDifficultyScore() <= 7
                ? SuspensionSetup.MEDIUM_SUSPENSION
                : SuspensionSetup.SOFT_SUSPENSION;

        // Brakes
        BrakeCompound brakes = track.isWet()
                ? BrakeCompound.HIGH_TEMPERATURE
                : BrakeCompound.MEDIUM_TEMPERATURE;

        // Gearbox
        GearboxRatio gearbox = track.hasLongStraights()
                ? GearboxRatio.WIDE_RATIO
                : GearboxRatio.CLOSE_RATIO;

        // Traction Control
        TractionControlLevel tc = track.isWet()
                ? TractionControlLevel.TC_HIGH
                : TractionControlLevel.TC_MEDIUM;

        return new RaceCar(
                engine,
                tyre,
                aero,
                fuelTank,
                suspension,
                brakes,
                gearbox,
                tc
        );
    }

    /**
     * Explains in human‐readable form why the preset was chosen.
     */
    public static String explainSetupChoice(RaceTrack track) {
        String name = track.getName().toLowerCase();
        if (name.contains("desert")) {
            return "🔥 Turbo engine with hard tyres for heat endurance and low drag for long straights.";
        } else if (name.contains("mountain")) {
            return "❄️ Hybrid engine and soft tyres for cold, twisty, wet terrain. Downforce adds grip in curves.";
        } else if (name.contains("oval")) {
            return "💨 V8 engine and low drag kit are perfect for max speed on this flat, high-speed circuit.";
        } else if (name.contains("urban")) {
            return "⚡ Electric engine and ground effect suit tight chicanes and quick direction changes in the heat.";
        } else if (name.contains("grand prix")) {
            return "🧠 Balanced setup. Medium tyres and extreme aero handle both fast and technical sections well.";
        }
        return "Balanced configuration for general racing conditions.";
    }
}
