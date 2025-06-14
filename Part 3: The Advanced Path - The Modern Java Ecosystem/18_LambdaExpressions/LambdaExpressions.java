
/**
 * This lesson introduces Lambda Expressions, a core feature of modern functional Java.
 *
 * A lambda is an anonymous (unnamed) function that you can treat like a value.
 * Its main purpose is to pass a block of code—a behavior—as an argument to a method.
 * This is most commonly used with collections to perform actions, filtering, or transformations.
 *
 * Basic Syntax: (parameters) -> { body }
 *
 * HOW TO RUN THIS FILE:
 * 1. Compile: javac LambdaExpressions.java
 * 2. Run:     java LambdaExpressions
 */

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class LambdaExpressions {

    // A simple record to represent our data.
    record Product(String name, double price) {
    }

    public static void main(String[] args) {

        List<Product> products = new ArrayList<>(List.of(
                new Product("Laptop", 1200.00),
                new Product("Mouse", 25.00),
                new Product("Keyboard", 75.00),
                new Product("Monitor", 300.00)));

        // --- 1. Performing an Action on Each Item (`forEach`) ---
        // The `forEach` method expects a `Consumer` - a block of code that "consumes"
        // an item.
        System.out.println("--- Displaying all products: ---");
        // The lambda `p -> System.out.println(p)` says: "for each product `p`, execute
        // this action."
        products.forEach(p -> System.out.println(p));

        // --- 2. Method References (::) - A Cleaner Shorthand ---
        // If your lambda just calls one existing method, you can use a cleaner method
        // reference.
        // `System.out::println` is a direct reference to the `println` method.
        System.out.println("\n--- Displaying again with a Method Reference: ---");
        products.forEach(System.out::println);

        // --- 3. Filtering a List (`removeIf`) ---
        // The `removeIf` method expects a `Predicate` - a function that returns true or
        // false.
        // This lambda removes any product from the list where the condition (price <
        // 100) is true.
        System.out.println("\n--- Removing products cheaper than $100: ---");
        products.removeIf(p -> p.price() < 100.00);
        products.forEach(System.out::println);

        // --- 4. Transforming Data (`Function`) ---
        // A `Function` transforms an input of one type to an output of another (e.g., T
        // -> R).
        // It's the core of mapping operations, which we'll see in the next lesson on
        // Streams.
        // Let's transform our list of `Product` objects into a list of `String`
        // descriptions.
        System.out.println("\n--- Transforming products into a list of descriptions: ---");

        // This function takes a Product `p` and returns a formatted String.
        Function<Product, String> toDescription = p -> p.name() + " costs $" + p.price();

        List<String> descriptions = mapToList(products, toDescription);
        descriptions.forEach(System.out::println);
    }

    /**
     * A helper method to demonstrate a Function. It takes a list and a "mapper"
     * function,
     * applies the function to every item, and returns a new list of the transformed
     * items.
     */
    public static <T, R> List<R> mapToList(List<T> source, Function<T, R> mapper) {
        List<R> results = new ArrayList<>();
        for (T item : source) {
            // Here we apply the provided lambda function to each item.
            results.add(mapper.apply(item));
        }
        return results;
    }
}