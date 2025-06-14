
/**
 * This lesson demonstrates modern File I/O (Input/Output) in Java.
 * File I/O allows your program to save data permanently by writing to files
 * and to process that data by reading from files.
 *
 * We will use the modern Java NIO.2 (New I/O) library, which is the
 * recommended approach for file operations. Key components are:
 *
 * - `Path`: Represents a location in the file system.
 * - `Paths`: A helper class to create `Path` objects.
 * - `Files`: A helper class with static methods for reading, writing, and
 *   manipulating files (e.g., `write()`, `readAllLines()`).
 *
 * All file operations can cause an `IOException`, which is a checked exception
 * that we must handle with a `try-catch` block.
 *
 * HOW TO RUN THIS FILE:
 * 1. Compile: javac FileIO.java
 * 2. Run:     java FileIO
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

        // 1. Define the file path using the modern `Path` interface.
        Path diaryFile = Paths.get("my_diary.txt");
        System.out.println("Working with file: " + diaryFile.toAbsolutePath());

        try {
            // --- 2. Writing a List of Lines to a File ---
            System.out.println("\nWriting today's diary entries...");
            List<String> entries = Arrays.asList(
                    "Dear Diary,",
                    "Today, I learned about modern Java File I/O.",
                    "It's much simpler than I thought!");
            // `Files.write()` is a convenient one-liner that handles opening, writing,
            // and closing the file. By default, it will overwrite the file if it exists.
            Files.write(diaryFile, entries);
            System.out.println("Entries saved successfully.");

            // --- 3. Appending a New Line to an Existing File ---
            System.out.println("\nAdding a late-night thought...");
            // To add to a file instead of overwriting, we use an "Open Option".
            // `StandardOpenOption.APPEND` tells Java to add to the end of the file.
            String newThought = "I should practice this more tomorrow.\n";
            Files.writeString(diaryFile, newThought, StandardOpenOption.APPEND);
            System.out.println("Thought appended.");

            // --- 4. Reading All Lines from the File ---
            System.out.println("\n--- Reading My Diary ---");
            // `Files.readAllLines()` reads the entire file into a List of strings.
            // This is perfect for reading and processing text-based files.
            List<String> allLines = Files.readAllLines(diaryFile);
            for (String line : allLines) {
                System.out.println(line);
            }
            System.out.println("--- End of Diary ---");

        } catch (IOException e) {
            // This block catches any I/O errors, such as not having permission to write.
            System.err.println("A file error occurred: " + e.getMessage());
        } finally {
            // --- 5. Cleaning Up the File ---
            // A `finally` block is a good place to put cleanup code.
            System.out.println("\nCleaning up the created diary file...");
            try {
                Files.deleteIfExists(diaryFile);
                System.out.println("File deleted.");
            } catch (IOException e) {
                System.err.println("Error during cleanup: " + e.getMessage());
            }
        }
    }
}