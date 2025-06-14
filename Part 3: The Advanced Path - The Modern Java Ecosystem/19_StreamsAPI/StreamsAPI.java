
/**
 * This lesson demonstrates the Streams API, Java's modern, powerful way to
 * process collections of data.
 *
 * A Stream is a "pipeline" of operations that lets you express complex data
 * processing in a clear, declarative style. Instead of writing loops and `if`
 * statements (the "how"), you describe "what" you want to achieve.
 *
 * A Stream pipeline has three parts:
 * 1. A Source (e.g., a List).
 * 2. Zero or more Intermediate Operations (e.g., `filter`, `map`, `sorted`).
 * 3. One Terminal Operation (e.g., `collect`, `forEach`, `sum`).
 *
 * HOW TO RUN THIS FILE:
 * 1. Compile: javac StreamsAPI.java
 * 2. Run:     java StreamsAPI
 */

import java.util.List;
import java.util.stream.Collectors;

// A record to represent our data.
record Sale(String customerName, String region, double amount) {
}

public class StreamsAPI {

    public static void main(String[] args) {

        // --- 1. The Data Source ---
        List<Sale> sales = List.of(
                new Sale("Alice", "North", 250.00),
                new Sale("Bob", "South", 480.50),
                new Sale("Charlie", "North", 320.00),
                new Sale("David", "West", 150.75),
                new Sale("Eve", "North", 750.25));

        // --- PROJECT: ANALYZE SALES DATA ---

        // **GOAL:** Find the names of all customers in the "North" region
        // who made a sale over $300, and return their names in a new list.

        System.out.println("--- Finding High-Value Customers in the North Region ---");

        // The entire logic is expressed as a single, readable pipeline.
        List<String> highValueCustomers = sales.stream() // 1. Get a stream from the source list.

                // --- Intermediate Operations ---
                // These operations are chained together. Each one returns a new, transformed
                // stream.
                .filter(s -> "North".equals(s.region())) // 2. Keep only sales from the "North" region.
                .filter(s -> s.amount() > 300.00) // 3. Keep only sales where the amount is > 300.
                .map(Sale::customerName) // 4. Transform each `Sale` object into just its customer name (a String).

                // --- Terminal Operation ---
                // This kicks off the processing and produces a final result.
                .collect(Collectors.toList()); // 5. Collect the resulting names into a new `List`.

        System.out.println("High-value customers found: " + highValueCustomers);

        // --- ANOTHER EXAMPLE: PERFORMING A CALCULATION ---

        // **GOAL:** Calculate the total revenue from all sales in the "North" region.

        double totalNorthRevenue = sales.stream() // Get a stream.
                .filter(s -> "North".equals(s.region())) // Filter for the "North" region.
                .mapToDouble(Sale::amount) // Map each Sale to its amount, creating a specialized `DoubleStream`.
                .sum(); // `sum()` is a terminal operation that calculates the total.

        System.out.printf("\nTotal revenue from the North region: $%.2f%n", totalNorthRevenue);

        // --- SIDE-EFFECT EXAMPLE: PRINTING ITEMS ---

        // **GOAL:** Print a formatted summary for each sale in the "South" region.
        System.out.println("\n--- Sales report for the South Region ---");
        sales.stream()
                .filter(s -> "South".equals(s.region()))
                .forEach(s -> System.out.printf("Customer: %-10s | Amount: $%.2f%n", s.customerName(), s.amount())); // `forEach`
                                                                                                                     // performs
                                                                                                                     // an
                                                                                                                     // action
                                                                                                                     // for
                                                                                                                     // each
                                                                                                                     // item.
    }
}