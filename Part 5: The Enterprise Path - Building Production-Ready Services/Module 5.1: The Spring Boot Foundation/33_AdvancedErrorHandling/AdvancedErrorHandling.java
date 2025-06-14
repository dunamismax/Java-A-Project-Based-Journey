
/**
 * This lesson teaches how to create a centralized exception handling mechanism
 * to produce clean, consistent, and professional JSON error responses for your API.
 *
 * ## The Problem with Default Error Handling
 *
 * By default, when an unhandled exception occurs in a Spring Boot controller, it
 * returns a generic JSON response that might contain sensitive information (like
 * stack traces) and doesn't follow a consistent format that your API clients can rely on.
 *
 * ## The Solution: `@ControllerAdvice`
 *
 * Spring provides a powerful mechanism using `@ControllerAdvice` and `@ExceptionHandler`
 * to intercept exceptions thrown from anywhere in your application and transform them
 * into custom HTTP responses. This keeps your controller logic clean and free of
 * repetitive try-catch blocks.
 *
 * ## Core Concepts & Annotations Covered:
 * - **`@ControllerAdvice`**: A global interceptor for exceptions thrown by controllers.
 * - **`@ExceptionHandler`**: A method-level annotation that specifies which exception(s)
 *   a method is responsible for handling.
 * - **Custom Exceptions**: Creating specific exceptions (e.g., `ResourceNotFoundException`)
 *   to represent business-level errors.
 * - **Standard Error DTO**: Defining a standard Java record/class for your JSON
 *   error responses.
 *
 * ## PREQUISITE: Maven `pom.xml` with `spring-boot-starter-web`.
 *
 * ## HOW TO RUN AND TEST THIS API:
 * 1.  Run the `main` method below.
 * 2.  Use `curl` or an API client to test the different scenarios.
 *
 *     - **Test a successful request:** `curl http://localhost:8080/api/users/1`
 *       (Should return a 200 OK with Alice's data)
 *
 *     - **Test a "Not Found" error:** `curl -i http://localhost:8080/api/users/99`
 *       (Should trigger our custom handler and return a 404 with a clean JSON error body)
 *
 *     - **Test a server error:** `curl -i http://localhost:8080/api/users/error`
 *       (Triggers a generic exception and returns a 500 with a safe error message)
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

// --- Main Application Entry Point ---
@SpringBootApplication
public class AdvancedErrorHandling {
    public static void main(String[] args) {
        SpringApplication.run(AdvancedErrorHandling.class, args);
    }
}

// --- 1. Custom Exception and Error Response DTO ---

/** A custom exception to represent when a resource is not found. */
class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

/**
 * A standard Data Transfer Object (DTO) for consistent JSON error responses.
 */
record ErrorResponse(LocalDateTime timestamp, int status, String error, String message, String path) {
}

// --- 2. The Global Exception Handler ---
// In a real project, this would be in its own file (e.g.,
// GlobalExceptionHandler.java).
// @ControllerAdvice allows this class to apply to all @RestController classes
// in the application.
@ControllerAdvice
class GlobalApiExceptionHandler {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(GlobalApiExceptionHandler.class);

    // This method handles our custom ResourceNotFoundException.
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        log.warn("Resource not found: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // This is a catch-all handler for any other unhandled exceptions.
    // It prevents stack traces from being exposed to the client.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
        log.error("An unexpected error occurred", ex);
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "An unexpected error occurred. Please contact support.",
                request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

// --- 3. The API Layer ---
// We reuse the user data and repository from the previous lesson.
record User(Integer id, String name) {
}

@Repository
class UserRepository {
    private final Map<Integer, User> userDatabase = new ConcurrentHashMap<>();

    public UserRepository() {
        userDatabase.put(1, new User(1, "Alice"));
    }

    public Optional<User> findById(int id) {
        return Optional.ofNullable(userDatabase.get(id));
    }

    public Collection<User> findAll() {
        return userDatabase.values();
    }
}

@RestController
@RequestMapping("/api/users")
class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * This method now throws our custom exception if the user isn't found.
     * The @ControllerAdvice handler will automatically catch it.
     */
    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }

    /**
     * A new endpoint designed to demonstrate the generic exception handler.
     */
    @GetMapping("/error")
    public void triggerError() {
        throw new IllegalStateException("This is a simulated internal server error.");
    }
}