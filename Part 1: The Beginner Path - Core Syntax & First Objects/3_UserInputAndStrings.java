/**
 * @file 3_UserInputAndStrings.java
 * @author dunamismax
 * @date 2025-06-11
 *
 * @brief This lesson introduces how to make programs interactive by reading user
 *        input and how to work with the powerful String class.
 * ---
 *
 * ## Making Programs Interactive
 *
 * So far, our programs have been static; they do the same thing every time. To make them
 * dynamic and more useful, we need to get input from the user. The most common way to
 * do this in a console application is with the `Scanner` class. [17, 21]
 *
 * ## The Power of `String`
 *
 * In Java, text is handled by the `String` class. It's important to understand that
 * a `String` is not a primitive type like `int` or `boolean`—it is an **object**. This
 * means it has built-in behaviors (methods) that let you manipulate the text in
 * powerful ways. [9]
 *
 * A key characteristic of `String` objects is that they are **immutable**. Once a
 * `String` object is created, its value cannot be changed. [10] Every time you "modify"
 * a string (e.g., by concatenation), you are actually creating a brand new `String` object in memory. [8]
 *
 * ### What you will learn:
 * - How to use the `Scanner` class to read input from the console. [17]
 * - The crucial difference between `==` and the `.equals()` method for comparing strings. [6, 18, 23]
 * - Common and useful `String` methods for manipulation like `.length()`, `.toLowerCase()`, and `.trim()`. [5]
 * - How to build strings efficiently with `StringBuilder` and `String.format()`. [2]
 *
 */

// We must import the Scanner class from its package `java.util` before we can use it.
import java.util.Scanner;

public class UserInputAndStrings {

    public static void main(String[] args) {

        // --- READING USER INPUT WITH SCANNER ---

        // To read from the console, we create a new `Scanner` object.
        // We pass `System.in` to its constructor, which is the standard input stream
        // that typically represents keyboard input. [13, 26]
        Scanner inputReader = new Scanner(System.in);

        // It's good practice to prompt the user so they know what to do.
        System.out.print("Please enter your name: "); // Using `print` keeps the cursor on the same line.

        // The `nextLine()` method reads all keyboard input until the user presses
        // Enter.
        String userName = inputReader.nextLine();

        // Let's greet the user personally.
        System.out.println("Hello, " + userName + "!");

        // --- THE MOST IMPORTANT STRING LESSON: `equals()` vs. `==` ---

        // Beginners often make the mistake of using `==` to compare strings for
        // equality. [6, 18]
        // The `==` operator checks if two variables point to the *exact same object* in
        // memory (reference equality). [23]
        // The `.equals()` method checks if two strings have the *same sequence of
        // characters* (content equality). [23]

        String literal1 = "Java";
        String literal2 = "Java";
        String responseObject = new String("Java");

        System.out.println("\n--- String Comparison ---");
        System.out.println("Comparing two literals with ==: " + (literal1 == literal2)); // Often true due to "String
                                                                                         // pooling", but don't rely on
                                                                                         // it!
        System.out.println("Comparing literal and new object with ==: " + (literal1 == responseObject)); // Always
                                                                                                         // false,
                                                                                                         // different
                                                                                                         // objects. [6]
        System.out.println("Comparing literal and new object with .equals(): " + literal1.equals(responseObject)); // True,
                                                                                                                   // content
                                                                                                                   // is
                                                                                                                   // the
                                                                                                                   // same.
                                                                                                                   // [6]

        // Rule of thumb: ALWAYS use `.equals()` to compare the content of strings. [9,
        // 15, 19]
        // Use `.equalsIgnoreCase()` to compare content while ignoring case. [5]
        System.out.println("Is 'Java' equal to 'java' (ignoring case)? " + "Java".equalsIgnoreCase("java"));

        // --- COMMON STRING MANIPULATION METHODS ---

        String message = "  Java is a versatile language!   ";

        System.out.println("\n--- String Methods ---");
        System.out.println("Original message: '" + message + "'");
        System.out.println("Length: " + message.length()); // Gets the number of characters
        System.out.println("Uppercase: " + message.toUpperCase()); // Converts to uppercase
        System.out.println("Lowercase: " + message.toLowerCase()); // Converts to lowercase
        System.out.println("Trimmed: '" + message.trim() + "'"); // Removes leading/trailing whitespace [2]
        System.out.println("Does it start with 'Java'? " + message.trim().startsWith("Java")); // Method chaining
        System.out.println("First 'a' is at index: " + message.indexOf('a')); // Finds first occurrence
        System.out.println("Substring from index 12: '" + message.trim().substring(12) + "'"); // Extracts a portion of
                                                                                               // the string

        // --- EFFICIENT STRING BUILDING ---

        // Using the `+` operator to join many strings in a loop is inefficient because
        // it creates many intermediate String objects. [14]
        // For these situations, `StringBuilder` is the modern, preferred choice. [3, 8,
        // 10]
        StringBuilder report = new StringBuilder();
        report.append("--- Report ---\n");
        report.append("User: ").append(userName).append("\n");
        report.append("Status: Active\n");
        String finalReport = report.toString(); // Convert back to a String when done
        System.out.println(finalReport);

        // For creating formatted strings with variables, `String.format()` is very
        // powerful. [5, 24]
        int userAge = 28;
        String formattedString = String.format("%s is %d years old.", userName, userAge);
        System.out.println(formattedString);

        // Always close the Scanner when you're finished with it to release system
        // resources.
        inputReader.close();
    }
}