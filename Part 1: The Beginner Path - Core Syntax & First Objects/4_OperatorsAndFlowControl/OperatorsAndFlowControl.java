/**
 * @file 4_OperatorsAndFlowControl.java
 * @author dunamismax
 * @date 2025-06-11
 *
 * @brief Explores operators to perform calculations and flow control to make
 *        decisions.
 *
 *        ---
 *
 *        ## Giving Your Program a Brain
 *
 *        To write useful programs, you need two things: the ability to perform
 *        operations
 *        on data (like math or comparisons) and the ability to make decisions
 *        based on
 *        the results. This is where operators and flow control statements come
 *        in. [16]
 *
 *        **Operators** are special symbols that perform specific operations on
 *        one, two, or
 *        three operands, and then return a result. For example, `+` adds two
 *        numbers. [17]
 *
 *        **Flow Control** statements (like `if` and `switch`) allow you to
 *        control the
 *        execution path of your program. They are the "decision-making" part of
 *        your code,
 *        allowing it to react differently to different situations. [3, 9]
 *
 *        ### What you will learn:
 *        - How to use arithmetic, relational, and logical operators.
 *        - The importance of "short-circuiting" with logical operators. [2, 12]
 *        - How to control program flow with `if`, `else if`, and `else`
 *        statements. [3]
 *        - How to use the modern "enhanced" `switch` statement for clear,
 *        multi-branch logic. [1, 15]
 *        - The ternary operator for concise conditional assignments. [17]
 *
 */
public class OperatorsAndFlowControl {

    public static void main(String[] args) {

        // --- PART 1: OPERATORS ---

        System.out.println("--- Arithmetic Operators ---");
        int a = 10;
        int b = 3;
        System.out.println("a + b = " + (a + b)); // Addition
        System.out.println("a - b = " + (a - b)); // Subtraction
        System.out.println("a * b = " + (a * b)); // Multiplication
        System.out.println("a / b = " + (a / b)); // Integer Division (discards remainder)
        System.out.println("a % b = " + (a % b)); // Modulo (gives the remainder)

        // Note on division: dividing two integers results in an integer.
        // To get a precise decimal result, at least one operand must be a double.
        double preciseA = 10.0;
        System.out.println("preciseA / b = " + (preciseA / b)); // Floating-point division

        System.out.println("\n--- Relational Operators ---");
        // These operators compare two values and result in a boolean (true or false).
        int score1 = 95;
        int score2 = 100;
        System.out.println("score1 > score2: " + (score1 > score2)); // Greater than
        System.out.println("score1 < score2: " + (score1 < score2)); // Less than
        System.out.println("score1 == 95: " + (score1 == 95)); // Equal to
        System.out.println("score1 != 100: " + (score1 != 100)); // Not equal to
        System.out.println("score2 >= 100: " + (score2 >= 100)); // Greater than or equal to

        System.out.println("\n--- Logical Operators ---");
        // These operators combine boolean expressions.
        boolean hasHighscore = true;
        boolean hasCompletedGame = false;

        // Logical AND (&&): true only if BOTH operands are true.
        System.out.println("Has highscore AND completed game? " + (hasHighscore && hasCompletedGame));

        // Logical OR (||): true if AT LEAST ONE operand is true.
        System.out.println("Has highscore OR completed game? " + (hasHighscore || hasCompletedGame));

        // Logical NOT (!): inverts the boolean value.
        System.out.println("NOT completed game? " + !hasCompletedGame);

        // Short-Circuiting: Logical operators && and || are "short-circuiting".
        // For `&&`, if the first operand is false, the second is never evaluated. [2,
        // 5]
        // For `||`, if the first operand is true, the second is never evaluated. [2, 5]
        // This is efficient and can prevent errors (e.g., checking for null before
        // using an object).

        // --- PART 2: FLOW CONTROL ---

        System.out.println("\n--- The if-else Ladder ---");
        int userAge = 18;

        if (userAge < 13) {
            System.out.println("You are a child.");
        } else if (userAge >= 13 && userAge < 18) { // `else if` checks another condition [4]
            System.out.println("You are a teenager.");
        } else { // The `else` block is a final catch-all if no other condition was met [9]
            System.out.println("You are an adult.");
        }

        System.out.println("\n--- Modern switch Statement (Java 14+) ---");
        // The `switch` statement is ideal for checking one variable against multiple
        // possible values. [11]
        // The modern "arrow" syntax (`->`) is cleaner and safer (prevents accidental
        // "fall-through"). [1, 15]
        String day = "WEDNESDAY";
        String dayType = switch (day.toUpperCase()) {
            case "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY" -> "Weekday";
            case "SATURDAY", "SUNDAY" -> "Weekend";
            default -> "Invalid day"; // The default case is required for exhaustive switches [10]
        };
        System.out.println(day + " is a " + dayType);

        // Pattern Matching in Switch (Java 21): Switch can now handle more than just
        // constants. [1, 18]
        // This is a powerful feature we will revisit later.

        System.out.println("\n--- Ternary Operator ---");
        // A compact, one-line if-else statement.
        // Syntax: condition ? value_if_true : value_if_false;
        int currentScore = 75;
        String resultMessage = (currentScore >= 60) ? "You passed!" : "You failed.";
        System.out.println("Your result: " + resultMessage);
    }
}