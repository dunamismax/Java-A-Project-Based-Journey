/**
 * @file 22_UnitTesting.java
 * @author dunamismax
 * @date 2025-06-11
 *
 * @brief Introduces the professional discipline of writing automated tests to ensure code correctness using the JUnit 5 framework.
 *
 * ---
 *
 * ## Building a Safety Net: Unit Testing
 *
 * How do we know our code actually works? So far, we've relied on running the `main` method
 * and visually inspecting the output from `System.out.println()`. This is slow, error-prone,
 * and doesn't scale.
 *
 * **Unit Testing** is the practice of writing small, automated tests for the smallest "units"
 * of your application (typically, individual methods) to verify they behave as expected. It is
 * a cornerstone of modern software development. [1, 2]
 *
 * ### Why Write Unit Tests?
 * - **Confidence:** They provide a safety net, giving you confidence that your code works and that
 *   new changes haven't broken existing functionality (this is called preventing **regressions**). [4]
 * - **Refactoring:** They enable fearless refactoring. You can improve your code's design knowing
 *   your tests will immediately tell you if you've made a mistake.
 * - **Documentation:** Tests act as living documentation, demonstrating exactly how a method is
 *   intended to be used.
 *
 * ### JUnit 5: The Standard Testing Framework
 * **JUnit** is the most popular testing framework for Java. We will use JUnit 5. [6] Tests are not
 * part of your main application code; they live in a separate source directory (`src/test/java`)
 * and are run by your build tool or IDE.
 *
 * ### Prerequisites (Maven `pom.xml`):
 * To use JUnit 5, you need to add its dependency with a `test` scope. This means the library is
 * only available for compiling and running tests, and won't be included in the final application JAR.
 * ```xml
 * <dependency>
 *     <groupId>org.junit.jupiter</groupId>
 *     <artifactId>junit-jupiter-api</artifactId>
 *     <version>5.10.2</version>
 *     <scope>test</scope>
 * </dependency>
 * ```
 *
 * ### What you will learn:
 * - How to structure a test class and write test methods using the `@Test` annotation.
 * - The "Arrange, Act, Assert" pattern for structuring tests. [10]
 * - How to use assertion methods like `assertEquals` and `assertThrows` to verify outcomes.
 * - How to run tests using a build tool like Maven.
 *
 * ---
 *
 * NOTE: In a real project, the `Calculator` class and the `CalculatorTest` class would be in
 * separate files in `src/main/java` and `src/test/java` respectively. They are included
 * here together for educational purposes.
 *
 */

// --- File 1: The Class to be Tested (`src/main/java/com/example/Calculator.java`) ---

/**
 * A simple Calculator class with basic arithmetic operations.
 * This is the "unit" we want to test.
 */
class Calculator {
    public int add(int a, int b) {
        return a + b;
    }

    public int subtract(int a, int b) {
        return a - b;
    }

    public double divide(double dividend, double divisor) {
        if (divisor == 0) {
            // It's good practice to throw an exception for invalid arguments.
            throw new IllegalArgumentException("Divisor cannot be zero");
        }
        return dividend / divisor;
    }
}

// --- File 2: The Test Class (`src/test/java/com/example/CalculatorTest.java`)
// ---
// Note: To make this file runnable as a self-contained lesson, we are nesting
// the test class
// logic inside the main class. In a real project, this would be a top-level
// class.

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains the unit tests for the Calculator class.
 * The class name typically ends with "Test".
 */
class CalculatorTest {

    // The `@Test` annotation marks a method as a test method. [6]
    // Test methods are typically `void` and take no arguments.
    @Test
    void testAddition() {
        // 1. Arrange: Set up the test. Create objects and define inputs.
        Calculator calculator = new Calculator();
        int a = 5;
        int b = 10;
        int expectedResult = 15;

        // 2. Act: Call the method being tested.
        int actualResult = calculator.add(a, b);

        // 3. Assert: Verify that the actual result matches the expected result.
        // `assertEquals` is a static method from JUnit's Assertions class.
        // If the values are not equal, the test will fail and print the message. [11]
        assertEquals(expectedResult, actualResult, "5 + 10 should equal 15");
    }

    @Test
    void testSubtractionWithNegativeResult() {
        // Arrange
        Calculator calculator = new Calculator();
        int a = 7;
        int b = 20;
        int expected = -13;

        // Act
        int actual = calculator.subtract(a, b);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void testSuccessfulDivision() {
        // Arrange
        Calculator calculator = new Calculator();
        // Assert
        assertEquals(5.0, calculator.divide(10.0, 2.0));
    }

    @Test
    void testDivisionByZeroThrowsException() {
        // This test verifies that our method correctly throws an exception for invalid
        // input.
        // Arrange
        Calculator calculator = new Calculator();
        double dividend = 10.0;
        double divisor = 0.0;

        // Act & Assert
        // `assertThrows` checks that the code inside the lambda expression throws the
        // specified type of exception. The test passes if the exception is thrown. [14]
        // It fails if a different exception is thrown, or if no exception is thrown at
        // all.
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.divide(dividend, divisor);
        });
    }
}

public class UnitTesting {
    public static void main(String[] args) {
        System.out.println("--- The Concept of Unit Testing ---");
        System.out.println("This file explains the structure and purpose of unit tests.");
        System.out.println("The actual tests are not run from a `main` method.");
        System.out.println(
                "Instead, you would run them using your IDE (e.g., clicking the 'play' button next to the test class)");
        System.out.println("or by executing the build tool's test command in your terminal, like:");
        System.out.println("\n  `mvn test`\n");
        System.out.println(
                "Maven would then find all classes annotated with @Test, run them, and generate a report indicating which tests passed and which failed.");
    }
}