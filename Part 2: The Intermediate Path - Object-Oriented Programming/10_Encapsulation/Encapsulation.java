/**
 * This lesson introduces Encapsulation, a core principle of Object-Oriented
 * Programming.
 *
 * The goal of encapsulation is to "hide" an object's internal data and only
 * allow
 * access through public methods. This protects the data from accidental or
 * invalid changes.
 *
 * We do this by:
 * 1. Marking fields as `private`.
 * 2. Providing `public` methods (called "getters" and "setters") to read and
 * modify the data.
 *
 * This gives the class full control over its own state.
 *
 * HOW TO RUN THIS FILE:
 * 1. Compile: javac Encapsulation.java
 * 2. Run: java Encapsulation
 */

class PlayerProfile {

    // --- 1. Private Fields ---
    // By marking these `private`, no code outside this class can access them
    // directly.
    private String username;
    private int level;

    // --- 2. The Constructor ---
    public PlayerProfile(String username) {
        this.username = username;
        this.level = 1; // All new players start at level 1.
    }

    // --- 3. Public "Getter" Methods (Read-Only Access) ---
    // These methods safely expose the private data.
    public String getUsername() {
        return this.username;
    }

    public int getLevel() {
        return this.level;
    }

    // --- 4. Public "Setter" Method (Controlled Write Access) ---
    // Instead of allowing anyone to set the level to any value, this method
    // contains validation logic. It enforces the rules of our "game".
    public void levelUp() {
        this.level++; // Increment the level by one.
        System.out.println(this.username + " leveled up to level " + this.level + "!");
    }

    // This setter includes validation logic.
    public void setLevel(int newLevel) {
        if (newLevel > this.level) {
            System.out.println("Cheating detected! Setting level to " + newLevel);
            this.level = newLevel;
        } else {
            System.out.println("Error: Cannot set level to a value lower than the current level.");
        }
    }
}

public class Encapsulation {

    public static void main(String[] args) {
        System.out.println("--- Demonstrating Encapsulation with a Player Profile ---");

        PlayerProfile player1 = new PlayerProfile("KnightRider_99");

        // --- Direct Access Is Blocked (This is Encapsulation!) ---
        // The following line will cause a COMPILE ERROR because `level` is private.
        // You cannot tamper with the object's data from the outside.
        // player1.level = 100; // COMPILE ERROR!

        // We must use the public methods to interact with the object.
        System.out.println("Initial player state:");
        System.out.println("Username: " + player1.getUsername());
        System.out.println("Level: " + player1.getLevel());

        System.out.println("\n--- Performing actions via public methods ---");
        player1.levelUp();
        player1.levelUp();
        System.out.println("Current level is now: " + player1.getLevel());

        System.out.println("\n--- Trying to set level directly (with validation) ---");
        player1.setLevel(2); // This will fail our validation check.
        player1.setLevel(10); // This will pass.

        System.out.println("\nFinal player state:");
        System.out.println("Username: " + player1.getUsername());
        System.out.println("Level: " + player1.getLevel());
    }
}