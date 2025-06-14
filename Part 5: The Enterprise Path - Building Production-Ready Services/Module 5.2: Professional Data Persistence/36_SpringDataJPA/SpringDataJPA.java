
/**
 * This lesson demonstrates the incredible power of the Spring Data JPA module, which
 * takes the concepts from the last lesson and elevates them to a new level of
 * productivity and simplicity.
 *
 * ## The Problem with Basic JPA/Hibernate
 *
 * While Hibernate eliminates manual SQL for simple CRUD, you often need more
 * complex queries, such as "find all products more expensive than X" or "find a user by
 * their email address." In traditional JPA, you would need to write these queries
 * yourself using JPQL (Java Persistence Query Language) or the Criteria API.
 *
 * ## The Solution: Spring Data JPA Repositories
 *
 * Spring Data JPA takes this a step further. It can automatically create queries
 * for you directly from your method names! This is called **query derivation**. By
 * simply defining a method on your repository interface with a specific naming
 * convention, Spring Data JPA will parse the name, generate the correct JPQL query,
 * and implement the method for you at runtime.
 *
 * ## Core Concepts Covered:
 * - **Query Derivation**: Writing repository methods like `findByNameContaining` and
 *   having Spring Data JPA automatically implement them.
 * - **`@Query` Annotation**: For when query derivation isn't enough, you can use `@Query`
 *   to provide your own custom JPQL query.
 * - **Sorting and Pagination**: How to easily sort results and retrieve data in "pages"
 *   without writing any implementation code.
 *
 * ## PREQUISITES: Same as the previous lesson.
 * A `pom.xml` with `spring-boot-starter-data-jpa` and `h2`, and the same
 * `application.properties` to show SQL and auto-update the schema.
 *
 * ## HOW TO RUN AND TEST THIS API:
 * 1.  Run the `main` method below.
 * 2.  Use `curl` or an API client to test the new, more advanced endpoints.
 *
 *     - **Find by name:** `curl "http://localhost:8080/api/laptops?name=Air"`
 *       (Note the quotes for URLs with query parameters in most terminals)
 *
 *     - **Find by price:** `curl "http://localhost:8080/api/laptops/search/price?min=1000"`
 *
 *     - **Find expensive laptops (custom @Query):** `curl "http://localhost:8080/api/laptops/search/expensive"`
 */

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// --- Main Application Entry Point ---
@SpringBootApplication
public class SpringDataJPA {
    public static void main(String[] args) {
        SpringApplication.run(SpringDataJPA.class, args);
    }

    // Use a CommandLineRunner to pre-populate our database with test data.
    @Bean
    public CommandLineRunner initialData(LaptopRepository repository) {
        return args -> {
            System.out.println("--- Populating Laptop Database ---");
            repository.save(new Laptop("Laptop Pro", "TechCorp", 1499.99));
            repository.save(new Laptop("Laptop Air", "TechCorp", 999.50));
            repository.save(new Laptop("Gamer Book", "GameGear", 2100.00));
            repository.save(new Laptop("Workstation Z", "BuildsRUs", 3500.00));
            repository.save(new Laptop("Ultra Thin", "StyleBook", 1150.00));
            System.out.println("--- Population Complete ---");
        };
    }
}

// --- 1. The Entity ---
// A simple JPA entity to represent a Laptop.
@Entity
class Laptop {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String brand;
    private double price;

    protected Laptop() {
    } // JPA requires no-arg constructor

    public Laptop(String name, String brand, double price) {
        this.name = name;
        this.brand = brand;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public double getPrice() {
        return price;
    }
}

// --- 2. The Spring Data JPA Repository ---
// This interface is the star of the show. We define method signatures, and
// Spring Data JPA provides the implementation for us at runtime.

@Repository
interface LaptopRepository extends JpaRepository<Laptop, Long> {

    // **Query Derivation Examples**

    // Spring Data parses this method name and generates the query:
    // `SELECT l FROM Laptop l WHERE l.brand = ?1`
    List<Laptop> findByBrand(String brand);

    // `Containing` translates to a `LIKE '%...%'` query. It's case-sensitive.
    List<Laptop> findByNameContaining(String text);

    // You can combine keywords. This generates a query for laptops with a price
    // greater than the provided `minPrice`.
    List<Laptop> findByPriceGreaterThan(double minPrice);

    // **@Query Annotation Example**

    // When method names get too long or the logic is complex, you can write your
    // own JPQL (Java Persistence Query Language) query.
    @Query("SELECT l FROM Laptop l WHERE l.price > :priceThreshold")
    List<Laptop> findVeryExpensiveLaptops(@Param("priceThreshold") double priceThreshold);
}

// --- 3. The Web API Controller ---
// This controller exposes our new, advanced repository methods.

@RestController
@RequestMapping("/api/laptops")
class LaptopController {
    private final LaptopRepository laptopRepository;

    public LaptopController(LaptopRepository laptopRepository) {
        this.laptopRepository = laptopRepository;
    }

    @GetMapping
    public List<Laptop> searchLaptops(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand) {
        if (name != null) {
            return laptopRepository.findByNameContaining(name);
        }
        if (brand != null) {
            return laptopRepository.findByBrand(brand);
        }
        return laptopRepository.findAll();
    }

    @GetMapping("/search/price")
    public List<Laptop> searchByPrice(@RequestParam double min) {
        return laptopRepository.findByPriceGreaterThan(min);
    }

    @GetMapping("/search/expensive")
    public List<Laptop> getExpensiveLaptops() {
        // We define "expensive" as over 2000 for this example.
        return laptopRepository.findVeryExpensiveLaptops(2000.00);
    }
}