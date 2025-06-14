/**
 * This lesson explains how to define "contracts" for your classes using
 * Abstract Classes and Interfaces. These are essential tools for building
 * flexible and scalable applications.
 *
 * An ABSTRACT CLASS:
 * - Is a template for related classes (an "is-a" relationship, e.g., a Dog is
 * an Animal).
 * - Cannot be instantiated directly. It's an incomplete blueprint.
 * - Can contain both implemented methods and `abstract` methods that subclasses
 * MUST implement.
 *
 * An INTERFACE:
 * - Is a pure contract of capabilities (a "can-do" relationship, e.g., a Car
 * can be Driveable).
 * - Defines what a class can do, but not how.
 * - A class `implements` an interface, promising to provide the required
 * methods.
 *
 * HOW TO RUN THIS FILE:
 * 1. Compile: javac AbstractClassesAndInterfaces.java
 * 2. Run: java AbstractClassesAndInterfaces
 */

// --- PART 1: ABSTRACT CLASS ---
// Establishes a template for a family of related classes.
abstract class Payable {
    // An abstract class can have fields and implemented methods, just like a
    // regular class.
    public String getPayeeName() {
        return "Generic Payee";
    }

    // An ABSTRACT method has no body. It's a contract that any concrete subclass
    // MUST implement.
    public abstract double getPaymentAmount();
}

// Concrete subclass 1
class Invoice extends Payable {
    private int quantity;
    private double pricePerItem;

    public Invoice(int quantity, double pricePerItem) {
        this.quantity = quantity;
        this.pricePerItem = pricePerItem;
    }

    // We MUST implement the abstract method from the Payable class.
    @Override
    public double getPaymentAmount() {
        return quantity * pricePerItem;
    }
}

// Concrete subclass 2
class SalariedEmployee extends Payable {
    private double weeklySalary;

    public SalariedEmployee(double weeklySalary) {
        this.weeklySalary = weeklySalary;
    }

    @Override
    public String getPayeeName() {
        return "Salaried Employee";
    }

    // We MUST implement the abstract method.
    @Override
    public double getPaymentAmount() {
        return weeklySalary;
    }
}

// --- PART 2: INTERFACE ---
// Defines a capability that can be applied to completely unrelated classes.
interface Loggable {
    // An interface method is a pure contract. Any class that implements Loggable
    // must provide this method.
    String getLogMessage();
}

// This class tracks user actions. It's a Loggable thing.
class UserAction implements Loggable {
    private String username;
    private String action;

    public UserAction(String username, String action) {
        this.username = username;
        this.action = action;
    }

    @Override
    public String getLogMessage() {
        return "USER_ACTION: " + username + " performed action: " + action;
    }
}

// This class represents a system error. It's also a Loggable thing, but
// unrelated to UserAction.
class SystemEvent implements Loggable {
    private String eventMessage;

    public SystemEvent(String eventMessage) {
        this.eventMessage = eventMessage;
    }

    @Override
    public String getLogMessage() {
        return "SYSTEM_EVENT: " + this.eventMessage;
    }
}

public class AbstractClassesAndInterfaces {
    public static void main(String[] args) {

        System.out.println("--- Demonstrating Abstract Classes ---");
        // The following line is a COMPILE ERROR because you cannot create an object
        // from an abstract class.
        // Payable genericItem = new Payable();

        // We can use the abstract class as a polymorphic type to hold its concrete
        // children.
        Payable[] itemsToPay = new Payable[2];
        itemsToPay[0] = new Invoice(10, 25.50); // This is an Invoice
        itemsToPay[1] = new SalariedEmployee(1500.00); // This is a SalariedEmployee

        for (Payable item : itemsToPay) {
            // Java calls the correct getPaymentAmount() for each object.
            System.out.printf("Paying %s an amount of $%.2f%n", item.getPayeeName(), item.getPaymentAmount());
        }

        System.out.println("\n--- Demonstrating Interfaces ---");
        // Create a collection of things that share the "Loggable" capability.
        Loggable[] itemsToLog = new Loggable[2];
        itemsToLog[0] = new UserAction("Alice", "LOGIN_SUCCESS");
        itemsToLog[1] = new SystemEvent("Database connection timed out.");

        System.out.println("Processing system logs:");
        for (Loggable item : itemsToLog) {
            // We can process them all uniformly based on their shared capability.
            System.out.println("LOG: " + item.getLogMessage());
        }
    }
}