
/**
 * This lesson teaches how to manage application settings professionally using external
 * configuration and Spring Profiles.
 *
 * In real-world applications, you never hard-code values like database URLs or API keys.
 * These settings change depending on the environment (development, testing, production).
 * Spring Boot makes managing this easy with a simple `application.properties` file.
 *
 * ## Core Concepts & Annotations Covered:
 * - **`application.properties`**: The default file in `src/main/resources` for
 *   key-value configuration pairs.
 * - **`@Value` Annotation**: Used to inject a property value from the configuration
 *   file directly into a field in your Java code.
 * - **Spring Profiles**: A core feature for separating configuration into different
 *   environments. By activating a profile (e.g., "dev"), Spring will load an
 *   additional profile-specific file (`application-dev.properties`) that can
 *   override the default values.
 *
 * ## PREQUISITE: Maven `pom.xml` with `spring-boot-starter-web`.
 *
 * ## SETUP: Create the following files in `src/main/resources`
 *
 * 1.  `application.properties` (The default/common configuration)
 *     ```properties
 *     # The port the server will run on
 *     server.port=8080
 *
 *     # A custom application property
 *     app.name=My Awesome App
 *
 *     # A default welcome message
 *     app.welcome-message=Welcome! Running with default configuration.
 *     ```
 *
 * 2.  `application-dev.properties` (Properties for the "development" profile)
 *     ```properties
 *     # Override the default message for the dev environment
 *     app.welcome-message=Hello Developer! Welcome to the DEV environment.
 *     ```
 *
 * 3.  `application-prod.properties` (Properties for the "production" profile)
 *     ```properties
 *     # Override the port and message for the prod environment
 *     server.port=80
 *     app.welcome-message=Welcome to the LIVE Production Application!
 *     ```
 *
 * ## HOW TO RUN AND TEST THIS API:
 * 1.  Run the `main` method below without any special configuration.
 *     - Visit `http://localhost:8080/config`. You will see the **default** message.
 *
 * 2.  Stop the application. Now, activate the "dev" profile. You can do this in your IDE's
 *     run configuration by adding a Program Argument: `--spring.profiles.active=dev`
 *
 * 3.  Run the `main` method again.
 *     - Visit `http://localhost:8080/config`. You will now see the **dev** message.
 *
 * 4.  Stop and activate the "prod" profile (`--spring.profiles.active=prod`).
 *     - Visit `http://localhost:80/config`. Note the port change and the **prod** message.
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

// --- Main Application Entry Point ---
@SpringBootApplication
public class ConfigurationAndProfiles {
    public static void main(String[] args) {
        SpringApplication.run(ConfigurationAndProfiles.class, args);
    }
}

// --- The Web API Layer (Controller) ---
// This controller will display the configuration values that have been
// injected.
@RestController
class ConfigController {

    // 1. Injecting Values with @Value
    // Spring reads the appropriate properties file and injects the value for the
    // given
    // key into this field at startup. The `${...}` is the placeholder syntax.

    @Value("${app.name}")
    private String applicationName;

    @Value("${app.welcome-message}")
    private String welcomeMessage;

    // You can also inject standard Spring Boot properties.
    @Value("${server.port}")
    private int serverPort;

    /**
     * Handles GET /config
     * 
     * @return A map containing the currently loaded configuration values.
     */
    @GetMapping("/config")
    public Map<String, Object> getCurrentConfig() {
        // We use a LinkedHashMap to preserve the insertion order for a clean output.
        Map<String, Object> configMap = new LinkedHashMap<>();
        configMap.put("applicationName", applicationName);
        configMap.put("serverPort", serverPort);
        configMap.put("activeWelcomeMessage", welcomeMessage);

        return configMap;
    }
}