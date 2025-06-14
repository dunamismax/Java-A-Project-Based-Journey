/**
 * This lesson introduces Inheritance, the mechanism for creating a new class
 * based on an existing one. It models an "is-a" relationship (e.g., a Car
 * "is-a" Vehicle).
 *
 * Why use Inheritance?
 * - Code Reusability: Write common code once in a parent class (superclass).
 * - Organization: Creates a logical hierarchy between related classes.
 *
 * We will use three key features:
 * - `extends`: The keyword to create a subclass from a superclass.
 * - `super()`: The way a subclass calls its parent's constructor.
 * - `@Override`: How a subclass provides its own version of an inherited
 * method.
 *
 * HOW TO RUN THIS FILE:
 * 1. Compile: javac Inheritance.java
 * 2. Run: java Inheritance
 */

// --- 1. The Superclass (Parent) ---
// This class contains fields and methods common to ALL vehicles.
class Vehicle {
    // `protected` means accessible by this class and any of its subclasses.
    protected String brand;
    protected int year;

    public Vehicle(String brand, int year) {
        System.out.println("-> Vehicle constructor is running...");
        this.brand = brand;
        this.year = year;
    }

    public void startEngine() {
        System.out.println("The " + this.brand + "'s engine starts.");
    }
}

// --- 2. The Subclass (Child) ---
// A Car `extends` Vehicle. It automatically inherits `brand`, `year`, and
// `startEngine()`.
class Car extends Vehicle {
    // Add a field specific only to Cars.
    private int numberOfDoors;

    // --- 3. The Subclass Constructor ---
    public Car(String brand, int year, int numberOfDoors) {
        // The first line MUST be a call to the parent constructor using `super()`.
        // This ensures the "Vehicle" part of the Car is built first.
        super(brand, year);
        System.out.println("--> Car constructor is running...");
        this.numberOfDoors = numberOfDoors;
    }

    // --- 4. Overriding a Method ---
    // A Car can provide its own specific version of an inherited method.
    // The `@Override` annotation tells the compiler we intend to do this,
    // which helps catch errors like typos in the method name.
    @Override
    public void startEngine() {
        System.out.println("The " + this.year + " " + this.brand + " car starts with a push button.");
    }

    public void displayCarInfo() {
        System.out.println("Car Info: " + this.year + " " + this.brand + " with " + this.numberOfDoors + " doors.");
    }
}

public class Inheritance {
    public static void main(String[] args) {

        System.out.println("--- Creating a Car object ---");
        // Note the order of constructor messages in the console.
        Car myCar = new Car("Toyota", 2023, 4);

        System.out.println("\n--- Interacting with the Car ---");

        // Calling the Car's overridden version of the method.
        myCar.startEngine();

        // Calling the Car's own specific method.
        myCar.displayCarInfo();

        // You can access the public/protected fields from the superclass.
        System.out.println("The car's brand is: " + myCar.brand);
    }
}