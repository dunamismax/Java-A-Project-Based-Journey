
/**
 * This lesson introduces **Spring Security**, the standard framework for securing
 * Spring applications. Security is not an optional feature; it's a fundamental
 * requirement for any real-world application.
 *
 * Spring Security is a powerful and highly customizable framework that handles
 * **Authentication** (who are you?) and **Authorization** (what are you allowed to do?).
 *
 * ## The Magic of Auto-Configuration
 *
 * Just by adding the Spring Security starter dependency, Spring Boot will automatically:
 * 1.  Secure ALL endpoints in your application by default.
 * 2.  Create a default security filter chain that requires authentication for every request.
 * 3.  Provide a default login page (for browser-based requests).
 * 4.  Create a default user with the username `user` and a randomly generated password
 *     that is printed to the console on startup.
 *
 * ## Core Concepts Covered:
 * - **Authentication**: Verifying the identity of a user (e.g., with a username/password).
 * - **Authorization**: Determining if an authenticated user has permission to access a resource.
 * - **Security Filter Chain**: The core of Spring Security. It's a series of filters that
 *   each request must pass through before it reaches your controller.
 * - **`SecurityFilterChain` Bean**: The modern, component-based way to configure security rules in Java.
 *
 * ## PREQUISITES: Maven `pom.xml` Dependencies
 * ```xml
 * <!-- Spring Security Starter -->
 * <dependency>
 *     <groupId>org.springframework.boot</groupId>
 *     <artifactId>spring-boot-starter-security</artifactId>
 * </dependency>
 *
 * <!-- Spring Web Starter (as before) -->
 * <dependency>
 *     <groupId>org.springframework.boot</groupId>
 *     <artifactId>spring-boot-starter-web</artifactId>
 * </dependency>
 * ```
 *
 * ## HOW TO RUN AND TEST THIS API:
 * 1.  Run the `main` method. **Check the console for the auto-generated password.**
 *     It will look something like: `Using generated security password: a1b2c3d4-e5f6...`
 *
 * 2.  Use `curl` to test the endpoints.
 *
 *     - **Try accessing a public endpoint:** `curl http://localhost:8080/`
 *       (This will work without a password).
 *
 *     - **Try accessing a secured endpoint WITHOUT credentials:** `curl -i http://localhost:8080/api/private`
 *       (This will fail with a `401 Unauthorized` status).
 *
 *     - **Access the secured endpoint WITH credentials:**
 *       Replace `GENERATED_PASSWORD` with the one from your console.
 *       `curl -u user:GENERATED_PASSWORD http://localhost:8080/api/private`
 *       (This will succeed with a `200 OK` status).
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.security.config.Customizer.withDefaults;

// --- Main Application Entry Point ---
@SpringBootApplication
public class IntroToSpringSecurity {
    public static void main(String[] args) {
        SpringApplication.run(IntroToSpringSecurity.class, args);
    }
}

// --- 1. The Security Configuration Class ---
// This class is where we define our custom security rules.

@Configuration // Marks this as a source of bean definitions.
@EnableWebSecurity // Enables Spring's web security support.
class BasicSecurityConfig {

    // A @Bean is an object managed by the Spring container. Here, we define a bean
    // named `securityFilterChain` which Spring Security will use to configure its
    // rules.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Authorization: Define which requests to secure.
                .authorizeHttpRequests(authorize -> authorize
                        // Permit all requests to the root URL "/" without authentication.
                        .requestMatchers("/").permitAll()
                        // Require authentication for any other request in the application.
                        .anyRequest().authenticated())
                // 2. Authentication: Configure how users should log in.
                // .httpBasic() enables HTTP Basic Authentication (sending a username/password
                // with each request).
                .httpBasic(withDefaults());

        // Build and return the configured chain.
        return http.build();
    }
}

// --- 2. The Web API Controllers ---
// We create two simple controllers to test our security rules.

@RestController
class PublicController {
    @GetMapping("/")
    public String publicEndpoint() {
        return "This is a public endpoint. Anyone can see this!";
    }
}

@RestController
class PrivateController {
    @GetMapping("/api/private")
    public String privateEndpoint() {
        // Because of our security configuration, only authenticated users can access
        // this.
        return "This is a secured endpoint. You are authenticated!";
    }
}