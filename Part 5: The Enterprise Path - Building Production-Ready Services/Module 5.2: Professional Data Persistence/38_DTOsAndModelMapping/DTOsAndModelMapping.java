
/**
 * This lesson introduces the **Data Transfer Object (DTO)** pattern, a crucial technique
 * for building secure, robust, and flexible APIs.
 *
 * ## The Problem: Exposing Your Internal Database Entities
 *
 * So far, we have been using our JPA `@Entity` classes directly in our API controllers.
 * This is simple, but it creates several major problems in real applications:
 *
 * - **Security Risk:** You might accidentally expose sensitive data (like user passwords,
 *   internal flags, etc.) that exists in your database entity but should never be
 *   sent to the client.
 * - **Inflexible API Design:** Your API's structure becomes tightly coupled to your
 *   database schema. If you rename a database column, it breaks your public API contract.
 * - **Unwanted Data:** Clients are often forced to receive more data than they need,
 *   wasting bandwidth.
 *
 * ## The Solution: The DTO Pattern
 *
 * A DTO is a simple object whose only purpose is to transfer data between different
 * layers of an application. We create separate classes (or records) for our API that
 * are completely decoupled from our database entities.
 *
 * The new flow becomes:
 * `DB -> @Entity (JPA)` -> **(MAP)** -> `DTO (API)` -> `JSON (Client)`
 *
 * We create a **mapper** to convert between the JPA Entity and the API DTO.
 *
 * ## Core Concepts Covered:
 * - The purpose and benefits of the DTO pattern.
 * - Creating specific DTOs for different API use cases (e.g., a full `UserDto` vs. a
 *   `CreateUserRequest` DTO for input).
 * - Writing a simple Mapper class to handle the conversion between Entities and DTOs.
 * - Refactoring the Service and Controller layers to use DTOs instead of entities.
 *
 * ## PREQUISITES: The same as the previous JPA lessons.
 *
 * ## HOW TO RUN AND TEST THIS API:
 * The API endpoints are the same, but observe how the JSON response is now different
 * (it contains `fullName` instead of separate first/last names), proving that we are
 * using the DTO and not the raw entity.
 *
 * - `curl http://localhost:8080/api/users`
 * - `curl -X POST -H "Content-Type: application/json" -d '{"firstName":"Charlie", "lastName":"Brown", "email":"charlie.b@example.com"}' http://localhost:8080/api/users`
 */

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

// --- Main Application Entry Point ---
@SpringBootApplication
public class DTOsAndModelMapping {
    public static void main(String[] args) {
        SpringApplication.run(DTOsAndModelMapping.class, args);
    }

    @Bean
    CommandLineRunner runner(UserRepository repo) {
        return args -> {
            repo.save(new User("Alice", "Smith", "alice.s@example.com", "password123"));
            repo.save(new User("Bob", "Johnson", "bob.j@example.com", "password456"));
        };
    }
}

// --- 1. The Internal Data Model (JPA Entity) ---
// This class represents our database table. Note it has a `password` field
// that we absolutely DO NOT want to expose in our API.

@Entity
class User {
    @Id
    @GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password; // Sensitive data!

    protected User() {
    }

    public User(String fn, String ln, String e, String p) {
        this.firstName = fn;
        this.lastName = ln;
        this.email = e;
        this.password = p;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}

// --- 2. The Public API Model (DTOs) ---
// These records define the public "shape" of our API.

/**
 * DTO for sending user data OUT to the client. Note the lack of a password
 * field.
 */
record UserDto(Long id, String fullName, String email) {
}

/** DTO for receiving user creation data IN from the client. */
record CreateUserRequest(String firstName, String lastName, String email, String password) {
}

// --- 3. The Mapper Component ---
// This class is responsible for converting between User (Entity) and UserDto
// (DTO).
// @Component is a generic Spring annotation that makes this class a managed
// bean.
@Component
class UserMapper {
    public UserDto toDto(User user) {
        String fullName = user.getFirstName() + " " + user.getLastName();
        return new UserDto(user.getId(), fullName, user.getEmail());
    }

    public User toEntity(CreateUserRequest request) {
        return new User(request.firstName(), request.lastName(), request.email(), request.password());
    }
}

// --- 4. Repository, Service, and Controller (Refactored) ---

@Repository
interface UserRepository extends JpaRepository<User, Long> {
}

@Service
class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    // The service layer now returns DTOs, not entities.
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto) // Convert each User entity to a UserDto
                .collect(Collectors.toList());
    }

    // The service now accepts a request DTO and returns a response DTO.
    public UserDto createUser(CreateUserRequest request) {
        User userEntity = userMapper.toEntity(request); // Map request DTO to an entity
        User savedEntity = userRepository.save(userEntity); // Save the entity
        return userMapper.toDto(savedEntity); // Return a response DTO
    }
}

@RestController
@RequestMapping("/api/users")
class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // This endpoint now returns a List of UserDto. Spring Boot will serialize this
    // into the clean JSON format, protecting the sensitive password field.
    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    // This endpoint now accepts the CreateUserRequest DTO.
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody CreateUserRequest request) {
        UserDto createdUser = userService.createUser(request);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
}