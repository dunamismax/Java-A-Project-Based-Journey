/**
 * This lesson introduces arrays, Java's fundamental structure for storing a
 * fixed-size list of items of the same type.
 *
 * Think of an array as a numbered row of boxes, where each box holds the same
 * kind of data (e.g., all integers or all strings).
 *
 * Key Characteristics:
 * - Fixed Size: The size is set at creation and cannot change.
 * - Homogeneous: All elements must be of the same type.
 * - Zero-Indexed: The first element is at index 0, the second at 1, and so on.
 *
 * HOW TO RUN THIS FILE:
 * 1. Compile: javac Arrays.java
 * 2. Run: java Arrays
 */
public class Arrays {

    public static void main(String[] args) {

        // --- 1. Creating and Using an Array ---

        // The easiest way to create an array is with an "array literal".
        // The size is automatically determined by the number of items.
        String[] daysOfWeek = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };

        // Access elements using their index (position). Remember, index 0 is the first
        // item.
        System.out.println("The first day of the week is: " + daysOfWeek[0]); // Prints Monday
        System.out.println("The fifth day of the week is: " + daysOfWeek[4]); // Prints Friday

        // The `.length` property tells you the total number of elements.
        System.out.println("There are " + daysOfWeek.length + " days in the week.");

        // You can change an element at a specific index.
        daysOfWeek[0] = "Start-of-the-Week";
        System.out.println("The first day is now called: " + daysOfWeek[0]);

        // --- 2. Looping Through an Array ---

        // The `for-each` loop is the modern, preferred way to process every item.
        // It's clean and less error-prone.
        System.out.println("\n--- Listing High Scores (for-each loop) ---");
        int[] highScores = { 98, 95, 100, 88 };
        for (int score : highScores) {
            System.out.println("Score: " + score);
        }

        // Use a standard `for` loop when you need the index of the element.
        System.out.println("\n--- Listing Days with their Index (standard for loop) ---");
        for (int i = 0; i < daysOfWeek.length; i++) {
            System.out.println("Day at index " + i + ": " + daysOfWeek[i]);
        }

        // --- 3. The Most Common Array Error ---

        // Trying to access an index that doesn't exist will crash your program.
        // For an array of length 4, the valid indices are 0, 1, 2, and 3.
        // The following line, if uncommented, causes an
        // `ArrayIndexOutOfBoundsException`.
        // System.out.println(highScores[4]); // ERROR: Index 4 is out of bounds.

        // --- 4. A Quick Look at Multi-dimensional Arrays ---

        // You can create an array of arrays, perfect for representing grids or tables.
        char[][] ticTacToeBoard = {
                { 'X', 'O', ' ' },
                { ' ', 'X', 'O' },
                { 'O', ' ', 'X' }
        };

        System.out.println("\n--- Tic-Tac-Toe Board ---");
        // We use nested loops to print the grid.
        for (char[] row : ticTacToeBoard) {
            for (char cell : row) {
                System.out.print(cell + " | ");
            }
            System.out.println(); // Move to the next line after each row.
        }
    }
}