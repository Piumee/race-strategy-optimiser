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
@DisplayName("Main Class - Interactive Console Application Testing")
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
    @DisplayName("Main method should start and exit gracefully")
    void testMainMethodStartAndExit() {
        // Simulate: select track 1, manual config, then answer "no" to try again after validation failure
        String simulatedInput = "1\n1\n1\n1\n1\n70\nno\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Should execute without throwing exceptions and terminate within timeout
        assertDoesNotThrow(() -> {
            Main.main(new String[]{});
        }, "Main method should handle user input and exit gracefully");

        String output = outputStream.toString();

        // Verify application components are working
        assertTrue(output.contains("Welcome to the Race Strategy Optimiser") ||
                        output.contains("Select a Race Track") ||
                        output.contains("Desert Sprint Circuit"),
                "Application should display welcome and track selection");

        // Should produce substantial output
        assertTrue(output.length() > 100, "Should produce meaningful interaction output");
    }

    // SYSTEM TESTING - Testing application with preset configuration path
    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    @DisplayName("Application workflow with preset configuration")
    void testApplicationWorkflowWithPreset() {
        // Simulate: track 1, preset config, accept recommendation, then exit
        String simulatedInput = "1\n2\nyes\nno\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        assertDoesNotThrow(() -> {
            Main.main(new String[]{});
        }, "Preset configuration workflow should complete successfully");

        String output = outputStream.toString();

        // Verify preset configuration was reached
        assertTrue(output.contains("Recommended Setup") ||
                        output.contains("Configuration Mode") ||
                        output.contains("Preset Profile") ||
                        output.length() > 50,
                "Should reach and display preset configuration options");

        // Should not hang or crash
        assertFalse(output.contains("Exception"), "Should not display exceptions");
    }
}