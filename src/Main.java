import aerodynamic.*;
import engine.*;
import tyre.*;
import components.SuspensionSetup;
import components.BrakeCompound;
import components.GearboxRatio;
import components.TractionControlLevel;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<RaceTrack> tracks = TrackFactory.getPresetTracks();

        while (true) {
            System.out.println("=== Welcome to the Race Strategy Optimiser ===\n");

            // Step 1: Select Track
            System.out.println("Select a Race Track:");
            for (int i = 0; i < tracks.size(); i++) {
                System.out.printf("[%d] %s\n", i + 1, tracks.get(i).getName());
            }
            int trackChoice = getChoice(scanner, "Enter track number: ", 1, tracks.size());
            RaceTrack selectedTrack = tracks.get(trackChoice - 1);
            System.out.printf("Selected: %s (Difficulty: %d/10)\n",
                    selectedTrack.getName(), selectedTrack.getDifficultyScore());

            // Step 2: Select Car
            RaceCar car = buildRaceCar(scanner, selectedTrack);

            // Step 3: Validate Configuration
            if (!ConfigurationValidator.isValid(car, selectedTrack)) {
                System.out.print("\nWould you like to try again? (yes/no): ");
                if (!scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                    System.out.println("Simulation aborted with incompatible setup. Goodbye!");
                    break;
                }
                continue;
            }

            // Step 4: Simulate Race
            RaceStrategySimulator simulator = new RaceStrategySimulator(car, selectedTrack);
            simulator.simulateRace();

            // Step 5: Restart Prompt
            System.out.print("\nSimulate another race? (yes/no): ");
            if (!scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                System.out.println("Thank you for using the Race Strategy Optimiser. Your car setup is finalized! 🏎️ Goodbye! 🏁");
                break;
            }
        }
        scanner.close();
    }

    private static RaceCar buildRaceCar(Scanner scanner, RaceTrack selectedTrack) {
        System.out.println("Choose Configuration Mode:");
        System.out.println("[1] Manual Configuration");
        System.out.println("[2] Use Preset Profile");
        int modeChoice = getChoice(scanner, "Select option: ", 1, 2);

        // Preset Profile
        if (modeChoice == 2) {
            RaceCar recommended = RaceStrategySimulator.getRecommendedSetup(selectedTrack);
            System.out.println("\n🏎️ Recommended Setup for " + selectedTrack.getName() + ":");
            System.out.println("- Engine: " + recommended.getEngine().getName());
            System.out.println("- Tyres: " + recommended.getTyre().getType());
            System.out.println("- Aero Kit: " + recommended.getAeroKit().getName());
            System.out.println("- Fuel Tank: " + recommended.getFuelTankCapacity() + " L");
            System.out.println("- Suspension: " + recommended.getSuspension());
            System.out.println("- Brakes: " + recommended.getBrakes());
            System.out.println("- Gearbox: " + recommended.getGearbox());
            System.out.println("- Traction Control: " + recommended.getTractionControl());

            System.out.println("💡 Why this setup? " + RaceStrategySimulator.explainSetupChoice(selectedTrack));
            System.out.print("\nWould you like to use this setup? (yes/no): ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            if (confirm.equals("yes")) {
                return recommended;
            } else {
                return buildRaceCar(scanner, selectedTrack);
            }
        }

        // Manual Configuration
        System.out.println("\nSelect Engine:");
        System.out.println("[1] Standard\n[2] Turbo\n[3] Hybrid\n[4] V8\n[5] Electric");
        int engineChoice = getChoice(scanner, "Enter engine number: ", 1, 5);
        Engine engine = switch (engineChoice) {
            case 2 -> new TurboEngine();
            case 3 -> new HybridEngine();
            case 4 -> new V8Engine();
            case 5 -> new ElectricEngine();
            default -> new StandardEngine();
        };

        System.out.println("\nSelect Tyre:");
        System.out.println("[1] Soft\n[2] Medium\n[3] Hard");
        int tyreChoice = getChoice(scanner, "Enter tyre number: ", 1, 3);
        Tyre tyre = switch (tyreChoice) {
            case 1 -> new SoftTyre();
            case 2 -> new MediumTyre();
            case 3 -> new HardTyre();
            default -> new MediumTyre();
        };

        System.out.println("\nSelect Aerodynamic Kit:");
        System.out.println("[1] Standard\n[2] Downforce\n[3] Low Drag\n[4] Ground Effect\n[5] Wet Weather\n[6] Extreme");
        int aeroChoice = getChoice(scanner, "Enter kit number: ", 1, 6);
        AerodynamicKit aero = switch (aeroChoice) {
            case 2 -> new DownforceKit();
            case 3 -> new LowDragKit();
            case 4 -> new GroundEffectKit();
            case 5 -> new WetWeatherKit();
            case 6 -> new ExtremeAeroKit();
            default -> new StandardKit();
        };

        System.out.print("\nEnter Fuel Tank Capacity (30–150 L): ");
        double tankCapacity;
        try {
            tankCapacity = Double.parseDouble(scanner.nextLine());
            if (tankCapacity < 30 || tankCapacity > 150) {
                System.out.println("⚠️ Invalid capacity. Using default: 70 L");
                tankCapacity = 70.0;
            }
        } catch (Exception e) {
            System.out.println("⚠️ Invalid input. Using default: 70 L");
            tankCapacity = 70.0;
        }

        System.out.println("\nSelect Suspension Setup:");
        SuspensionSetup suspension = selectOption(scanner, SuspensionSetup.values());

        System.out.println("\nSelect Brake Compound:");
        BrakeCompound brakes = selectOption(scanner, BrakeCompound.values());

        System.out.println("\nSelect Gearbox Ratio:");
        GearboxRatio gearbox = selectOption(scanner, GearboxRatio.values());

        System.out.println("\nSelect Traction Control Level:");
        TractionControlLevel tc = selectOption(scanner, TractionControlLevel.values());

        return new RaceCar(
                engine,
                tyre,
                aero,
                tankCapacity,
                suspension,
                brakes,
                gearbox,
                tc
        );
    }

    // Generic choice helper
    private static int getChoice(Scanner scanner, String prompt, int min, int max) {
        int choice;
        while (true) {
            System.out.print(prompt);
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice >= min && choice <= max) {
                    return choice;
                }
                System.out.printf("Please enter a number between %d and %d.\n", min, max);
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    // Generic enum selection helper
    private static <E extends Enum<E>> E selectOption(Scanner scanner, E[] options) {
        for (int i = 0; i < options.length; i++) {
            System.out.printf("[%d] %s\n", i + 1, options[i]);
        }
        int choice = getChoice(scanner, "Enter option: ", 1, options.length);
        return options[choice - 1];
    }
}
