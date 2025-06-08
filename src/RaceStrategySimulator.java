import aerodynamic.*;
import engine.*;
import tyre.*;

public class RaceStrategySimulator {
    private final RaceCar car;
    private final RaceTrack track;

    public RaceStrategySimulator(RaceCar car, RaceTrack track) {
        this.car = car;
        this.track = track;
    }

    public void simulateRace() {
        System.out.println("\n=== Race Strategy Simulation ===");

        System.out.println("\n🚗 Selected Car Setup:");
        System.out.println("Engine: " + car.engine.getName());
        System.out.println("Tyres: " + car.tyre.getType());
        System.out.println("Aero Kit: " + car.aeroKit.getName());
        System.out.printf("Fuel/Battery Capacity: %.1fL\n", car.getFuelTankCapacity());


        double fuelEfficiency = car.calculateEfficiency();
        double fuelNeeded = track.getTotalDistanceKm() / fuelEfficiency;
        int fuelStops = (int) Math.ceil(fuelNeeded / car.getFuelTankCapacity());

        double lapTime = car.calculateLapTime(track.getTrackLengthKm(), track.getTemperatureC());
        int totalLaps = (int) (track.getTotalDistanceKm() / track.getTrackLengthKm());
        double totalRaceTime = lapTime * totalLaps;

        double tyreWearPerLap = car.getTyre().getWearRate();
        int tyreChanges = (int) (totalLaps * tyreWearPerLap);

        System.out.printf("Estimated Lap Time: %.2f minutes\n", lapTime * 60);
        System.out.printf("Estimated Total Race Time: %.2f minutes\n", totalRaceTime * 60);
        System.out.printf("Tyre Changes Estimated: %d\n", tyreChanges);

        // NEW: electric-specific simulation
        if (car.getEngine() instanceof ElectricEngine) {
            ElectricEngine ev = (ElectricEngine) car.getEngine();
            double energyPer100Km = ev.getEnergyConsumption();
            double totalEnergy = (track.getTotalDistanceKm() / 100.0) * energyPer100Km;

            System.out.printf("Energy Consumption: %.2f kWh/100km\n", energyPer100Km);
            System.out.printf("Estimated Energy Needed: %.2f kWh\n", totalEnergy);

            double batteryCapacity = car.getFuelTankCapacity(); // Reuse fuelTankCapacity as batteryCapacity
            int chargingStops = (int) Math.ceil(totalEnergy / batteryCapacity);
            System.out.printf("🔌 Charging Stops Required: %d\n", chargingStops);
        } else {
            System.out.printf("Fuel Efficiency: %.2f km/l\n", fuelEfficiency);
            System.out.printf("Estimated Fuel Needed: %.2f L\n", fuelNeeded);
            System.out.printf("Fuel Stops Required: %d\n", fuelStops);
        }

        // 🧠 Final Strategy Recommendation
        System.out.println("\n🧠 Final Strategy Recommendation:");
        boolean hasRecommendation = false;

       // Dynamic thresholds based on track type
        boolean highWearTrack = track.getNumberOfCurves() > 10 || track.getNumberOfChicanes() > 3;
        boolean fuelDemandingTrack = track.getTotalDistanceKm() > 160 || track.getElevationGain() > 100;

        boolean tyreTempMismatch = !car.getTyre().isTemperatureOptimal(track.getTemperatureC());

        boolean tyreTooSoftForCorners = highWearTrack && car.getTyre().getWearRate() > 0.12;
        //boolean fuelTooLow = fuelDemandingTrack && fuelEfficiency < 6.0;
        // CHANGED: fuelTooLow condition should ignore EVs
        boolean fuelTooLow = fuelDemandingTrack && !(car.getEngine() instanceof ElectricEngine) && fuelEfficiency < 6.0;

        boolean brakesTooWeakInWet = track.isWet() && car.getAeroKit().getBrakeEfficiency() < 0.6;
        boolean turboInWet = track.isWet() && car.getEngine().getName().toLowerCase().contains("turbo");

        RaceCar optimalCar = getRecommendedSetup(track);

        boolean engineChanged = !car.engine.getClass().equals(optimalCar.engine.getClass());
        boolean tyreChanged = !car.tyre.getClass().equals(optimalCar.tyre.getClass());
        boolean aeroChanged = !car.aeroKit.getClass().equals(optimalCar.aeroKit.getClass());
        boolean fuelChanged = Math.abs(car.getFuelTankCapacity() - optimalCar.getFuelTankCapacity()) > 5;



        if (tyreTempMismatch && tyreChanged) {
            System.out.println("- Tyres do not match track temperature range. Consider changing compound.");
            hasRecommendation = true;
        }
        if (fuelTooLow && fuelChanged) {
            System.out.println("- Fuel efficiency is low for this track. Consider optimizing aero or switching to hybrid/electric.");
            hasRecommendation = true;
        }
        if (tyreTooSoftForCorners && tyreChanged) {
            System.out.println("- Tyres may degrade quickly on this twisty circuit. Consider using harder compound.");
            hasRecommendation = true;
        }
        if (brakesTooWeakInWet && aeroChanged) {
            System.out.println("- Wet track and low brake efficiency may reduce control. Consider a Downforce Kit.");
            hasRecommendation = true;
        }
        if (turboInWet && engineChanged) {
            System.out.println("- Turbo engines may be unstable in wet conditions. Consider Hybrid or Electric.");
            hasRecommendation = true;
        }

        boolean isSameSetup = !engineChanged && !tyreChanged && !aeroChanged && !fuelChanged;

        // Warn if component is manually downgraded even if threshold not violated
        if (!isSameSetup) {
            if (engineChanged) {
                System.out.println("- ❗ Engine selection is suboptimal. Recommended: " + optimalCar.engine.getName());
                hasRecommendation = true;
            }
            if (tyreChanged) {
                System.out.println("- ❗ Tyre type differs from the track-optimized compound. Recommended: " + optimalCar.tyre.getType());
                hasRecommendation = true;
            }
            if (aeroChanged) {
                System.out.println("- ❗ Aerodynamic kit choice may not provide the ideal performance. Recommended: " + optimalCar.aeroKit.getName());
                hasRecommendation = true;
            }
            if (fuelChanged) {
                System.out.printf("- ❗ Fuel tank capacity may not meet race distance demands. Recommended: %.1fL\n", optimalCar.getFuelTankCapacity());
                hasRecommendation = true;
            }
        }

        if (!hasRecommendation) {
            if (isSameSetup) {
                System.out.println("✅ Your setup perfectly matches the optimal configuration for this track.");
            } else {
                System.out.println("✅ Your current setup is well-suited for the track conditions.");
            }
        }


        // 🛠️ Recommended Setup for the selected Track (Synced with rules above)
        System.out.println("\n🛠️ Recommended Setup for This Track:");

        // ENGINE
        String engineRec;
        if (turboInWet) {
            engineRec = "Hybrid Engine";
        } else if (track.isWet()) {
            engineRec = "Electric Engine";
        } else if (track.getNumberOfCurves() > 12) {
            engineRec = "Hybrid Engine";
        } else if (track.hasLongStraights()) {
            engineRec = "Turbo Engine";
        } else {
            engineRec = "Standard Engine";
        }

        // TYRE
        String tyreRec;
        if (track.getTemperatureC() < 20) {
            tyreRec = "Soft Tyres";
        } else if (track.getTemperatureC() > 30 || tyreTooSoftForCorners) {
            tyreRec = "Hard Tyres";
        } else {
            tyreRec = "Medium Tyres";
        }

        // AERO KIT
        String aeroRec;
        if (brakesTooWeakInWet) {
            aeroRec = "Downforce Kit";
        } else if (track.getNumberOfCurves() > 12) {
            aeroRec = "Downforce Kit";
        } else if (track.hasLongStraights()) {
            aeroRec = "Low Drag Kit";
        } else {
            aeroRec = "Standard Kit";
        }

        System.out.println("- Engine: " + engineRec);
        System.out.println("- Tyres: " + tyreRec);
        System.out.println("- Aero Kit: " + aeroRec);
    }


    // pre recommended cars setups for tracks
    public static RaceCar getRecommendedSetup(RaceTrack track) {
        // ENGINE
        Engine engine;
        if (track.isWet() && track.getNumberOfCurves() > 10) {
            engine = new ElectricEngine(); // electric for control in twisty wet
        } else if (track.isWet() || track.getTrackLengthKm() < 3.5) {
            engine = new ElectricEngine();
        } else if (track.getNumberOfCurves() > 15) {
            engine = new HybridEngine();
        } else if (track.hasLongStraights()) {
            engine = new TurboEngine();
        } else {
            engine = new StandardEngine();
        }

        // TYRE
        Tyre tyre;
        if (track.getTemperatureC() < 20) {
            tyre = new SoftTyre();
        } else if (track.getTemperatureC() > 30 || track.getNumberOfCurves() > 10) {
            tyre = new HardTyre(); // hot & high wear
        } else {
            tyre = new MediumTyre();
        }

        // AERO
        AerodynamicKit aero;
        if (track.getNumberOfChicanes() > 3 || track.getNumberOfCurves() > 12) {
            aero = new DownforceKit();
        } else if (track.hasLongStraights() && track.getNumberOfCurves() < 6) {
            aero = new LowDragKit();
        } else {
            aero = new StandardKit();
        }

        // FUEL
        double fuelTank;
        if (track.getTotalDistanceKm() > 180) {
            fuelTank = 100;
        } else if (track.getTotalDistanceKm() > 150) {
            fuelTank = 90;
        } else if (track.getTotalDistanceKm() < 120) {
            fuelTank = 70;
        } else {
            fuelTank = 80;
        }

        return new RaceCar(engine, tyre, aero, fuelTank);
    }


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
