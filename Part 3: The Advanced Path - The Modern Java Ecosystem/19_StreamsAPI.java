
/**
 * @file 19_StreamsAPI.java
 * @author dunamismax
 * @date 2025-06-11
 *
 * @brief Project: Use the functional Streams API to perform complex data analysis on a collection.
 *
 * ---
 *
 * ## From Loops to Pipelines: The Streams API
 *
 * The **Streams API**, introduced in Java 8, provides a powerful, declarative way to process
 * sequences of elements. A stream is not a data structure itself; instead, it's a view or
 * a "conveyor belt" for data from a source (like a `List` or `Array`) that allows you to
 * chain operations together into a pipeline. [2, 3]
 *
 * This approach lets you move from an **imperative** style ("how" to do something, e.g., using
 * `for` loops and `if` statements) to a **declarative** style ("what" you want to achieve).
 * The result is often more concise, readable, and easier to parallelize.
 *
 * ### The Stream Pipeline Structure
 * A stream pipeline consists of three parts:
 * 1.  **Source:** Where the stream comes from (e.g., `myList.stream()`).
 * 2.  **Intermediate Operations (0 or more):** These transform the stream into another stream.
 *     Examples include `filter()`, `map()`, and `sorted()`. These operations are **lazy**—they
 *     don't execute until a terminal operation is called. [4, 7]
 * 3.  **Terminal Operation (1):** This produces a final result or a side effect, kicking off
 *     the actual processing of the stream. Examples include `collect()`, `forEach()`, and `count()`. [4, 7]
 *
 * ### What you will learn:
 * - How to create a stream from a collection.
 * - Key intermediate operations: `filter`, `map`, and `sorted`.
 * - Key terminal operations: `collect`, `forEach`, and numeric summaries.
 * - How to chain these operations into a clean, readable data processing pipeline.
 *
 */

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// A simple record to represent our data.
record Product(String name, String category, double price, int stock) {
}

public class StreamsAPI {

    public static void main(String[] args) {

        // --- 1. The Data Source ---
        // Let's create a list of products to work with.
        List<Product> products = new ArrayList<>(List.of(
                new Product("Laptop", "Electronics", 1200.50, 45),
                new Product("Smartphone", "Electronics", 799.99, 110),
                new Product("Coffee Maker", "Appliances", 89.90, 75),
                new Product("Desk Chair", "Furniture", 150.00, 30),
                new Product("Headphones", "Electronics", 199.99, 200),
                new Product("Blender", "Appliances", 45.50, 0), // Out of stock
                new Product("4K Monitor", "Electronics", 450.00, 60)));

        // --- PROJECT: ANALYZE THE PRODUCT DATA ---

        // **GOAL:** Find the names of all "Electronics" products that are in stock,
        // sort them by price (most expensive first), and collect their names into a new
        // list.

        System.out.println("--- Traditional Imperative Approach (for comparison) ---");
        List<Product> filteredProducts = new ArrayList<>();
        for (Product p : products) {
            if ("Electronics".equals(p.category()) && p.stock() > 0) {
                filteredProducts.add(p);
            }
        }
        filteredProducts.sort((p1, p2) -> Double.compare(p2.price(), p1.price()));
        List<String> productNamesOld = new ArrayList<>();
        for (Product p : filteredProducts) {
            productNamesOld.add(p.name());
        }
        System.out.println("Result (Old Way): " + productNamesOld);

        System.out.println("\n--- Modern Declarative Approach (Streams API) ---");
        List<String> expensiveInStockElectronics;

        // The entire logic is expressed as a single, readable pipeline.
        expensiveInStockElectronics = products.stream() // 1. Get a stream from the source list.

                // --- Intermediate Operations ---
                .filter(p -> "Electronics".equals(p.category())) // 2. Filter for electronics only.
                .filter(p -> p.stock() > 0) // 3. Filter for products that are in stock.
                .sorted((p1, p2) -> Double.compare(p2.price(), p1.price())) // 4. Sort by price, descending.
                .map(Product::name) // 5. Map each Product object to its name (String).

                // --- Terminal Operation ---
                .collect(Collectors.toList()); // 6. Collect the resulting names into a new List.

        System.out.println("Result (Streams Way): " + expensiveInStockElectronics);

        // --- ANOTHER EXAMPLE: PERFORMING A CALCULATION ---

        // **GOAL:** Calculate the total value of all products in the "Appliances"
        // category.

        double totalApplianceValue = products.stream() // Get stream
                .filter(p -> "Appliances".equals(p.category())) // Filter for appliances
                .mapToDouble(p -> p.price() * p.stock()) // Map each product to its total value (price * stock)
                                                         // `mapToDouble` is efficient for primitive streams.
                .sum(); // `sum()` is a terminal operation that calculates the total.

        System.out.printf("\nTotal value of all 'Appliances' in stock: $%.2f%n", totalApplianceValue);

        // --- SIDE EFFECT EXAMPLE: PRINTING ITEMS ---

        // **GOAL:** Print the name of each product that is out of stock.
        System.out.println("\nProducts that are out of stock:");
        products.stream()
                .filter(p -> p.stock() == 0)
                .map(Product::name)
                .forEach(System.out::println); // `forEach` is a terminal operation that performs an action.

    }
}