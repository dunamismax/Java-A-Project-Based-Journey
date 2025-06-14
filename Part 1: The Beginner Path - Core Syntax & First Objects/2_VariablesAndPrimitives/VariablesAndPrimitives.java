/**
 * This lesson introduces variables and Java's primitive data types.
 *
 * A variable is a named container that stores a value. In Java, every variable
 * must have a specific type, which cannot be changed. This is known as static
 * typing.
 *
 * We will cover the most common built-in types for storing numbers, text,
 * true/false values, and single characters.
 *
 * HOW TO RUN THIS FILE:
 * 1. Compile: javac VariablesAndPrimitives.java
 * 2. Run: java VariablesAndPrimitives
 */
public class VariablesAndPrimitives {

    public static void main(String[] args) {

        // --- Primitive Types: The Building Blocks ---

        // `int`: For whole numbers.
        int age = 30;
        System.out.println("Age: " + age);

        // `double`: For decimal numbers.
        double price = 19.99;
        System.out.println("Price: $" + price);

        // `boolean`: For true or false values.
        boolean isLoggedIn = true;
        System.out.println("User is logged in: " + isLoggedIn);

        // `char`: For a single character. Uses single quotes.
        char grade = 'A';
        System.out.println("Grade: " + grade);

        // --- The String: A Fundamental Object for Text ---

        // While not a primitive, `String` is essential for handling text. Uses double
        // quotes.
        String name = "Alex";
        String message = "Hello, " + name + "! Welcome."; // The '+' joins strings
        System.out.println(message);

        // --- Modern Java: Type Inference with `var` ---

        // Since Java 10, you can use `var` for local variables.
        // The compiler automatically infers the type from the assigned value.
        // This makes code cleaner when the type is obvious.
        var itemCount = 150; // Inferred as int
        var accountBalance = 2500.75; // Inferred as double
        var aGreeting = "Hi there!"; // Inferred as String

        System.out.println("Item Count (inferred): " + itemCount);

        // NOTE: The type is still static. The following would cause an error:
        // aGreeting = 123; // COMPILE ERROR: Cannot assign an int to a String.
    }
}