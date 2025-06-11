/**
 * @file 9_Constructors.java
 * @author dunamismax
 * @date 2025-06-11
 *
 * @brief Explores constructors, the special methods for creating and
 *        initializing objects properly.
 *
 *        ---
 *
 *        ## The Proper Way to Build Objects: Constructors
 *
 *        In the last lesson, we created a `Car` object and then manually set
 *        its fields one by one.
 *        This works, but it has two major problems:
 *        1. It's repetitive and clutters our code.
 *        2. It allows an object to exist in an incomplete or invalid state.
 *        What if we forgot
 *        to set the `model`? Our car would be a `null` model, which could cause
 *        errors later.
 *
 *        A **constructor** solves this problem. A constructor is a special
 *        method that is
 *        automatically called when you create an instance of a class with the
 *        `new` keyword. Its
 *        primary job is to initialize the object's state, ensuring it's born in
 *        a valid and
 *        usable condition. [2, 11]
 *
 *        ### What you will learn:
 *        - How to define a constructor to initialize an object's state upon
 *        creation.
 *        - The purpose of the `this` keyword to refer to the current object
 *        instance. [1, 5, 6]
 *        - What happens to the "default constructor" when you write your own.
 *        - **Constructor Overloading**: Providing multiple ways to create an
 *        object. [9]
 *
 *        ### Rules for Constructors:
 *        - A constructor's name must be **exactly the same** as the class name.
 *        [7]
 *        - A constructor **cannot have a return type**, not even `void`. [7]
 *
 */

// --- The Blueprint, now with Constructors ---
class Vehicle {

    // Instance Variables
    String model;
    String color;
    int year;
    double currentSpeed;

    // --- 1. The Main Constructor ---
    // This constructor requires all the essential data to create a valid Vehicle.
    // Notice the name `Vehicle` matches the class name, and there's no return type.
    public Vehicle(String model, String color, int year) {
        System.out.println("Main constructor called: Creating a " + year + " " + color + " " + model);

        // --- 2. The `this` Keyword ---
        // `this` is a reference to the current object being created. [1]
        // We use it here to distinguish between the instance variables (`this.model`)
        // and the constructor parameters (`model`) which have the same name. [5, 6]
        this.model = model;
        this.color = color;
        this.year = year;
        this.currentSpeed = 0; // It's good practice to explicitly initialize all fields.
    }

    // --- 3. Constructor Overloading ---
    // We can have multiple constructors with different parameter lists. This
    // provides
    // flexibility in how objects are created. [9, 13]

    /**
     * A second constructor for creating a Vehicle with a default color.
     */
    public Vehicle(String model, int year) {
        // Instead of repeating the logic, we can call another constructor from this one
        // using `this()`. This is called "constructor chaining" and is a best practice.
        // [3, 12]
        // The call to `this()` MUST be the very first statement in the constructor. [3]
        this(model, "Black", year); // Calls the main constructor with a default color.
    }

    /**
     * A third, "no-argument" (or no-arg) constructor to create a default vehicle.
     * If you define ANY constructor, Java no longer provides the invisible default
     * one. [4]
     * If you still want to be able to call `new Vehicle()`, you must define it
     * yourself.
     */
    public Vehicle() {
        this("Unknown Model", "White", 2025); // Chain to the main constructor with default values.
        System.out.println("No-argument constructor called: A default vehicle was created.");
    }

    // Instance methods (Behaviors)
    void displayInfo() {
        System.out.println("--- Vehicle Info ---");
        System.out.println("Model: " + this.model); // `this` is optional here, but can improve clarity.
        System.out.println("Color: " + this.color);
        System.out.println("Year: " + this.year);
        System.out.println("--------------------");
    }
}

// --- Main Application Class ---
public class Constructors {

    public static void main(String[] args) {

        System.out.println("--- Creating Objects Using Constructors ---\n");

        // Create a sports car using the main constructor.
        // The object is created and initialized in a single, clean line.
        // It is guaranteed to have a model, color, and year from the moment it exists.
        Vehicle sportsCar = new Vehicle("Corvette", "Velocity Yellow", 2024);
        sportsCar.displayInfo();

        // Create a truck using the overloaded constructor that has a default color.
        Vehicle truck = new Vehicle("F-150", 2023);
        truck.displayInfo(); // Notice the color is "Black" as set by the chained constructor.

        // Create a "mystery" car using the no-argument constructor.
        Vehicle mysteryCar = new Vehicle();
        mysteryCar.displayInfo();

        // --- Why this is safer ---
        // In the previous lesson, we could do `Car myCar = new Car();` and then forget
        // to
        // set the fields. Now, that's controlled.
        // If we remove the `public Vehicle() {}` no-arg constructor from the Vehicle
        // class,
        // the line `Vehicle defaultCar = new Vehicle();` would cause a COMPILE ERROR.
        // The compiler enforces that we provide the necessary data, making our code
        // more robust.
    }
}