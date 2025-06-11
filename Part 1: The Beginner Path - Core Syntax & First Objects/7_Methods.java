/**
 * @file 7_Methods.java
 * @author dunamismax
 * @date 2025-06-11
 *
 * @brief Introduces methods to organize code into reusable, modular blocks.
 *
 *        ---
 *
 *        ## Organizing Your Code with Methods
 *
 *        As programs grow larger, putting all the code inside the `main` method
 *        becomes
 *        messy, hard to read, and difficult to maintain. A **method** is a
 *        named block of code
 *        that performs a specific task and only runs when it is "called" or
 *        "invoked".
 *
 *        Methods are the primary way we achieve **modularity** and **code
 *        reuse**. By breaking
 *        down a complex problem into smaller, logical pieces, our code becomes
 *        dramatically
 *        cleaner and easier to understand. This practice is often called the
 *        **Don't Repeat Yourself (DRY)** principle. [3, 16]
 *
 *        ### What you will learn:
 *        - How to define and call your own methods.
 *        - The components of a method signature: access modifiers, return
 *        types, names, and parameters. [1, 2]
 *        - The difference between methods that return a value and those that
 *        are `void`. [6]
 *        - How to pass data to methods using parameters (arguments).
 *        - **Method Overloading**: Creating multiple methods with the same name
 *        but different parameters. [15, 17]
 *
 *        ### A Note on the `static` Keyword
 *        For now, all our methods will include the `static` keyword. This means
 *        the method belongs
 *        to the class itself, not to a specific object created from the class.
 *        Since `main` is static,
 *        it can directly call other static methods in the same class. We will
 *        dive deep into what this
 *        truly means when we start creating our own objects in the next lesson.
 *        [7, 10]
 *
 */
public class Methods {

    // The `main` method is the entry point. It will act as the "manager"
    // that calls our other worker methods to get the job done.
    public static void main(String[] args) {

        // --- 1. Calling a simple method ---
        // This method takes no input and returns no value. It just performs an action.
        printWelcomeMessage();

        // --- 2. Calling a method with an argument ---
        // We pass the value "Maria" to the method. Inside the method, this value
        // will be assigned to its `name` parameter.
        String studentName = "Maria";
        printPersonalizedGreeting(studentName);
        printPersonalizedGreeting("David"); // We can also pass a literal value directly.

        // --- 3. Calling a method that returns a value ---
        // The `add` method returns an `int`. We must capture that returned value in a
        // variable.
        int sum = add(15, 27);
        System.out.println("\n--- Methods that Return Values ---");
        System.out.println("The result of the add method is: " + sum);

        // We can also use the returned value directly in another expression.
        System.out.println("We can use the result directly: " + add(100, 50));

        // --- 4. A more practical example combining arrays and methods ---
        int[] scores = { 100, 85, 92, 78, 95 };
        double averageScore = calculateAverage(scores); // Call the method and store the result.

        // `printf` is a way to print formatted strings. `%.2f` means "format this
        // floating-point number with 2 decimal places", and `%n` is a newline.
        System.out.printf("The average score for the class is: %.2f%n", averageScore);

        // --- 5. Method Overloading ---
        // We're calling two different methods that share the same name
        // (`displayValue`).
        // Java knows which one to use based on the type of data we provide.
        System.out.println("\n--- Method Overloading Demo ---");
        displayValue(42); // This will call the version that accepts an int.
        displayValue("Java"); // This will call the version that accepts a String.
        displayValue(averageScore); // This calls the version that accepts a double.

    } // End of the main method.

    // --- METHOD DEFINITIONS ---
    // Here we define the "worker" methods that `main` calls.

    /**
     * @brief A simple method that prints a static welcome message.
     *        It is `void` because it does not return any value.
     */
    private static void printWelcomeMessage() {
        System.out.println("--- Welcome to the lesson on Methods! ---");
    }

    /**
     * @brief Greets a user by the name provided.
     * @param name The name of the user to greet. This is a parameter.
     */
    private static void printPersonalizedGreeting(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Hello, anonymous user!");
        } else {
            System.out.println("Hello, " + name + "! Welcome to the program.");
        }
    }

    /**
     * @brief Adds two integers together.
     * @param num1 The first integer for the addition.
     * @param num2 The second integer for the addition.
     * @return The sum of the two integers.
     */
    private static int add(int num1, int num2) {
        // The `return` keyword sends a value back to where the method was called.
        // The type of the value must match the method's declared return type (`int`).
        return num1 + num2;
    }

    /**
     * @brief Calculates the average of an array of integers.
     * @param numbers An array of integers. Cannot be null.
     * @return The average of the numbers as a `double`. Returns 0.0 if the array is
     *         empty.
     */
    private static double calculateAverage(int[] numbers) {
        // It's good practice to handle edge cases, like an empty array.
        if (numbers.length == 0) {
            return 0.0;
        }

        int sum = 0;
        for (int number : numbers) {
            sum += number; // same as sum = sum + number
        }

        // To get a precise decimal result, we must perform floating-point division.
        // We "cast" the integer `sum` to a `double` before dividing.
        return (double) sum / numbers.length;
    }

    /**
     * @brief (Overloaded) Displays an integer value to the console.
     * @param num The integer to display.
     */
    private static void displayValue(int num) {
        System.out.println("Displaying an integer value: " + num);
    }

    /**
     * @brief (Overloaded) Displays a String value to the console.
     * @param text The String to display.
     */
    private static void displayValue(String text) {
        System.out.println("Displaying a String value: \"" + text + "\"");
    }

    /**
     * @brief (Overloaded) Displays a double value to the console.
     * @param num The double to display.
     */
    private static void displayValue(double num) {
        System.out.println("Displaying a double value: " + num);
    }
}