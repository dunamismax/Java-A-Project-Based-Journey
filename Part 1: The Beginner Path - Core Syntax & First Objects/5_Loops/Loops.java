/**
 * This lesson explains loops, which are used to execute a block of code
 * repeatedly.
 * Loops are essential for automating tasks like processing every item in a
 * list.
 * We will cover the `for`, `while`, and the modern `for-each` loop.
 *
 * HOW TO RUN THIS FILE:
 * 1. Compile: javac Loops.java
 * 2. Run: java Loops
 */
public class Loops {

    public static void main(String[] args) {

        // --- 1. The `for` Loop ---
        // Ideal when you know exactly how many times to repeat.
        // Structure: (initializer; condition; updater)
        System.out.println("--- `for` loop: Countdown ---");
        for (int i = 3; i > 0; i--) {
            System.out.println(i);
        }
        System.out.println("Liftoff!");

        // --- 2. The `while` Loop ---
        // Repeats as long as a condition is true. Use when the number of iterations is
        // unknown.
        System.out.println("\n--- `while` loop: Dice Roll ---");
        int diceRoll = 0;
        while (diceRoll != 6) {
            // Math.random() gives a double between 0.0 and <1.0
            diceRoll = (int) (Math.random() * 6) + 1;
            System.out.println("You rolled a " + diceRoll);
        }
        System.out.println("You rolled a 6! You win!");

        // --- 3. The Enhanced `for-each` Loop (Modern & Recommended) ---
        // The cleanest way to iterate over every item in an array or collection.
        // It reads as "for each planet in planets...".
        System.out.println("\n--- `for-each` loop: Listing planets ---");
        String[] planets = { "Mercury", "Venus", "Earth", "Mars" };
        for (String planet : planets) {
            System.out.println("- " + planet);
        }

        // --- Loop Control: `break` and `continue` ---
        System.out.println("\n--- Loop Control: Finding specific planets ---");

        // `break` exits a loop immediately.
        // `continue` skips the current iteration and moves to the next.

        for (String planet : planets) {
            if (planet.equals("Earth")) {
                System.out.println("Found Earth, our home! Stopping search.");
                break; // Exit the loop now
            }

            if (!planet.contains("r")) {
                System.out.println("Skipping " + planet + " (no 'r' sound)...");
                continue; // Skip to the next planet
            }

            System.out.println("Analyzing " + planet + "...");
        }

        // --- 4. The `do-while` Loop ---
        // Similar to `while`, but guarantees the code block runs at least once
        // because the condition is checked at the end.
        System.out.println("\n--- `do-while` loop: Menu example ---");
        int choice;
        do {
            System.out.println("This menu will always show at least once.");
            choice = 0; // In a real app, you'd get user input here.
        } while (choice != 0);
        System.out.println("Exiting menu.");
    }
}