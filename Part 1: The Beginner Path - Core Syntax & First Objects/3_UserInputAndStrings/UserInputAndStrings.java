/**
 * This lesson demonstrates how to make programs interactive by reading user input.
 * It also covers essential techniques for working with the powerful `String` class.
 *
 * A String is an object that represents a sequence of characters. Unlike primitives,
 * it comes with many built-in methods for text manipulation.
 *
 * HOW TO RUN THIS FILE:
 * 1. Compile: javac UserInputAndStrings.java
 * 2. Run:     java UserInputAndStrings
 */

// We must import the Scanner class to use it for reading input.
import java.util.Scanner;

public class UserInputAndStrings {

    public static void main(String[] args) {

        // To get user input, we create a `Scanner` object that reads from the keyboard.
        Scanner scanner = new Scanner(System.in);

        System.out.print("Please enter your name: "); // `print` keeps the cursor on the same line.

        // The `nextLine()` method reads text until the user presses Enter.
        String userName = scanner.nextLine();

        System.out.println("Hello, " + userName + "!");

        // --- IMPORTANT: How to Compare Strings ---

        // NEVER use `==` to compare the content of strings.
        // `==` checks if two variables point to the exact same object in memory.
        // `.equals()` checks if the strings contain the same characters.

        System.out.print("To prove a point, please type the word 'admin': ");
        String userRole = scanner.nextLine();

        System.out.println("Does your input equal 'admin'?");

        // This is the correct way to compare string content.
        if (userRole.equals("admin")) {
            System.out.println("SUCCESS: .equals() reports a match!");
        } else {
            System.out.println("FAILURE: .equals() reports no match.");
        }

        // For case-insensitive comparison, use `.equalsIgnoreCase()`.
        if (userRole.equalsIgnoreCase("admin")) {
            System.out.println("Login successful (case-insensitive).");
        }

        // --- Useful String Methods ---

        String rawData = "   Some Important Data   ";
        System.out.println("\nOriginal data: '" + rawData + "'");

        // We can chain methods to perform multiple operations.
        String cleanedData = rawData.trim().toUpperCase(); // `trim()` removes whitespace from both ends.

        System.out.println("Cleaned data: '" + cleanedData + "'");
        System.out.println("Length of cleaned data: " + cleanedData.length());

        // --- Efficiently Building and Formatting Strings ---

        // For simple cases, `+` is fine. For complex formatting, use `String.format()`.
        // It's like a template: `%s` is a placeholder for a String, `%d` for an
        // integer.
        int userAge = 25;
        String formattedSummary = String.format("User Summary: %s, Age: %d", userName, userAge);
        System.out.println(formattedSummary);

        // For building strings in loops, `StringBuilder` is the most efficient choice.
        StringBuilder report = new StringBuilder();
        report.append("--- User Report ---\n");
        report.append("User: ").append(userName).append("\n");
        report.append("Role: ").append(userRole);
        System.out.println(report.toString());

        // It's good practice to close the scanner to release system resources.
        scanner.close();
    }
}