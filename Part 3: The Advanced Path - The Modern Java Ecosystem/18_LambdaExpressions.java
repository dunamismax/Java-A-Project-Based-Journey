
/**
 * @file 18_LambdaExpressions.java
 * @author dunamismax
 * @date 2025-06-11
 *
 * @brief Embraces Functional Java by introducing concise, powerful anonymous functions (Lambdas).
 *
 * ---
 *
 * ## The Shift to Functional Programming: Lambda Expressions
 *
 * Introduced in Java 8, **Lambda Expressions** fundamentally changed how Java developers write code.
 * A lambda expression is essentially an **anonymous function**—a function without a name that
 * you can treat as a value, pass to methods, or store in a variable. [2, 11]
 *
 * This feature allows Java to adopt a more **functional programming style**, leading to more
 * expressive, concise, and readable code, especially when working with collections of data. [3]
 *
 * ### What are they for?
 *
 * A lambda expression provides the implementation for a **Functional Interface**. A functional
 * interface is any interface that contains exactly **one abstract method**. [4, 6] The lambda
 * expression's body is matched to that single method.
 *
 * ### What you will learn:
 * - The syntax of a lambda expression: `(parameters) -> { body }`.
 * - The concept of a `FunctionalInterface` and the `@FunctionalInterface` annotation.
 * - How to replace verbose anonymous inner classes with clean lambda expressions.
 * - How to use common built-in functional interfaces from `java.util.function` like `Predicate`, `Function`, and `Consumer`.
 * - **Method References (`::`)**: An even shorter syntax for some lambdas. [13]
 *
 */

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

// --- 1. A Functional Interface ---
// An interface with exactly one abstract method. The annotation is optional
// but is a best practice as it tells the compiler to enforce this rule. [6]
@FunctionalInterface
interface MathOperation {
    int operate(int a, int b);
}

public class LambdaExpressions {

    public static void main(String[] args) {

        // --- 2. Implementing an Interface: The Old vs. The New ---

        // The "Old Way" (before Java 8): Using an Anonymous Inner Class
        // This is verbose and boilerplate-heavy.
        MathOperation addition_old = new MathOperation() {
            @Override
            public int operate(int a, int b) {
                return a + b;
            }
        };
        System.out.println("Result (Old Way): " + addition_old.operate(10, 5));

        // The "New Way": Using a Lambda Expression
        // The lambda provides the implementation for the `operate` method.
        // `(a, b)` are the parameters. `->` separates parameters from the body.
        // `a + b` is the expression whose result is returned.
        MathOperation addition_new = (a, b) -> a + b;
        System.out.println("Result (New Way): " + addition_new.operate(10, 5));

        // For more complex, multi-line lambdas, use curly braces and a `return`
        // statement.
        MathOperation subtraction = (a, b) -> {
            System.out.println("Subtracting " + b + " from " + a);
            return a - b;
        };
        System.out.println("Result (Complex Lambda): " + subtraction.operate(10, 5));

        // --- 3. Using Common Built-in Functional Interfaces ---
        System.out.println("\n--- Common Functional Interfaces ---");
        List<String> names = new ArrayList<>(List.of("Alice", "Bob", "Charlie", "Anna", "Alex"));

        // a) `Predicate<T>` - Represents a boolean-returning function. (T -> boolean)
        // Used for filtering data. Let's find all names starting with 'A'.
        Predicate<String> startsWithA = (name) -> name.startsWith("A");
        // `removeIf` is a method on List that accepts a Predicate.
        // It removes all elements that match the predicate.
        List<String> namesCopy = new ArrayList<>(names);
        namesCopy.removeIf(startsWithA);
        System.out.println("Names after removing those starting with 'A': " + namesCopy);

        // b) `Function<T, R>` - Represents a function that transforms a T into an R. (T
        // -> R)
        // Used for mapping data. Let's get the length of each name.
        Function<String, Integer> getNameLength = (name) -> name.length();
        int aliceLength = getNameLength.apply("Alice");
        System.out.println("\nLength of 'Alice' is: " + aliceLength);

        // c) `Consumer<T>` - Represents an operation to be performed on an argument. (T
        // -> void)
        // Used for performing an action on each element.
        Consumer<String> printName = (name) -> System.out.println("Hello, " + name);
        System.out.println("\nGreeting all names:");
        // `forEach` is a method that accepts a Consumer.
        names.forEach(printName);

        // --- 4. Method References (`::`) ---
        // A method reference is a shorthand syntax for a lambda expression that only
        // calls
        // a single existing method. They make code even more readable. [13, 14]
        System.out.println("\n--- Method References Demo ---");

        // Instead of: `name -> System.out.println(name)`
        // We can use: `System.out::println`
        // This is a reference to the `println` method of the `System.out` object.
        System.out.println("Printing names with a method reference:");
        names.forEach(System.out::println);

        // This powerful paradigm of chaining operations on collections using lambdas
        // and
        // method references is the foundation of the Streams API, which we explore
        // next.
    }
}