/**
 * This lesson introduces the Spring Framework and Spring Boot, the de facto
 * standard for building modern, enterprise-grade Java applications.
 *
 * ## Welcome to the Enterprise Path
 *
 * Spring Boot is an "opinionated" framework that makes it incredibly simple
 * to create stand-alone, production-ready applications. It values convention
 * over
 * configuration, allowing you to focus on business logic instead of boilerplate
 * setup.
 *
 * ## Core Concepts
 * - **Inversion of Control (IoC) & Dependency Injection (DI):** Spring manages
 * the
 * lifecycle of your objects (called "beans") and "injects" them where they are
 * needed,
 * promoting a highly modular and testable architecture.
 * - **Auto-configuration:** Spring Boot's superpower. It automatically
 * configures
 * your application with sensible defaults based on the libraries you include.
 *
 * PREQUISITE: A Maven `pom.xml` with the Spring Boot Web Starter dependency.
 * This single starter brings in the entire Spring web framework and an embedded
 * Tomcat web server.
 * ```xml
 * <parent>
 * <groupId>org.springframework.boot</groupId>
 * <artifactId>spring-boot-starter-parent</artifactId>
 * <version>3.3.0</version> <!-- Use a recent Spring Boot version -->
 * </parent>
 *
 * <dependencies>
 * <dependency>
 * <groupId>org.springframework.boot</groupId>
 * <artifactId>spring-boot-starter-web</artifactId>
 * </dependency>
 * </dependencies>
 * ```
 *
 * HOW TO RUN THIS FILE:
 * 1. Ensure the dependency is in your pom.xml.
 * 2. Run the `main` method below directly from your IDE.
 * 3. Open your web browser or use `curl` to visit
 * `http://localhost:8080/hello`.
 */

// --- Main Application Entry Point ---
// The @SpringBootApplication annotation is a master switch that enables
// auto-configuration, component scanning, and other key Spring Boot features.
@org.springframework.boot.autoconfigure.SpringBootApplication
public class IntroToSpringBoot {

    /**
     * The main method uses SpringApplication.run() to launch the application.
     * This single line bootstraps the Spring container and starts the embedded
     * web server.
     */
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(IntroToSpringBoot.class, args);
        System.out.println("\n✅ Spring Boot server started. Access endpoints at http://localhost:8080");
    }
}

// --- 1. The Web API Layer (Controller) ---
// The @RestController annotation tells Spring that this class is a component
// responsible for handling incoming HTTP requests. Spring will automatically
// create and manage an instance of this class (a "bean").
@org.springframework.web.bind.annotation.RestController
class GreetingController {

    /**
     * The @GetMapping annotation maps HTTP GET requests for the "/hello" path
     * to this method.
     *
     * @return A simple String, which Spring Boot will automatically write to the
     *         HTTP response body.
     */
    @org.springframework.web.bind.annotation.GetMapping("/hello")
    public String getGreeting() {
        return "Hello from your first Spring Boot REST API!";
    }

    /**
     * Another example endpoint mapped to the root path.
     *
     * @param name An optional request parameter from the URL (e.g., /?name=World)
     * @return A personalized greeting.
     */
    @org.springframework.web.bind.annotation.GetMapping("/")
    public String getPersonalizedGreeting(
            @org.springframework.web.bind.annotation.RequestParam(value = "name", defaultValue = "Guest") String name) {
        return "Welcome, " + name + "! You've reached the root of the API.";
    }
}