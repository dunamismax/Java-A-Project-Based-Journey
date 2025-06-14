
/**
 * @file 15_ExceptionHandling.java
 * @author dunamismax
 * @date 2025-06-11
 *
 * @brief Introduces how to write resilient code that can handle errors and unexpected events gracefully.
 *
 * ---
 *
 * ## Writing Bulletproof Code: Exception Handling
 *
 * An **exception** is an unexpected or erroneous event that occurs during the execution of a
 * program, disrupting its normal flow. Common examples include trying to divide by zero,
 * accessing a file that doesn't exist, or using an object that is `null`.
 *
 * Without proper exception handling, these events will crash your program. Java provides a
 * robust mechanism using `try`, `catch`, `finally`, and `throws` keywords to manage these
 * situations, allowing you to create stable and user-friendly applications. [2, 11]
 *
 * ### What you will learn:
 * - The purpose of the `try-catch` block to handle runtime errors. [3, 11]
 * - The `finally` block for executing crucial cleanup code, regardless of an error. [4]
 * - The difference between **Checked** and **Unchecked** exceptions, a core Java concept. [1, 9, 14]
 * - The `throws` keyword to declare that a method might pass an exception to its caller. [6]
 *
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ExceptionHandling {

    public static void main(String[] args) {

        // --- 1. The Basic `try-catch` Block ---
        // The `try` block contains "risky" code that might throw an exception. [3]
        // If an exception occurs, the `try` block is immediately exited, and Java looks
        // for a matching `catch` block to handle it. [3]
        System.out.println("--- Demonstrating try-catch ---");
        try {
            System.out.println("Attempting to divide by zero...");
            int result = 10 / 0; // This will throw an ArithmeticException
            System.out.println("This line will never be reached.");
        } catch (ArithmeticException e) {
            // This block only executes if an ArithmeticException is caught.
            System.out.println("Caught an error!");
            System.out.println("Error message: " + e.getMessage()); // e.getMessage() gives a description of the error.
        }
        System.out.println("Program continues executing after the catch block.\n");

        // --- 2. The `finally` Block ---
        // The `finally` block is optional, but it will **always** execute, whether an
        // exception occurred or not. It's essential for cleanup tasks like closing
        // files or database connections. [4, 7]
        System.out.println("--- Demonstrating finally ---");
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Enter a number: ");
            int number = scanner.nextInt();
            System.out.println("You entered: " + number);
        } catch (InputMismatchException e) {
            System.out.println("Error: That was not a valid number.");
        } finally {
            // This code runs no matter what happens in the try-catch block.
            System.out.println("The finally block is executing, closing resources.");
            scanner.close(); // Good practice to close the scanner.
        }

        // --- 3. Checked vs. Unchecked Exceptions ---

        // **Unchecked Exceptions** (subclasses of `RuntimeException`):
        // Caused by programming errors (e.g., `NullPointerException`,
        // `ArrayIndexOutOfBoundsException`).
        // The compiler does NOT force you to handle them, though you can. [1, 9]

        // **Checked Exceptions** (subclasses of `Exception` but not
        // `RuntimeException`):
        // External issues a program can reasonably anticipate (e.g., file not found,
        // network error).
        // The compiler FORCES you to handle them using `try-catch` or `throws`. [1, 9]

        // --- 4. Handling a Checked Exception ---
        // The `readFirstLineFromFile` method is declared with `throws
        // FileNotFoundException`.
        // This is a checked exception, so the compiler requires us to handle it here.
        System.out.println("\n--- Handling a Checked Exception ---");
        try {
            String firstLine = readFirstLineFromFile("non_existent_file.txt");
            System.out.println("First line: " + firstLine);
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: The file could not be found!");
            // e.printStackTrace(); // A useful method for debugging to see the full error
            // stack.
        }
    }

    /**
     * @brief Reads the first line from a file.
     * @param filePath The path to the file.
     * @return The first line of the file.
     * @throws FileNotFoundException If the file does not exist at the specified
     *                               path.
     *
     *                               This method uses the `throws` keyword to
     *                               delegate the responsibility of handling
     *                               the `FileNotFoundException` to whatever code
     *                               calls this method. [6]
     */
    public static String readFirstLineFromFile(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        // The Scanner constructor here can throw a checked exception, so we must
        // either handle it here or declare it with `throws`. We chose the latter.
        Scanner fileScanner = new Scanner(file);
        String line = fileScanner.nextLine();
        fileScanner.close();
        return line;
    }
}