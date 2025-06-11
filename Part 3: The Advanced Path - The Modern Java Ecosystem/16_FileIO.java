
/**
 * @file 16_FileIO.java
 * @author dunamismax
 * @date 2025-06-11
 *
 * @brief Persists data by reading from and writing to files using the modern Java API.
 *
 * ---
 *
 * ## Making Data Persistent: Modern File I/O
 *
 * So far, all the data our programs have worked with disappears the moment the program ends.
 * To save data permanently, we need to write it to a **file**. File I/O (Input/Output) is
 * the process of reading from and writing to files on a storage device.
 *
 * Since Java 7, the preferred way to handle file operations is with the **New I/O (NIO.2)**
 * API, located in the `java.nio.file` package. It offers a more powerful and intuitive
 * approach compared to the older `java.io.File` class. [1, 2]
 *
 * ### What you will learn:
 * - The `Path` interface: A modern representation of a file or directory path. [4]
 * - The `Paths` and `Files` utility classes for creating paths and performing file operations. [4]
 * - The `try-with-resources` statement: The modern, correct way to ensure resources like files are automatically closed. [6, 10]
 * - How to easily read and write text and lines to and from a file.
 * - How to handle the checked `IOException`.
 *
 */

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

public class FileIO {

    public static void main(String[] args) {

        // --- 1. The `Path` Interface ---
        // A `Path` object represents a path in the file system. It is OS-agnostic.
        // We use the `Paths.get()` factory method to create a Path object.
        // This will create a file in the same directory where the program is run.
        Path filePath = Paths.get("myFirstFile.txt");
        System.out.println("Working with file: " + filePath.toAbsolutePath());

        // --- 2. Writing a String to a File ---
        // The `Files` class provides convenient static methods for I/O.
        System.out.println("\n--- Writing to a File ---");
        try {
            String content = "Hello, File I/O!\nWelcome to the world of persistent data.";
            // `Files.writeString()` is a simple way to write an entire string.
            // It handles opening, writing, and closing the file automatically.
            Files.writeString(filePath, content);
            System.out.println("Successfully wrote content to " + filePath);
        } catch (IOException e) {
            // `IOException` is a checked exception. We must handle it.
            // It can occur if we don't have permission to write the file, etc.
            System.err.println("An error occurred while writing to the file: " + e.getMessage());
        }

        // --- 3. Reading the Entire File into a String ---
        System.out.println("\n--- Reading from a File ---");
        try {
            // `Files.readString()` reads all content from a file into a single string.
            String fileContent = Files.readString(filePath);
            System.out.println("Content read from file:\n---\n" + fileContent + "\n---");
        } catch (IOException e) {
            System.err.println("An error occurred while reading the file: " + e.getMessage());
        }

        // --- 4. Writing a List of Lines and Appending ---
        System.out.println("\n--- Writing multiple lines ---");
        try {
            List<String> shoppingList = Arrays.asList("Milk", "Bread", "Cheese");
            Path listPath = Paths.get("shoppingList.txt");

            // `Files.write()` takes a Path and an Iterable (like a List) and writes each
            // item as a line.
            Files.write(listPath, shoppingList);
            System.out.println("Successfully wrote shopping list to " + listPath);

            // What if we want to add to the file instead of overwriting it?
            // We use `StandardOpenOption.APPEND`.
            System.out.println("Appending 'Eggs' to the list...");
            Files.writeString(listPath, "Eggs\n", StandardOpenOption.APPEND);

        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }

        // --- 5. Reading a File Line by Line with `try-with-resources` ---
        System.out.println("\n--- Reading the shopping list line by line ---");
        Path listPath = Paths.get("shoppingList.txt");

        // The `try-with-resources` statement is the BEST way to handle resources.
        // Any resource declared in the `()` (like a file stream) that implements
        // the `AutoCloseable` interface will be CLOSED AUTOMATICALLY at the end
        // of the block, whether it completes normally or with an error. [6, 10]
        // This prevents resource leaks and is much cleaner than a `finally` block.
        try {
            // `Files.readAllLines()` reads all lines into a List<String>.
            List<String> lines = Files.readAllLines(listPath);
            System.out.println("Items in shopping list:");
            for (String item : lines) {
                System.out.println("- " + item);
            }
        } catch (IOException e) {
            System.err.println("An error occurred reading the list: " + e.getMessage());
        }

        // --- 6. Cleaning Up ---
        // It's good practice to clean up created files.
        System.out.println("\n--- Cleaning up created files ---");
        try {
            Files.deleteIfExists(filePath);
            System.out.println("Deleted: " + filePath.getFileName());
            Files.deleteIfExists(listPath);
            System.out.println("Deleted: " + listPath.getFileName());
        } catch (IOException e) {
            System.err.println("Error during cleanup: " + e.getMessage());
        }
    }
}