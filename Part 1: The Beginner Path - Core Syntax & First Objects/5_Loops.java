/**
 * @file 5_Loops.java
 * @author dunamismax
 * @date 2025-06-11
 *
 * @brief Teaches programs to perform repetitive tasks using loops.
 *
 *        ---
 *
 *        ## Automating Repetitive Tasks with Loops
 *
 *        Loops are a fundamental control structure in programming that allow
 *        you to execute
 *        a block of code repeatedly. Without loops, you would have to write the
 *        same code
 *        over and over again. They are essential for iterating over collections
 *        of data,
 *        waiting for user input, or running a simulation.
 *
 *        Java provides several types of loops, each suited for different
 *        scenarios. Choosing
 *        the right loop makes your code cleaner and more efficient. [11]
 *
 *        ### What you will learn:
 *        - The `for` loop: Ideal when you know the number of iterations in
 *        advance. [5, 17]
 *        - The `while` loop: Used when a loop should continue as long as a
 *        condition is true. [11, 16]
 *        - The `do-while` loop: Similar to `while`, but guarantees the loop
 *        body runs at least once. [2]
 *        - The enhanced `for-each` loop: A modern, clean way to iterate over
 *        every item in an array or collection. [1]
 *        - Loop control statements: `break` to exit a loop and `continue` to
 *        skip an iteration. [4]
 *
 */
public class Loops {

    public static void main(String[] args) {

        // An array of strings we will use for some loop examples.
        String[] planets = { "Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune" };

        // --- 1. The `for` Loop ---
        // Best for when you know exactly how many times you want to loop.
        // It consists of three parts in the parentheses:
        // 1. Initialization: `int i = 0` (runs once at the beginning)
        // 2. Condition: `i < 5` (checked before each iteration)
        // 3. Increment: `i++` (runs after each iteration)
        System.out.println("--- The `for` loop (Countdown) ---");
        for (int i = 5; i > 0; i--) {
            System.out.println("T-minus " + i);
        }
        System.out.println("Liftoff!");

        // --- 2. The `while` Loop ---
        // A `while` loop executes as long as its condition remains true. It's an
        // "entry-controlled" loop because the condition is checked *before* the loop
        // body runs. [16, 17]
        // Useful when the number of iterations is not known beforehand. [11]
        System.out.println("\n--- The `while` loop (Finding a planet) ---");
        int currentIndex = 0;
        while (currentIndex < planets.length) {
            String currentPlanet = planets[currentIndex];
            System.out.println("Checking planet: " + currentPlanet);
            if (currentPlanet.equals("Mars")) {
                System.out.println("Found Mars! Halting the search.");
                break; // `break` immediately terminates the innermost loop. [4, 9]
            }
            currentIndex++; // CRITICAL: Don't forget to update the loop variable to avoid an infinite loop!
        }

        // --- 3. The `do-while` Loop ---
        // This loop is similar to `while`, but the condition is checked *after* the
        // loop body. [2, 20]
        // This guarantees the loop will execute at least once, which is useful for
        // things like menus. [12, 18, 19]
        System.out.println("\n--- The `do-while` loop ---");
        int counter = 5;
        do {
            System.out.println("This will print at least once, even though the condition is false.");
            counter++;
        } while (counter < 5);

        // --- 4. The Enhanced `for-each` Loop (Java 5+) ---
        // The cleanest way to iterate over all elements of an array or collection. [1,
        // 14]
        // You don't need to manage an index variable, which reduces errors. [6, 14]
        // Read it as: "for each String `planet` in the `planets` array..."
        System.out.println("\n--- The enhanced `for-each` loop (Printing all planets) ---");
        for (String planet : planets) {
            System.out.println("Planet: " + planet);
        }

        // --- 5. Loop Control with `continue` ---
        // The `continue` statement skips the rest of the current iteration and
        // proceeds to the next one. [4, 8, 10]
        System.out.println("\n--- Using `continue` to skip planets without the letter 'a' ---");
        for (String planet : planets) {
            if (!planet.toLowerCase().contains("a")) {
                continue; // Skip to the next planet
            }
            System.out.println(planet + " contains the letter 'a'.");
        }
    }
}