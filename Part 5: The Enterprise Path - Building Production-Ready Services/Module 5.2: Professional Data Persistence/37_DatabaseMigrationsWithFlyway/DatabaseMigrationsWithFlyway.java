
/**
 * This lesson introduces a critical professional skill: **Database Migrations**.
 * We will use **Flyway**, the most popular database migration tool in the Java ecosystem.
 *
 * ## The Problem with `spring.jpa.hibernate.ddl-auto=update`
 *
 * In previous lessons, we let Hibernate automatically create our database tables. This is
 * great for development but is **extremely dangerous and unsuitable for production**.
 * - It can lead to data loss if a column is renamed or removed.
 * - It provides no history or version control for your database schema.
 * - You can't easily roll back changes or set up a new database to an exact state.
 *
 * ## The Solution: Database Migrations with Flyway
 *
 * Flyway brings version control to your database. You write plain SQL scripts for every
 * schema change. Flyway tracks which scripts have been applied to a database and applies
 * new ones in the correct order.
 *
 * This ensures that your database schema is built up consistently, repeatably, and
 * safely across all environments (your machine, your teammate's machine, testing, production).
 *
 * ## SETUP:
 *
 * 1.  **Add the Flyway dependency to your `pom.xml`**:
 *     ```xml
 *     <dependency>
 *         <groupId>org.flywaydb</groupId>
 *         <artifactId>flyway-core</artifactId>
 *     </dependency>
 *     ```
 *
 * 2.  **Disable Hibernate's automatic schema generation in `application.properties`**:
 *     This is VERY IMPORTANT. We are now giving full control to Flyway.
 *     ```properties
 *     # We are no longer using Hibernate to manage the schema.
 *     spring.jpa.hibernate.ddl-auto=none
 *
 *     # Optional: Tell Spring Boot that Flyway is enabled. (Often not needed, but good practice).
 *     spring.flyway.enabled=true
 *     ```
 *
 * 3.  **Create your first SQL migration file**:
 *     - Flyway scans for SQL files in `src/main/resources/db/migration`.
 *     - Create this directory structure.
 *     - Create a file named `V1__Create_customer_table.sql`.
 *       The naming convention is critical: `V` + `VersionNumber` + `__` (two underscores) + `Description` + `.sql`.
 *
 *     **File: `src/main/resources/db/migration/V1__Create_customer_table.sql`**
 *     ```sql
 *     CREATE TABLE customer (
 *         id BIGINT AUTO_INCREMENT PRIMARY KEY,
 *         first_name VARCHAR(255) NOT NULL,
 *         last_name VARCHAR(255) NOT NULL,
 *         email VARCHAR(255) UNIQUE NOT NULL
 *     );
 *     ```
 *
 * 4. **(Optional) Create a second migration file to add more data or alter the table:**
 *    **File: `src/main/resources/db/migration/V2__Add_initial_customers.sql`**
 *     ```sql
 *    INSERT INTO customer (first_name, last_name, email) VALUES
 *        ('Alice', 'Smith', 'alice.smith@example.com'),
 *        ('Bob', 'Johnson', 'b.johnson@example.com');
 *     ```
 *
 * ## HOW IT WORKS:
 * When you run the application, Spring Boot will automatically invoke Flyway.
 * Flyway connects to the database and checks for a special table it creates called
 * `flyway_schema_history`. It compares the migrations in that table to the ones in
 * your classpath (`db/migration`) and applies any new ones in version order.
 *
 */

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// --- Main Application Entry Point ---
@SpringBootApplication
public class DatabaseMigrationsWithFlyway {
    public static void main(String[] args) {
        // When you run this, check the console logs! You will see Flyway's output
        // indicating which migration scripts it found and successfully applied.
        SpringApplication.run(DatabaseMigrationsWithFlyway.class, args);
        System.out.println("\n✅ Application started. Flyway has configured the database schema.");
        System.out.println("Visit http://localhost:8080/api/customers to see the data inserted by migration V2.");
    }
}

// --- 1. The Entity ---
// This JPA Entity must now perfectly match the table structure we defined in
// our SQL migration file.
// The application code now follows the database schema, not the other way
// around.
@Entity
class Customer {
    @Id
    @GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    // Getters and no-arg constructor required by JPA
    protected Customer() {
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
}

// --- 2. The Spring Data JPA Repository ---
// This repository works exactly as before. Spring Data JPA sees the @Entity
// and knows how to interact with the table that Flyway created.
@Repository
interface CustomerRepository extends JpaRepository<Customer, Long> {
}

// --- 3. The Web API Controller ---
// This controller allows us to verify that the data inserted by our V2
// migration script
// is actually in the database and accessible through our application.
@RestController
class CustomerController {
    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping("/api/customers")
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
}