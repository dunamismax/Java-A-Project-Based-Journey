
/**
 * This lesson introduces a modern, powerful way to interact with databases using
 * JPA (the Jakarta Persistence API) and its most popular implementation, Hibernate.
 * This is the standard approach for data persistence in enterprise Java.
 *
 * ## The Problem with Raw JDBC
 *
 * Writing database logic with JDBC is powerful but has drawbacks:
 * - **Verbose:** It requires a lot of boilerplate code (connections, statements, result sets).
 * - **Manual SQL:** You manually write SQL strings, which can be error-prone.
 * - **Object-Relational Mismatch:** You manually map `ResultSet` rows to Java objects,
 *   which is tedious and repetitive.
 *
 * ## The Solution: Object-Relational Mapping (ORM)
 *
 * ORM is a technique that automatically maps Java objects directly to database tables.
 * - **JPA (The Specification):** Defines a standard set of interfaces and annotations
 *   (like `@Entity`, `@Id`) for ORM.
 * - **Hibernate (The Implementation):** The powerful engine that implements the JPA
 *   specification. It generates the SQL for you and handles the object mapping.
 *
 * Spring Boot, with the Spring Data JPA starter, makes using JPA/Hibernate incredibly simple.
 *
 * ## PREQUISITES: Maven `pom.xml` Dependencies
 * ```xml
 * <!-- Spring Data JPA Starter: Includes JPA API, Hibernate, and JDBC -->
 * <dependency>
 *     <groupId>org.springframework.boot</groupId>
 *     <artifactId>spring-boot-starter-data-jpa</artifactId>
 * </dependency>
 * <!-- H2 In-Memory Database Driver -->
 * <dependency>
 *     <groupId>com.h2database</groupId>
 *     <artifactId>h2</artifactId>
 *     <scope>runtime</scope>
 * </dependency>
 * ```
 *
 * ## SETUP: Add the following to `src/main/resources/application.properties`
 * ```properties
 * # This tells Hibernate to log the actual SQL statements it generates to the console.
 * # Incredibly useful for debugging and learning.
 * spring.jpa.show-sql=true
 *
 * # This tells Hibernate to automatically create or update the database schema
 * # based on your @Entity classes when the application starts.
 * spring.jpa.hibernate.ddl-auto=update
 * ```
 *
 * ## HOW TO RUN AND TEST THIS API:
 * 1.  Run the `main` method below. Check the console logs to see Hibernate creating the table.
 * 2.  Use `curl` to interact with the API, which is now backed by a real database.
 *     - `curl http://localhost:8080/api/products`
 *     - `curl -X POST -H "Content-Type: application/json" -d '{"name":"Laptop Pro", "price":1499.99}' http://localhost:8080/api/products`
 */

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// --- Main Application Entry Point ---
@SpringBootApplication
public class IntroToJPAAndHibernate {
    public static void main(String[] args) {
        SpringApplication.run(IntroToJPAAndHibernate.class, args);
    }

    // A CommandLineRunner is a simple way to execute code on startup.
    // We'll use it to insert some initial data into our database.
    @Bean
    public CommandLineRunner initialData(ProductRepository repository) {
        return args -> {
            System.out.println("Inserting initial product data...");
            repository.save(new Product("Smart Watch", 299.99));
            repository.save(new Product("Wireless Keyboard", 75.50));
            System.out.println("Initial data inserted.");
        };
    }
}

// --- 1. The Entity Class ---
// This is the core of JPA. This class is now a blueprint for both a Java object
// AND a database table.

@Entity // @Entity marks this class as a JPA entity, mapped to a table named "product".
public class Product {

    @Id // @Id marks this field as the table's primary key.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tells Hibernate to let the database auto-generate the ID.
    private Long id;

    private String name;
    private double price;

    // JPA requires a no-argument constructor for its internal operations.
    protected Product() {
    }

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    // Standard getters and setters are needed for the framework to access the
    // properties.
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return String.format("Product[id=%d, name='%s', price=%.2f]", id, name, price);
    }
}

// --- 2. The Repository Interface (Spring Data JPA) ---
// This is where the magic happens! By extending JpaRepository, Spring Data JPA
// will automatically create a full implementation of this interface at runtime
// with
// all the standard CRUD methods (save, findById, findAll, delete, etc.).
// No more writing DAO/Repository implementation code!

@Repository
interface ProductRepository extends JpaRepository<Product, Long> {
    // We'll explore adding custom queries here in a later lesson.
}

// --- 3. The Web API Layer (Controller) ---
// This controller now uses the JPA-backed repository to interact with the
// database.

@RestController
@RequestMapping("/api/products")
class ProductController {
    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        // The .save() method will perform an INSERT or UPDATE as needed.
        return productRepository.save(product);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}