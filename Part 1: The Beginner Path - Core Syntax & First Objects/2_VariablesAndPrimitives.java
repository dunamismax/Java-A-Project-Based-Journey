/**
 * @file 2_VariablesAndPrimitives.java
 * @author dunamismax
 * @date 2025-06-11
 *
 * @brief Demonstrates declaring, initializing, and using variables and
 *        primitive data types.
 *
 *        ---
 *
 *        ## Storing Information: Variables and Primitive Types
 *
 *        A variable is a container in memory for storing a data value. [21] To
 *        create a variable,
 *        you must give it a name and a data type. This is called "declaring" a
 *        variable. [11]
 *
 *        Java is a "statically-typed" and "strongly-typed" language. [1, 2]
 *        - **Statically-typed** means that you must declare the type of every
 *        variable before you can
 *        use it, and the type is checked at compile-time. [8, 18]
 *        - **Strongly-typed** means that once a variable's type is set, it
 *        cannot be changed, and
 *        Java will prevent you from mixing incompatible types, which helps
 *        catch errors early. [2, 5]
 *
 *        ### What you will learn:
 *        - How to declare and initialize variables.
 *        - The eight primitive data types that are the building blocks of data
 *        in Java. [8, 18]
 *        - How to perform basic operations on these variables.
 *        - Modern Java's `var` keyword for local variable type inference. [13]
 *
 *        ### Primitive Data Types
 *
 *        Java has eight built-in data types known as "primitives". They are
 *        called this because
 *        they are the simplest types of data, storing direct values rather than
 *        complex objects. [1]
 *        They are:
 *
 *        1. **`int`**: The most common type for whole numbers (e.g., -10, 0,
 *        100). [17]
 *        2. **`double`**: The default type for floating-point (decimal) numbers
 *        (e.g., 3.14, -0.5). [17]
 *        3. **`boolean`**: Represents a simple `true` or `false` value,
 *        essential for logic. [17]
 *        4. **`char`**: Holds a single character, like 'A' or '$'. It uses
 *        Unicode, a global
 *        character standard. [1]
 *        5. `byte`, `short`, `long`: Variations of whole number types for
 *        different size requirements. [18]
 *        6. `float`: A less precise decimal type than `double`. [18]
 *
 */
public class VariablesAndPrimitives {

    public static void main(String[] args) {

        // --- DECLARING AND INITIALIZING VARIABLES ---

        // A variable is declared by specifying its type and name. [11]
        // Good practice is to initialize it with a value at the same time. [10]
        // Syntax: type variableName = value;

        // 1. `int` for integers (whole numbers)
        int numberOfStudents = 25;
        int temperatureInCelsius = -4;

        System.out.println("Number of Students: " + numberOfStudents);
        System.out.println("Temperature: " + temperatureInCelsius + "°C");

        // 2. `double` for floating-point (decimal) numbers
        double accountBalance = 1307.54;
        double piApproximation = 3.14159;

        System.out.println("My account balance is: $" + accountBalance);
        System.out.println("The value of Pi is approximately: " + piApproximation);

        // 3. `boolean` for true or false values
        boolean isLoggedIn = true;
        boolean isGameOver = false;

        System.out.println("Is the user logged in? " + isLoggedIn);
        System.out.println("Is the game over? " + isGameOver);

        // 4. `char` for single characters (must use single quotes ')
        char grade = 'A';
        char currencySymbol = '$';

        System.out.println("The student's grade is: " + grade);
        System.out.println("The currency symbol is: " + currencySymbol);

        // --- BASIC OPERATIONS ---

        // You can perform mathematical operations on numeric types.
        int score = 100;
        int penalty = 25;
        int finalScore = score - penalty; // Subtraction
        System.out.println("Final score: " + finalScore);

        double price = 19.99;
        int quantity = 3;
        double totalPrice = price * quantity; // Multiplication
        System.out.println("Total price: " + totalPrice);

        // --- A SPECIAL CASE: The `String` ---

        // While not a primitive, the `String` is a fundamental data type representing
        // text.
        // It is actually an object, but Java gives it special treatment (like using
        // double quotes "").
        String welcomeMessage = "Welcome to your Java journey!";
        String studentName = "Alex";

        // You can join strings together using the `+` operator (concatenation).
        String greeting = "Hello, " + studentName + "! " + welcomeMessage;
        System.out.println(greeting);

        // --- MODERN JAVA: LOCAL VARIABLE TYPE INFERENCE with `var` ---

        // Since Java 10, if a local variable is initialized at declaration, you can
        // use the `var` keyword. The compiler infers the type from the value on the
        // right. [13, 15]
        // This can make code more concise, especially with complex type names. [15]

        var inferredNumberOfBooks = 50; // Inferred as int
        var inferredBookPrice = 29.95; // Inferred as double
        var inferredAuthorName = "Jane Doe"; // Inferred as String
        var isAvailable = true; // Inferred as boolean

        // Best practice: Use `var` when the type is obvious from the right-hand side.
        // [6, 19]
        // It improves readability by reducing redundant information.
        System.out.println("--- Inferred Types ---");
        System.out.println("Author: " + inferredAuthorName);
        System.out.println("Price: " + inferredBookPrice);

        // NOTE: The type of a 'var' variable is still fixed at compile time. It is NOT
        // dynamic. [16]
        // The following line would cause a compiler error because `inferredAuthorName`
        // is a String.
        // inferredAuthorName = 123; // COMPILE ERROR: Type mismatch
    }
}