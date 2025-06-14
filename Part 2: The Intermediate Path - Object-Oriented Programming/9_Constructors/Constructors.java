/**
 * This lesson introduces constructors, special methods for creating and
 * initializing objects correctly and efficiently.
 *
 * A constructor ensures an object is created in a valid state, preventing
 * errors
 * from incomplete setup. Instead of creating an empty object and setting fields
 * manually,
 * you provide the required data right when you create it.
 *
 * Key Rules for Constructors:
 * - The method name must be IDENTICAL to the class name.
 * - They have NO return type, not even `void`.
 *
 * HOW TO RUN THIS FILE:
 * 1. Compile: javac Constructors.java
 * 2. Run: java Constructors
 */

// In this example, we'll create a `User` class.
class User {
    // Instance Fields
    String username;
    String email;
    boolean isActive;

    // --- 1. The Main Constructor ---
    // This is the primary way to create a new User. It requires all essential data.
    public User(String username, String email) {
        System.out.println("Creating a new user: " + username);

        // The `this` keyword refers to the current object being created.
        // It's used here to distinguish the field (`this.username`) from the
        // parameter (`username`) when they share the same name.
        this.username = username;
        this.email = email;
        this.isActive = true; // Set a default value for a new user.
    }

    // --- 2. Constructor Overloading & Chaining ---
    // This provides a second, simpler way to create a User.
    public User(String username) {
        // `this(...)` calls another constructor in the same class. This is called
        // "constructor chaining" and is used to avoid duplicating code.
        // It MUST be the very first line in the constructor.
        this(username, "no-email-provided@example.com"); // Reuses the main constructor.
    }

    // A method to display this user's information.
    public void displayInfo() {
        String status = this.isActive ? "Active" : "Inactive";
        System.out.println("User: " + this.username + " | Email: " + this.email + " | Status: " + status);
        System.out.println("----------------------------------------------------------");
    }
}

public class Constructors {
    public static void main(String[] args) {
        System.out.println("--- Using Constructors to Build Objects ---\n");

        // 1. Create a user with the main constructor.
        // The object is created and initialized in a single, safe step.
        User adminUser = new User("admin", "admin@system.io");
        adminUser.displayInfo();

        // 2. Create a user with the overloaded constructor.
        User guestUser = new User("guest");
        guestUser.displayInfo(); // Note the email is the default provided by the constructor.

        // --- Why this is better ---
        // If we remove the `User(String username)` constructor from the User class,
        // the line `new User("guest")` would cause a compiler error.
        // Constructors enforce that objects are created correctly, making code more
        // robust.

        // Similarly, because we defined our own constructors, the default "no-argument"
        // constructor is gone. The following line would cause a compiler error:
        // User brokenUser = new User(); // COMPILE ERROR: No such constructor exists.
    }
}