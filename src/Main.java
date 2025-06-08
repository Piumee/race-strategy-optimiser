import aerodynamic.*;
import engine.*;
import tyre.*;
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

        //predefine cars
        if (modeChoice == 2) {
            RaceCar recommendedCar = RaceStrategySimulator.getRecommendedSetup(selectedTrack); // selectedTrack must be in scope
            System.out.println("\n🏎️ Recommended Setup for " + selectedTrack.getName() + ":");
            System.out.println("- Engine: " + recommendedCar.engine.getName());
            System.out.println("- Tyres: " + recommendedCar.tyre.getType());
            System.out.println("- Aero Kit: " + recommendedCar.aeroKit.getName());
            System.out.println("- Fuel Tank: " + recommendedCar.getFuelTankCapacity() + "L");

            System.out.println("💡 Why this setup? " + RaceStrategySimulator.explainSetupChoice(selectedTrack));
            System.out.print("\nWould you like to use this setup? (yes/no): ");
            String confirm = scanner.nextLine().trim().toLowerCase();

            if (confirm.equals("yes")) {
                return recommendedCar;
            } else {
                return buildRaceCar(scanner,selectedTrack); //  go back and re-prompt
            }
        }

        // Manual Configuration Flow
        System.out.println("\nSelect Engine:");
        System.out.println("[1] Standard\n[2] Turbo\n[3] Hybrid\n[4] V8\n[5] Electric");
        int engineChoice = getChoice(scanner, "Enter engine number: ", 1, 5);
        Engine engine = switch (engineChoice) {
            case 1 -> new StandardEngine();
            case 2 -> new TurboEngine();
            case 3 -> new HybridEngine();
            case 4 -> new V8Engine();
            case 5 -> new ElectricEngine();
            default -> new StandardEngine();
        };

        if (engineChoice == 5) {
            System.out.println("⚡ Note: Electric Engine uses energy consumption instead of fuel. Capacity used only for simulation.");
        }

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
            case 1 -> new StandardKit();
            case 2 -> new DownforceKit();
            case 3 -> new LowDragKit();
            case 4 -> new GroundEffectKit();
            case 5 -> new WetWeatherKit();
            case 6 -> new ExtremeAeroKit();
            default -> new StandardKit();
        };

        System.out.print("\nEnter Fuel Tank Capacity (60–100 liters): ");
        double tank;
        try {
            if (engine instanceof ElectricEngine) {
                System.out.print("Enter Battery Capacity (40–100 kWh): ");
                tank = scanner.nextDouble();
                if (tank < 40 || tank > 100) {
                    System.out.println("⚠️ Invalid battery capacity. Using default: 70 kWh");
                    tank = 70;
                }
            } else {
                System.out.print("Enter Fuel Tank Capacity (50–100 liters): ");
                tank = scanner.nextDouble();
                if (tank < 50 || tank > 100) {
                    System.out.println("⚠️ Invalid fuel capacity. Using default: 70L");
                    tank = 70;
                }
            }

        } catch (Exception e) {
            System.out.println("⚠️ Invalid input. Using default: 70");
            tank = 70.0;
        }

        return new RaceCar(engine, tyre, aero, tank);
    }

    private static int getChoice(Scanner scanner, String prompt, int min, int max) {
        int choice;
        while (true) {
            System.out.print(prompt);
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice >= min && choice <= max) return choice;
                System.out.printf("Please enter a number between %d and %d.\n", min, max);
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
}
