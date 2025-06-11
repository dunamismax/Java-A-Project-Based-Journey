/**
 * @file 10_Encapsulation.java
 * @author dunamismax
 * @date 2025-06-11
 *
 * @brief Introduces encapsulation, a core pillar of OOP for building robust and
 *        secure classes.
 *
 *        ---
 *
 *        ## The Principle of Data Hiding: Encapsulation
 *
 *        **Encapsulation** is one of the four fundamental principles of
 *        Object-Oriented Programming (OOP).
 *        It refers to the practice of bundling an object's data (fields) and
 *        the methods that
 *        operate on that data into a single unit (the class). More importantly,
 *        it involves
 *        **hiding** the internal state of an object from the outside world. [2,
 *        12, 16]
 *
 *        Instead of making fields `public` and allowing anyone to change them,
 *        we declare them
 *        as `private`. We then provide controlled, `public` methods (known as
 *        **getters** and
 *        **setters**) to interact with that data. [3]
 *
 *        ### Why is Encapsulation so important?
 *        1. **Control & Validation:** By forcing access through methods, we can
 *        add logic to
 *        prevent the object from being put into an invalid state. For example,
 *        a bank
 *        balance should never be set to a negative number. [4]
 *        2. **Security & Integrity:** It protects the object's internal state
 *        from accidental
 *        or malicious modification. The `BankAccount` object itself is
 *        responsible for
 *        maintaining its own balance, not the outside code. [1]
 *        3. **Flexibility & Maintainability:** It allows you to change the
 *        internal implementation
 *        of a class without breaking the code that uses it. For example, you
 *        could change
 *        how `balance` is stored internally, and as long as the public methods
 *        work the
 *        same, no other part of the application needs to change. [2, 16]
 *
 *        ### What you will learn:
 *        - How to use the `private` access modifier to hide data.
 *        - The purpose of "getter" (accessor) methods for providing read-only
 *        access.
 *        - The purpose of "setter" (mutator) methods for providing controlled
 *        write access with validation.
 *        - Why encapsulation is crucial for creating robust, secure, and
 *        maintainable software.
 *
 */

// --- The Encapsulated Blueprint ---
class BankAccount {

    // --- 1. Private Instance Variables ---
    // These fields are hidden from the outside world. They can ONLY be accessed
    // by the code inside the BankAccount class itself.
    private String accountNumber;
    private String accountHolderName;
    private double balance;

    // --- 2. The Constructor ---
    // The constructor still initializes the object. We can include validation here,
    // too.
    public BankAccount(String accountNumber, String accountHolderName, double initialDeposit) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        // Ensure the initial balance is not negative.
        if (initialDeposit >= 0) {
            this.balance = initialDeposit;
        } else {
            this.balance = 0;
            System.out.println("Error: Initial deposit cannot be negative. Balance set to 0.");
        }
    }

    // --- 3. Public "Getter" Methods (Accessors) ---
    // These methods provide read-only access to the private fields.

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public String getAccountHolderName() {
        return this.accountHolderName;
    }

    public double getBalance() {
        return this.balance;
    }

    // --- 4. Public "Mutator" Methods with Validation ---
    // We don't provide a direct `setBalance()` method. That would defeat the
    // purpose.
    // Instead, we provide methods that represent real-world actions and contain
    // logic.

    /**
     * @brief Deposits a specified amount into the account.
     * @param amount The amount to deposit. Must be a positive value.
     */
    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
            System.out.printf("Successfully deposited $%.2f. New balance: $%.2f%n", amount, this.balance);
        } else {
            System.out.println("Error: Deposit amount must be positive.");
        }
    }

    /**
     * @brief Withdraws a specified amount from the account.
     * @param amount The amount to withdraw. Must be positive and not exceed the
     *               current balance.
     */
    public void withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Error: Withdrawal amount must be positive.");
        } else if (amount > this.balance) {
            System.out.printf("Error: Insufficient funds. Cannot withdraw $%.2f from a balance of $%.2f%n", amount,
                    this.balance);
        } else {
            this.balance -= amount;
            System.out.printf("Successfully withdrew $%.2f. New balance: $%.2f%n", amount, this.balance);
        }
    }
}

// --- Main Application Class ---
public class Encapsulation {

    public static void main(String[] args) {
        System.out.println("--- Demonstrating Encapsulation with a Bank Account ---");

        // Create a new BankAccount instance.
        BankAccount myAccount = new BankAccount("12345-67890", "John Doe", 500.00);

        // --- Direct Access is Blocked ---
        // The following lines will cause a COMPILE ERROR because the fields are
        // private.
        // This is the core of data hiding. You cannot tamper with the state directly.
        // myAccount.balance = 1000000; // COMPILE ERROR!
        // System.out.println(myAccount.accountHolderName); // COMPILE ERROR!

        System.out.println("\n--- Initial Account State ---");
        // We must use the public getter methods to access the data.
        System.out.println("Account Holder: " + myAccount.getAccountHolderName());
        System.out.println("Account Number: " + myAccount.getAccountNumber());
        System.out.printf("Current Balance: $%.2f%n", myAccount.getBalance());

        System.out.println("\n--- Performing Transactions ---");
        // Now, we interact with the object through its public methods.
        myAccount.deposit(250.75);
        myAccount.withdraw(100.00);

        System.out.println("\n--- Testing Validation Logic ---");
        myAccount.deposit(-50.00); // This will fail due to our validation.
        myAccount.withdraw(1000.00); // This will fail due to insufficient funds.

        System.out.println("\n--- Final Account State ---");
        // The object has maintained its own integrity thanks to encapsulation.
        System.out.printf("Final Balance: $%.2f%n", myAccount.getBalance());
    }
}