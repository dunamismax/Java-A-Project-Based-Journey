
/**
 * This lesson introduces **Centralized Configuration**, a pattern for managing the
 * configuration of all microservices from a single, central location.
 *
 * ## The Problem: Configuration Drift
 *
 * As our system grows, each microservice has its own `application.properties` file.
 * This leads to several problems:
 * - **Duplication:** Common properties (like database credentials, Eureka URLs, etc.)
 *   are copied across many services.
 * - **Inconsistency:** It's easy for configuration to "drift" between services and
 *   environments, leading to hard-to-diagnose bugs.
 * - **No Audit Trail:** There's no history of who changed what configuration and when.
 * - **Difficult Updates:** To change a property, you must update, rebuild, and redeploy
 *   every single service that uses it.
 *
 * ## The Solution: Spring Cloud Config Server
 *
 * Spring Cloud Config provides a centralized server that manages configuration properties
 * for all microservices. The configuration files themselves are typically stored in a
 * **Git repository**, giving you version control, history, and a single source of truth.
 *
 * ### The Flow:
 * 1.  The **Config Server** starts and connects to a Git repository containing the properties.
 * 2.  A **microservice** (a "Config Client") starts up.
 * 3.  The client contacts the Config Server and asks for its configuration based on its
 *     application name (e.g., `user-service`) and active profile (e.g., `dev`).
 * 4.  The Config Server finds the correct properties in the Git repo and sends them to the client.
 * 5.  The client uses these properties to configure itself.
 *
 * ## PREQUISITES: Maven `pom.xml` Dependencies
 * ```xml
 * <!-- For the Config Server application -->
 * <dependency>
 *     <groupId>org.springframework.cloud</groupId>
 *     <artifactId>spring-cloud-config-server</artifactId>
 * </dependency>
 *
 * <!-- For ALL other microservices (Config Clients) -->
 * <dependency>
 *     <groupId>org.springframework.cloud</groupId>
 *     <artifactId>spring-cloud-starter-config</artifactId>
 * </dependency>
 *
 * <!-- Spring Cloud BOM is required in <dependencyManagement> -->
 * ```
 *
 * ## SETUP:
 * 1.  **Create a Git Repository for Configuration:** Create a new, public Git repository (e.g., on GitHub).
 *
 * 2.  **Add Configuration Files to the Git Repo:**
 *     - `application.yml` (shared properties for all services)
 *       ```yaml
 *       # This is a shared property that all services will receive.
 *       welcome:
 *         message: "Welcome! This message is from the central Config Server!"
 *       ```
 *     - `user-service.yml` (properties specific to the user-service)
 *       ```yaml
 *       server:
 *         port: 8081
 *       # A property specific to this service
 *       user-service:
 *         greeting: "Hello from the User Service!"
 *       ```
 *
 * 3.  **Configure the Config Server (`application.properties`):**
 *     ```properties
 *     server.port=8888
 *     # The URI of the Git repository containing your config files.
 *     spring.cloud.config.server.git.uri=https://github.com/your-username/your-config-repo.git
 *     ```
 *
 * 4.  **Bootstrap the Client Services:**
 *     For clients to know where the Config Server is *before* the main application context starts,
 *     we need a `bootstrap.properties` file in `src/main/resources` for each client.
 *     **File: `user-service/src/main/resources/bootstrap.properties`**
 *     ```properties
 *     # The name must match the config file name in the Git repo (e.g., user-service.yml)
 *     spring.application.name=user-service
 *     # The address of the Config Server
 *     spring.cloud.config.uri=http://localhost:8888
 *     ```
 *
 * ## HOW TO RUN AND TEST:
 * 1.  **Start the Config Server (Terminal 1):** `java com.yourpackage.ConfigServerApplication`
 * 2.  **Start the User Service (Terminal 2):** `java com.yourpackage.UserServiceApplication`
 *     *Observe the logs. You will see it fetching its configuration from `http://localhost:8888`.*
 * 3.  **Test the User Service:** `curl http://localhost:8081/config`. The response will show properties
 *     loaded from the central Git repository, not from a local file!
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

// =====================================================================================
// --- CONFIG SERVER (Runs on Port 8888) ---
// =====================================================================================

@SpringBootApplication
@EnableConfigServer // This single annotation turns this app into a Config Server.
class ConfigServerApplication {
    public static void main(String[] args) {
        // Requires an `application.properties` file specifying the Git URI.
        SpringApplication.run(ConfigServerApplication.class, args);
    }
}

// =====================================================================================
// --- USER SERVICE (Config Client) (Runs on Port 8081, set from Config Server)
// ---
// =====================================================================================

@SpringBootApplication
class UserServiceApplication {
    public static void main(String[] args) {
        // Requires a `bootstrap.properties` to find the Config Server.
        SpringApplication.run(UserServiceApplication.class, args);
    }
}

@RestController
class ConfigTestController {
    // These values are not from a local file. They are injected at startup
    // from the properties fetched from the Spring Cloud Config Server.
    @Value("${welcome.message}")
    private String sharedWelcomeMessage;

    @Value("${user-service.greeting}")
    private String serviceSpecificGreeting;

    @Value("${server.port}")
    private int port;

    @GetMapping("/config")
    public Map<String, Object> getConfig() {
        return Map.of(
                "sharedWelcomeMessage", sharedWelcomeMessage,
                "serviceSpecificGreeting", serviceSpecificGreeting,
                "runningOnPort", port,
                "source", "Fetched from central Git repository via Config Server");
    }
}