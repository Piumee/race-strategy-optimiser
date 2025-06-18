import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Timeout;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.util.concurrent.TimeUnit;

/**
 * Test class for Main - Minimal testing for interactive console application
 * Testing Types: Integration, System Testing
 */
@DisplayName("Main Class - User Interactive Console Application Testing")
class MainTest {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }


    // INTEGRATION TESTING - Testing main method with proper exit sequence
    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    @DisplayName("Manual configuration workflow with exact output validation")
    void testManualConfigurationWorkflow() {
        // Fixed input sequence: track 1, manual config (1), engine 1, tyre 2, aero 1, fuel 70, exit (no)
        String simulatedInput = "1\n1\n1\n2\n1\n70\nno\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        assertDoesNotThrow(() -> {
            Main.main(new String[]{});
        }, "Main method should handle manual configuration and exit gracefully");

        String output = outputStream.toString();

        // Clean the output by removing \r characters and splitting properly
        String cleanOutput = output.replace("\r", "");
        String[] outputLines = cleanOutput.split("\n");

        // Validate exact workflow progression with cleaned output
        boolean foundWelcome = false;
        boolean foundTrackSelection = false;
        boolean foundConfigMode = false;
        boolean foundEngineSelection = false;
        boolean foundTyreSelection = false;
        boolean foundAeroSelection = false;
        boolean foundRaceSimulation = false;
        boolean foundSimulateAnother = false;
        boolean foundGoodbye = false;

        for (String line : outputLines) {
            String trimmedLine = line.trim();

            if (trimmedLine.equals("=== Welcome to the Race Strategy Optimiser ===")) {
                foundWelcome = true;
            }
            if (trimmedLine.equals("Select a Race Track:")) {
                foundTrackSelection = true;
            }
            if (trimmedLine.equals("Choose Configuration Mode:")) {
                foundConfigMode = true;
            }
            if (trimmedLine.equals("Select Engine:")) {
                foundEngineSelection = true;
            }
            if (trimmedLine.equals("Select Tyre:")) {
                foundTyreSelection = true;
            }
            if (trimmedLine.equals("Select Aerodynamic Kit:")) {
                foundAeroSelection = true;
            }
            if (trimmedLine.equals("=== Race Strategy Simulation ===")) {
                foundRaceSimulation = true;
            }
            if (trimmedLine.startsWith("Simulate another race?")) {
                foundSimulateAnother = true;
            }
            if (trimmedLine.contains("Thank you for using the Race Strategy Optimiser")) {
                foundGoodbye = true;
            }
        }

        // Assert exact workflow progression - no lambda needed
        assertTrue(foundWelcome, "Should display welcome message");
        assertTrue(foundTrackSelection, "Should display track selection");
        assertTrue(foundConfigMode, "Should display configuration mode selection");
        assertTrue(foundEngineSelection, "Should display engine selection");
        assertTrue(foundTyreSelection, "Should display tyre selection");
        assertTrue(foundAeroSelection, "Should display aero selection");
        assertTrue(foundRaceSimulation, "Should display race simulation");
        assertTrue(foundSimulateAnother, "Should ask about simulating another race");
        assertTrue(foundGoodbye, "Should display goodbye message");

        // Validate specific selections were processed correctly as manual inputs
        assertTrue(cleanOutput.contains("Selected: Desert Sprint Circuit"), "Should select track 1");
        assertTrue(cleanOutput.contains("Engine: Standard Engine"), "Should select standard engine");
        assertTrue(cleanOutput.contains("Tyres: Medium"), "Should select medium tyres");
        assertTrue(cleanOutput.contains("Aero Kit: Standard Kit"), "Should select standard aero kit");
        assertTrue(cleanOutput.contains("Fuel/Battery Capacity: 70.0L"), "Should set fuel capacity to 70L");
    }


    // SYSTEM TESTING - Testing application with preset configuration path
    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    @DisplayName("Preset configuration workflow with exact validation")
    void testApplicationWorkflowWithPreset() {
        // Input sequence: track 1, preset config (2), accept recommendation (yes), exit (no)
        String simulatedInput = "1\n2\nyes\nno\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        assertDoesNotThrow(() -> {
            Main.main(new String[]{});
        }, "Preset configuration workflow should complete successfully");

        String output = outputStream.toString();

        // Clean the output by removing \r characters
        String cleanOutput = output.replace("\r", "");
        String[] outputLines = cleanOutput.split("\n");

        // Validate exact preset workflow progression
        boolean foundWelcome = false;
        boolean foundTrackSelection = false;
        boolean foundConfigurationMode = false;
        boolean foundRecommendedSetup = false;
        boolean foundEngineRecommendation = false;
        boolean foundTyreRecommendation = false;
        boolean foundAeroRecommendation = false;
        boolean foundFuelRecommendation = false;
        boolean foundSetupExplanation = false;
        boolean foundUseSetupPrompt = false;
        boolean foundRaceSimulation = false;
        boolean foundSelectedCarSetup = false;
        boolean foundSimulateAnother = false;
        boolean foundGoodbye = false;

        for (String line : outputLines) {
            String trimmedLine = line.trim();

            if (trimmedLine.equals("=== Welcome to the Race Strategy Optimiser ===")) {
                foundWelcome = true;
            }
            if (trimmedLine.equals("Select a Race Track:")) {
                foundTrackSelection = true;
            }
            if (trimmedLine.equals("Choose Configuration Mode:")) {
                foundConfigurationMode = true;
            }
            if (trimmedLine.startsWith("🏎️ Recommended Setup for")) {
                foundRecommendedSetup = true;
            }
            if (trimmedLine.startsWith("- Engine:")) {
                foundEngineRecommendation = true;
            }
            if (trimmedLine.startsWith("- Tyres:")) {
                foundTyreRecommendation = true;
            }
            if (trimmedLine.startsWith("- Aero Kit:")) {
                foundAeroRecommendation = true;
            }
            if (trimmedLine.startsWith("- Fuel Tank:")) {
                foundFuelRecommendation = true;
            }
            if (trimmedLine.startsWith("💡 Why this setup?")) {
                foundSetupExplanation = true;
            }
            if (trimmedLine.startsWith("Would you like to use this setup?")) {
                foundUseSetupPrompt = true;
            }
            if (trimmedLine.equals("=== Race Strategy Simulation ===")) {
                foundRaceSimulation = true;
            }
            if (trimmedLine.equals("🚗 Selected Car Setup:")) {
                foundSelectedCarSetup = true;
            }
            if (trimmedLine.startsWith("Simulate another race?")) {
                foundSimulateAnother = true;
            }
            if (trimmedLine.contains("Thank you for using the Race Strategy Optimiser")) {
                foundGoodbye = true;
            }
        }

        // Assert exact preset workflow progression - no lambda needed
        assertTrue(foundWelcome, "Should display welcome message");
        assertTrue(foundTrackSelection, "Should display track selection");
        assertTrue(foundConfigurationMode, "Should display configuration mode");
        assertTrue(foundRecommendedSetup, "Should display recommended setup header");
        assertTrue(foundEngineRecommendation, "Should display engine recommendation");
        assertTrue(foundTyreRecommendation, "Should display tyre recommendation");
        assertTrue(foundAeroRecommendation, "Should display aero recommendation");
        assertTrue(foundFuelRecommendation, "Should display fuel tank recommendation");
        assertTrue(foundSetupExplanation, "Should provide setup explanation");
        assertTrue(foundUseSetupPrompt, "Should prompt to use setup");
        assertTrue(foundRaceSimulation, "Should run race simulation");
        assertTrue(foundSelectedCarSetup, "Should show selected car setup section");
        assertTrue(foundSimulateAnother, "Should ask about another race");
        assertTrue(foundGoodbye, "Should display goodbye message");

        // Validate preset-specific workflow elements
        assertTrue(cleanOutput.contains("Selected: Desert Sprint Circuit"), "Should select track 1");
        assertTrue(cleanOutput.contains("[1] Manual Configuration"), "Should show manual option");
        assertTrue(cleanOutput.contains("[2] Use Preset Profile"), "Should show preset option");

        // Validate recommendation structure is complete
        assertTrue(cleanOutput.contains("🏎️ Recommended Setup for Desert Sprint Circuit:"), "Should show specific track recommendation");

        // Validate all recommendation components are present
        int engineCount = countOccurrences(cleanOutput, "- Engine:");
        int tyreCount = countOccurrences(cleanOutput, "- Tyres:");
        int aeroCount = countOccurrences(cleanOutput, "- Aero Kit:");
        int fuelCount = countOccurrences(cleanOutput, "- Fuel Tank:");

        assertTrue(engineCount >= 1, "Should contain at least one engine recommendation");
        assertTrue(tyreCount >= 1, "Should contain at least one tyre recommendation");
        assertTrue(aeroCount >= 1, "Should contain at least one aero recommendation");
        assertTrue(fuelCount >= 1, "Should contain at least one fuel tank recommendation");

        // Validate explanation is provided
        assertTrue(cleanOutput.contains("💡 Why this setup?"), "Should provide setup explanation");

        // Validate user accepted the recommendation (yes was input)
        assertTrue(cleanOutput.contains("🚗 Selected Car Setup:"), "Should show that recommendation was accepted");

        // Validate simulation results are shown
        assertTrue(cleanOutput.contains("Estimated Lap Time:"), "Should show lap time estimation");
        assertTrue(cleanOutput.contains("Estimated Total Race Time:"), "Should show total race time");
        assertTrue(cleanOutput.contains("Fuel Efficiency:") || cleanOutput.contains("Energy Consumption:"),
                "Should show fuel/energy information");
    }

    // Helper method to count string occurrences
    private int countOccurrences(String text, String substring) {
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(substring, index)) != -1) {
            count++;
            index += substring.length();
        }
        return count;
    }


}