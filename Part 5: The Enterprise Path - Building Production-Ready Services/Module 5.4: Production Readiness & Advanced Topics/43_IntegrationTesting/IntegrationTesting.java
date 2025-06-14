
/**
 * This lesson introduces **Integration Testing** for a Spring Boot application.
 * While Unit Tests check small, isolated pieces of code, Integration Tests verify
 * that different parts of the application work together correctly.
 *
 * ## The Goal of Integration Testing
 *
 * Our goal is to test the full flow of an API request:
 * `HTTP Request -> Controller -> Service -> Repository -> Database`
 *
 * To do this reliably, we need a real database. We can't use an in-memory H2
 * database if our production app uses PostgreSQL, because their behaviors can differ.
 *
 * ## The Solution: Testcontainers
 *
 * **Testcontainers** is a brilliant library that allows you to programmatically
 * start and stop real services (like a PostgreSQL database, Kafka, Redis, etc.)
 * inside Docker containers directly from your Java test code.
 *
 * ### Benefits:
 * - **Realism:** You test against the exact same database software you use in production.
 * - **Isolation:** Each test run gets a fresh, clean database instance, ensuring
 *   tests don't interfere with each other.
 * - **Simplicity:** No need to manually install or manage a local database for testing.
 *
 * ## PREQUISITES:
 * 1.  **Docker Desktop** must be installed and running on your machine.
 * 2.  **Maven `pom.xml` Dependencies**:
 *     ```xml
 *     <!-- Spring Boot Test Starter (usually already present) -->
 *     <dependency>
 *         <groupId>org.springframework.boot</groupId>
 *         <artifactId>spring-boot-starter-test</artifactId>
 *         <scope>test</scope>
 *     </dependency>
 *
 *     <!-- Testcontainers for JUnit 5 -->
 *     <dependency>
 *         <groupId>org.testcontainers</groupId>
 *         <artifactId>junit-jupiter</artifactId>
 *         <scope>test</scope>
 *     </dependency>
 *
 *     <!-- Testcontainers module for PostgreSQL -->
 *     <dependency>
 *         <groupId>org.testcontainers</groupId>
 *         <artifactId>postgresql</artifactId>
 *         <scope>test</scope>
 *     </dependency>
 *
 *     <!-- PostgreSQL JDBC Driver (needed for the app to connect) -->
 *     <dependency>
 *         <groupId>org.postgresql</groupId>
 *         <artifactId>postgresql</artifactId>
 *         <scope>runtime</scope>
 *     </dependency>
 *     ```
 *
 * ## HOW TO RUN THE TEST:
 * - This file contains both the application code and the test code for clarity.
 * - In a real project, the test class would be in `src/test/java`.
 * - Run the `BookApiIntegrationTest` class from your IDE or by running `mvn test`
 *   from your terminal. Observe as Docker starts a PostgreSQL container before
 *   the tests run and stops it afterward.
 */

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// --- Main Application Entry Point ---
// The application itself is simple; the focus is on the test.
@SpringBootApplication
public class IntegrationTesting {
    public static void main(String[] args) {
        SpringApplication.run(IntegrationTesting.class, args);
    }
}

// --- Application Code (Entity, Repository, Controller) ---
@Entity
class Book {
    @Id
    @GeneratedValue
    private Long id;
    private String title;

    protected Book() {
    }

    public Book(String t) {
        this.title = t;
    }

    public String getTitle() {
        return title;
    }
}

interface BookRepository extends JpaRepository<Book, Long> {
}

@RestController
@RequestMapping("/api/books")
class BookController {
    private final BookRepository repository;

    public BookController(BookRepository r) {
        this.repository = r;
    }

    @GetMapping
    List<Book> getAll() {
        return repository.findAll();
    }

    @PostMapping
    Book create(@RequestBody Book book) {
        return repository.save(book);
    }
}

// --- THE INTEGRATION TEST CLASS ---

/**
 * This test class will test the entire application, from the web layer to the
 * database.
 * NOTE: In a real project, this class would be in the `src/test/java`
 * directory.
 */
@Testcontainers // Enables automatic startup and cleanup of containers defined with @Container
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK) // Loads the full Spring application context
@AutoConfigureMockMvc // Configures MockMvc, a tool to simulate HTTP requests without a real network
                      // call
@Transactional // Ensures each test method runs in its own transaction and is rolled back
               // afterwards
class BookApiIntegrationTest {

    // 1. Define the container
    // This will start a PostgreSQL Docker container before any tests are run.
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    // 2. Dynamically configure the application context
    // This method is called BEFORE the Spring context is created. It overrides the
    // default database configuration with the dynamic URL, username, and password
    // from the running Testcontainer instance.
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        // We let Hibernate create the schema in our test database.
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    // 3. Autowire components for testing
    @Autowired
    private MockMvc mockMvc; // Used to perform fake HTTP requests

    @Autowired
    private BookRepository bookRepository; // Used to set up test data directly in the DB

    @Test
    void testGetAllBooks_ReturnsEmptyList_WhenNoBooksExist() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0)); // Assert that the JSON array is empty
    }

    @Test
    void testCreateBook_SuccessfullyCreatesAndReturnsBook() throws Exception {
        // Arrange
        String bookJson = """
                {
                    "title": "The Hobbit"
                }
                """;

        // Act & Assert
        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists()) // Assert that the response has an ID
                .andExpect(jsonPath("$.title").value("The Hobbit"));
    }

    @Test
    void testGetAllBooks_ReturnsExistingBooks() throws Exception {
        // Arrange: Insert data directly into the database for this test
        bookRepository.save(new Book("1984"));
        bookRepository.save(new Book("Brave New World"));

        // Act & Assert
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("1984"))
                .andExpect(jsonPath("$[1].title").value("Brave New World"));
    }
}