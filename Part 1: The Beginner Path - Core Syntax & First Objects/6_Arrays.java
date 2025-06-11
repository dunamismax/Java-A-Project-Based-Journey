/**
 * @file 6_Arrays.java
 * @author dunamismax
 * @date 2025-06-11
 *
 * @brief Introduces arrays, Java's fundamental structure for storing fixed-size
 *        collections of data.
 *
 *        ---
 *
 *        ## Managing Collections of Data with Arrays
 *
 *        So far, we've used variables that hold a single value (e.g., `int x =
 *        5;`). But what if
 *        you need to work with a list of values, like 100 student scores or the
 *        7 days of the week?
 *
 *        An **array** is a container object that holds a fixed number of values
 *        of a **single type**. [2, 10]
 *        The length of an array is established when the array is created, and
 *        after creation, its
 *        length is fixed. [2, 11] Think of it as a numbered list or a set of
 *        boxes, where each box holds
 *        the same kind of item.
 *
 *        ### What you will learn:
 *        - How to declare, create, and initialize arrays. [6]
 *        - How to access and modify elements using zero-based indexing. [4]
 *        - The difference between the standard `for` loop and the `for-each`
 *        loop for iteration.
 *        - The common `ArrayIndexOutOfBoundsException` error and how to avoid
 *        it. [13]
 *        - A brief introduction to multi-dimensional arrays. [12]
 *
 *        ### Key Array Characteristics
 *        - **Fixed Size:** The size cannot be changed after creation.
 *        - **Homogeneous:** All elements must be of the same data type. [10]
 *        - **Zero-Indexed:** The first element is at index `0`, the second at
 *        `1`, and so on. The last
 *        element is at index `length - 1`. [4]
 *
 */
public class Arrays {

    public static void main(String[] args) {

        // --- 1. Declaring and Initializing Arrays ---

        // Declaration: `type[] arrayName;` is the standard Java convention.
        String[] daysOfWeek;

        // Initialization: Using the `new` keyword to create an array of a specific
        // size.
        // This creates an array that can hold 7 String objects.
        // By default, a new String array is filled with `null` values.
        daysOfWeek = new String[7];

        // We can assign values to elements using their index.
        daysOfWeek[0] = "Monday";
        daysOfWeek[1] = "Tuesday";
        // ...and so on.

        // A more common and concise way is to declare and initialize at the same time
        // using an "array literal". The size is inferred from the number of elements.
        int[] highScores = { 98, 95, 100, 88, 92 };
        double[] prices = { 19.99, 25.50, 9.75, 10.00 };

        // --- 2. Accessing and Modifying Array Elements ---

        System.out.println("--- Accessing Array Elements ---");
        System.out.println("The first high score is: " + highScores[0]); // Access the element at index 0
        System.out.println("The third price is: " + prices[2]); // Access the element at index 2

        // The `.length` property gives you the size (capacity) of the array.
        // Note: it's a property, not a method, so no parentheses `()`.
        System.out.println("There are " + highScores.length + " high scores.");

        // Modifying an element is just like assigning a value.
        System.out.println("Changing the third high score from " + highScores[2] + " to 99.");
        highScores[2] = 99;
        System.out.println("The new third high score is: " + highScores[2]);

        // --- 3. Iterating Through an Array ---

        // Method A: The standard `for` loop.
        // This is useful when you need the index of the element.
        System.out.println("\n--- Iterating with a standard for loop ---");
        for (int i = 0; i < prices.length; i++) {
            System.out.println("Price at index " + i + " is $" + prices[i]);
        }

        // Method B: The enhanced `for-each` loop.
        // This is a cleaner, preferred way when you only need the value and not the
        // index.
        System.out.println("\n--- Iterating with an enhanced for-each loop ---");
        for (int score : highScores) {
            System.out.println("A high score is: " + score);
        }

        // --- 4. The `ArrayIndexOutOfBoundsException` ---

        // This is one of the most common errors for beginners. It happens when you try
        // to access an index that is outside the valid range [0 to length-1]. [13]
        System.out.println("\n--- Common Errors ---");
        System.out.println("The last high score is at index " + (highScores.length - 1));
        // The following line would crash the program if uncommented, because the valid
        // indices for `highScores` are 0, 1, 2, 3, and 4. There is no index 5.
        // System.out.println(highScores[5]); // THROWS ArrayIndexOutOfBoundsException

        // --- 5. A Brief Look at Multi-dimensional Arrays ---

        // You can have arrays of arrays, which are useful for representing grids,
        // matrices, etc. [12]
        char[][] ticTacToeBoard = {
                { 'X', 'O', ' ' },
                { ' ', 'X', 'O' },
                { 'O', ' ', 'X' }
        };

        // You use nested loops to iterate through them.
        System.out.println("\n--- Multi-dimensional Array (Tic-Tac-Toe) ---");
        // The outer loop iterates through the rows
        for (int row = 0; row < ticTacToeBoard.length; row++) {
            // The inner loop iterates through the columns of the current row
            for (int col = 0; col < ticTacToeBoard[row].length; col++) {
                System.out.print(ticTacToeBoard[row][col] + " | ");
            }
            System.out.println("\n---------"); // Newline after each row
        }
    }
}