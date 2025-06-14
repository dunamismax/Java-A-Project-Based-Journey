
/**
 * This lesson demonstrates Exception Handling, Java's mechanism for managing errors
 * gracefully. An "exception" is an event that disrupts the normal flow of a program.
 *
 * Without proper handling, exceptions will crash your application. By using `try`,
 * `catch`, and `finally`, you can create resilient code that anticipates and
 * manages problems.
 *
 * We will learn to:
 * - Use a `try-catch` block to handle potential errors.
 * - Use the `finally` block to execute essential cleanup code.
 * - Understand the difference between CHECKED and UNCHECKED exceptions.
 * - Use the `throws` keyword to delegate error handling.
 *
 * HOW TO RUN THIS FILE:
 * 1. Compile: javac ExceptionHandling.java
 * 2. Run:     java ExceptionHandling
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ExceptionHandling {

    public static void main(String[] args) {

        // --- 1. Handling an UNCHECKED Exception with try-catch-finally ---
        // Unchecked exceptions are typically programming errors (e.g., dividing by
        // zero,
        // using a null object). The compiler does not force you to handle them.

        System.out.println("--- Dividing two numbers ---");
        try {
            // The `try` block contains the "risky" code.
            int numerator = 10;
            int denominator = 0;
            int result = numerator / denominator; // This will throw an ArithmeticException.
            System.out.println("Result: " + result); // This line will never be reached.

        } catch (ArithmeticException e) {
            // The `catch` block executes ONLY if the specific exception is thrown.
            System.out.println("Error: Cannot divide by zero. " + e.getMessage());

        } finally {
            // The `finally` block ALWAYS runs, whether an exception occurred or not.
            // It's perfect for cleanup code, like closing files or network connections.
            System.out.println("This 'finally' block always executes.");
        }

        System.out.println("Program continues after the first example.\n");

        // --- 2. Handling a CHECKED Exception ---
        // Checked exceptions are for external issues a program should anticipate
        // (e.g., a file not being found). The compiler FORCES you to handle them.

        // The `readFile` method is declared with `throws FileNotFoundException`.
        // This means we, the caller, are responsible for handling that potential error.
        System.out.println("--- Attempting to read from a file ---");
        try {
            readFile("non_existent_file.txt");
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: The file could not be found. Please check the filename.");
            // For debugging, `e.printStackTrace();` is very useful.
        }

        System.out.println("Program execution finished.");
    }

    /**
     * Attempts to read from a file.
     * This method uses the `throws` keyword to declare that it might throw a
     * `FileNotFoundException`. It delegates the responsibility of handling this
     * checked exception to whatever code calls the method.
     *
     * @param fileName The name of the file to read.
     * @throws FileNotFoundException if the file cannot be found.
     */
    public static void readFile(String fileName) throws FileNotFoundException {
        System.out.println("Opening file: " + fileName);
        File file = new File(fileName);
        Scanner scanner = new Scanner(file); // This line can throw the exception.
        System.out.println("File found and opened successfully!"); // Only prints if successful.
        scanner.close();
    }
}