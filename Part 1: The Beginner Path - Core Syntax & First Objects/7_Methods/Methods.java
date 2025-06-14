/**
 * This lesson introduces methods, which are reusable blocks of code that
 * perform a specific task.
 *
 * Using methods helps keep your code organized, readable, and easy to maintain.
 * It's a core principle of programming known as "Don't Repeat Yourself" (DRY).
 *
 * We will learn to:
 * - Define and call methods.
 * - Pass data to methods using parameters.
 * - Get data back from methods using a return value.
 * - Overload methods to handle different data types.
 *
 * HOW TO RUN THIS FILE:
 * 1. Compile: javac Methods.java
 * 2. Run: java Methods
 */
public class Methods {

    // The `main` method is our program's entry point. It will call our other
    // "worker" methods to perform tasks.
    public static void main(String[] args) {

        // 1. Calling a simple method that takes no input and returns nothing.
        printWelcomeMessage();

        // 2. Calling a method and passing data (an "argument") to it.
        greetUser("Alice");
        greetUser("Bob");

        // 3. Calling a method that returns a value, and storing that value.
        int sum = add(15, 30);
        System.out.println("The sum is: " + sum);
        // We can also use the returned value directly.
        System.out.println("100 + 200 = " + add(100, 200));

        // 4. A practical example: calculating an average from an array.
        int[] scores = { 100, 85, 92, 78, 95 };
        double averageScore = calculateAverage(scores);
        System.out.println("The average score is: " + averageScore);

        // 5. Method Overloading: Using the same method name for different data types.
        // Java automatically calls the correct version based on the argument.
        displayData("Hello, Java!"); // Calls the String version
        displayData(42); // Calls the int version
        displayData(averageScore); // Calls the double version
    }

    // --- METHOD DEFINITIONS ---
    // These are the "worker" methods that `main` calls.

    // A simple method that performs an action but doesn't return a value (`void`).
    static void printWelcomeMessage() {
        System.out.println("--- Welcome to the Methods Lesson! ---");
    }

    // This method accepts a `String` parameter named `name`.
    static void greetUser(String name) {
        System.out.println("Hello, " + name + "!");
    }

    // This method accepts two `int` parameters and returns their sum as an `int`.
    static int add(int a, int b) {
        return a + b;
    }

    // This method calculates the average of an integer array and returns a
    // `double`.
    static double calculateAverage(int[] numbers) {
        if (numbers.length == 0) {
            return 0.0; // Handle the edge case of an empty array.
        }

        int total = 0;
        for (int number : numbers) {
            total += number; // same as total = total + number
        }

        // We "cast" the total to a double to ensure accurate decimal division.
        return (double) total / numbers.length;
    }

    // --- Overloaded Methods ---

    // A version of `displayData` that accepts a String.
    static void displayData(String text) {
        System.out.println("Displaying String: \"" + text + "\"");
    }

    // A version of `displayData` that accepts an int.
    static void displayData(int number) {
        System.out.println("Displaying integer: " + number);
    }

    // A version of `displayData` that accepts a double.
    static void displayData(double number) {
        System.out.println("Displaying double: " + number);
    }
}