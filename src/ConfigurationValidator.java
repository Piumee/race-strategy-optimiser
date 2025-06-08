
public class ConfigurationValidator {
    public static boolean isValid(RaceCar car, RaceTrack track) {
        String tyreType = car.getTyre().getType().toLowerCase();
        String aeroName = car.getAeroKit().getName().toLowerCase();
        String engineName = car.getEngine().getName().toLowerCase();
        double engineWeight = car.getEngine().getWeight();
        double trackLength = track.getTrackLengthKm();

        boolean valid = true;

        if (aeroName.contains("wet") && tyreType.contains("hard")) {
            System.out.println("❌ Invalid Configuration: Wet Weather Kit cannot be used with Hard Tyres.");
            return false;
        }

        if (engineName.contains("turbo") && tyreType.contains("soft") && trackLength > 8) {
            System.out.println("⚠️ Warning: Soft Tyres may degrade quickly with Turbo src.engine.Engine on a long track.");
        }

        if (aeroName.contains("extreme") && engineName.contains("electric")) {
            System.out.println("⚠️ Warning: Extreme Aero Kit may significantly reduce electric performance.");
        }

        if (aeroName.contains("ground") && engineWeight > 230) {
            System.out.println("⚠️ Warning: Heavy engine may limit effectiveness of Ground Effect Kit.");
        }

        if (aeroName.contains("low") && track.isWet()) {
            System.out.println("⚠️ Warning: Low Drag Aero Kit is not recommended on wet tracks.");
        }

        return valid;
    }
}