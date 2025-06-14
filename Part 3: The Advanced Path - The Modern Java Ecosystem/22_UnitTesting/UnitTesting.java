/**
 * This lesson introduces Unit Testing, the professional practice of writing
 * automated tests to verify that your code works as expected.
 *
 * ## Why Write Tests?
 *
 * - **Confidence:** Provides a safety net, ensuring new changes don't break
 * existing functionality (called "regression testing").
 * - **Quality:** Forces you to write testable, modular code.
 * - **Documentation:** Tests act as living examples of how your code is meant
 * to be used.
 *
 * ## JUnit 5: The Standard
 *
 * We use **JUnit 5**, the standard testing framework for Java. Tests are
 * written
 * in a separate `src/test/java` directory and are run by a build tool like
 * Maven
 * or your IDE, not from a `main` method.
 *
 * PREQUISITE: This project requires a build tool like Maven or Gradle with the
 * JUnit 5 dependency added to your `pom.xml` in the `<scope>test</scope>`.
 *
 * ---
 * NOTE: For educational clarity, the class-to-be-tested and its test class are
 * shown together below. In a real project, they would be in separate files in
 * `src/main/java` and `src/test/java`.
 * ---
 */
public class UnitTesting {

    // --- 1. The "Unit" to be Tested ---
    // This is a simple utility class whose methods we want to verify.
    // In a real project, this would be in
    // `src/main/java/com/example/StringUtils.java`.
    static class StringUtils {
        /**
         * Reverses a given string.
         * 
         * @param str The string to reverse.
         * @return The reversed string.
         * @throws IllegalArgumentException if the input string is null.
         */
        public String reverse(String str) {
            if (str == null) {
                throw new IllegalArgumentException("Input string cannot be null.");
            }
            return new StringBuilder(str).reverse().toString();
        }

        /**
         * Checks if a string is a palindrome (reads the same forwards and backwards).
         */
        public boolean isPalindrome(String str) {
            if (str == null)
                return false;
            String reversed = reverse(str);
            return str.equals(reversed);
        }
    }

    // --- 2. The Test Class ---
    // This class contains the automated tests for `StringUtils`.
    // In a real project, this would be in
    // `src/test/java/com/example/StringUtilsTest.java`.
    // It would also have the necessary imports from JUnit.
    /*
     * import org.junit.jupiter.api.Test;
     * import static org.junit.jupiter.api.Assertions.*;
     */
    static class StringUtilsTest {

        // The `@Test` annotation marks this method as a test case.
        // Test names should clearly describe what they are testing.
        @Test
        void reverse_shouldReturnReversedString_forNormalInput() {
            // A. Arrange: Set up the test conditions.
            StringUtils utils = new StringUtils();
            String input = "hello";
            String expected = "olleh";

            // B. Act: Call the method you are testing.
            String actual = utils.reverse(input);

            // C. Assert: Verify the result is what you expect.
            // `assertEquals` will pass if `expected` and `actual` are equal, otherwise it
            // fails the test.
            /* assertEquals(expected, actual); */
        }

        @Test
        void isPalindrome_shouldReturnTrue_forPalindromeString() {
            // Arrange
            StringUtils utils = new StringUtils();

            // Act & Assert
            /* assertTrue(utils.isPalindrome("racecar")); */
            /* assertTrue(utils.isPalindrome("madam")); */
        }

        @Test
        void isPalindrome_shouldReturnFalse_forNonPalindromeString() {
            // Arrange
            StringUtils utils = new StringUtils();
            // Act & Assert
            /* assertFalse(utils.isPalindrome("hello")); */
        }

        @Test
        void reverse_shouldThrowException_forNullInput() {
            // Arrange
            StringUtils utils = new StringUtils();

            // Act & Assert
            // This test PASSES if the code inside the lambda throws the specified
            // exception.
            // It FAILS if no exception (or a different one) is thrown.
            /*
             * assertThrows(IllegalArgumentException.class, () -> {
             * utils.reverse(null);
             * });
             */
        }
    }

    /**
     * This main method is for explanation only.
     * Tests are not run from here.
     */
    public static void main(String[] args) {
        System.out.println("--- The Concept of Unit Testing ---");
        System.out.println("This file explains the structure and purpose of unit tests.");
        System.out.println("The assertions in the `StringUtilsTest` class are commented out");
        System.out.println("because the JUnit library is not available when running a main method.");
        System.out.println("\nTo run tests, you would use your IDE or a build tool command like:");
        System.out.println("  `mvn test`");
        System.out.println("\nThe build tool would then find, execute, and report on all test cases.");
    }
}