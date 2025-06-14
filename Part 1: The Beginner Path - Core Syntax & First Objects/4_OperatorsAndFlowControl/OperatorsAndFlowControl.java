/**
 * This lesson covers operators and flow control, the fundamental tools
 * for making decisions and performing calculations in your code.
 *
 * Operators are symbols that perform operations like addition (+) or comparison
 * (>).
 * Flow control statements like `if` and `switch` use these operations to direct
 * the execution path of your program.
 *
 * HOW TO RUN THIS FILE:
 * 1. Compile: javac OperatorsAndFlowControl.java
 * 2. Run: java OperatorsAndFlowControl
 */
public class OperatorsAndFlowControl {

    public static void main(String[] args) {

        // --- PART 1: OPERATORS ---

        // Arithmetic operators perform calculations.
        int score = 100;
        int bonus = 15;
        System.out.println("Final Score: " + (score + bonus)); // Addition

        int items = 10;
        int people = 3;
        System.out.println("Items per person: " + (items / people)); // Integer division (result is 3)
        System.out.println("Items left over: " + (items % people)); // Modulo/remainder (result is 1)

        // --- PART 2: FLOW CONTROL (MAKING DECISIONS) ---

        System.out.println("\n--- The if-else Statement ---");
        int userAge = 20;
        boolean hasLicense = true;

        // `if` checks a condition. Logical AND `&&` requires both to be true.
        if (userAge >= 18 && hasLicense) {
            System.out.println("You are eligible to drive.");
        } else if (userAge < 18) {
            System.out.println("You are too young to drive.");
        } else {
            // This 'else' block is the catch-all for any other case.
            System.out.println("You are old enough, but you need a license.");
        }

        System.out.println("\n--- The modern `switch` Statement ---");
        // A `switch` is perfect for checking one variable against multiple possible
        // values.
        // The modern arrow syntax `->` (Java 14+) is clean and recommended.
        String userRole = "ADMIN";
        String permissions = switch (userRole) {
            case "ADMIN" -> "Full Access";
            case "EDITOR" -> "Can write content";
            case "VIEWER" -> "Read-only access";
            default -> "Unknown Role"; // A `default` case handles all other values.
        };
        System.out.println("Your permissions: " + permissions);

        System.out.println("\n--- The Ternary Operator ---");
        // A compact, one-line shortcut for a simple if-else assignment.
        // Syntax: condition ? value_if_true : value_if_false;
        int balance = 500;
        String accountStatus = (balance > 0) ? "Active" : "Inactive";
        System.out.println("Account Status: " + accountStatus);
    }
}