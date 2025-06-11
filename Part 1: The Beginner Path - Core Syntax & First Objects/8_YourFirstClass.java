/**
 * @file 8_YourFirstClass.java
 * @author dunamismax
 * @date 2025-06-11
 *
 * @brief THE BIG LEAP: From procedural code to Object-Oriented Programming
 *        (OOP) by creating a custom class.
 *
 *        ---
 *
 *        ## The Object-Oriented Leap: Creating Blueprints
 *
 *        Until now, we have been writing code in a "procedural" style inside
 *        the static `main`
 *        method. This is great for simple scripts, but for building complex
 *        applications, we
 *        need a better way to structure our code. Welcome to **Object-Oriented
 *        Programming (OOP)**.
 *
 *        The core idea of OOP is to model real-world things (or concepts) as
 *        **objects**. An object
 *        bundles its own data and behavior together.
 *
 *        - A **Class** is the **blueprint** or template for creating objects.
 *        It defines the properties
 *        (data) and actions (methods) that all objects of that type will have.
 *        Think of it as a cookie cutter. [1, 2]
 *        - An **Object** is a specific **instance** of a class. It's the actual
 *        cookie you create
 *        using the cookie cutter. You can create many objects from a single
 *        class, each with its
 *        own unique data. [1, 2, 7]
 *
 *        ### What you will learn:
 *        - The fundamental difference between a class and an object.
 *        - How to define a class with **instance variables** (fields) to hold
 *        an object's state. [9, 13]
 *        - How to define **instance methods** to represent an object's
 *        behavior. [9]
 *        - How to create objects (instances) from your class using the `new`
 *        keyword. [1]
 *        - How to use the dot operator (`.`) to access an object's fields and
 *        methods. [4]
 *
 *        ### Static vs. Instance
 *        - `static` members (like our previous methods) belong to the **class
 *        itself**.
 *        - **Instance** members (what we are creating now) belong to each
 *        individual **object**.
 *        Every `Car` object will have its OWN `color` and its OWN
 *        `currentSpeed`.
 *
 */

// --- The Blueprint: The `Car` Class ---
// Note: For simplicity, we are defining this class in the same file. In larger
// projects,
// each public class gets its own `.java` file. A single file can only have one
// public class.
class Car {

    // --- 1. Instance Variables (Fields or Properties) ---
    // These variables define the "state" of a Car object. Each Car object
    // we create will get its own separate set of these variables.
    String model;
    String color;
    int year;
    double currentSpeed; // a default value of 0.0 is assigned

    // --- 2. Instance Methods (Behaviors or Actions) ---
    // These methods define what a Car object can "do". They can access and
    // modify the instance variables of the object they belong to.

    /**
     * @brief A simple behavior that simulates starting the car's engine.
     */
    void startEngine() {
        System.out.println("The " + color + " " + model + "'s engine has started.");
    }

    /**
     * @brief Increases the car's current speed.
     * @param amount The speed to increase by (in mph).
     */
    void accelerate(double amount) {
        currentSpeed += amount; // same as currentSpeed = currentSpeed + amount;
        System.out.println("Accelerating... Current speed is now " + currentSpeed + " mph.");
    }

    /**
     * @brief Resets the car's speed to 0.
     */
    void brake() {
        currentSpeed = 0;
        System.out.println("Braking... The car has stopped.");
    }

    /**
     * @brief Displays all the information about this specific car instance.
     *        Note how it uses the object's own instance variables.
     */
    void displayInfo() {
        System.out.println("--- Car Details ---");
        System.out.println("Model: " + model);
        System.out.println("Color: " + color);
        System.out.println("Year: " + year);
        System.out.println("Current Speed: " + currentSpeed + " mph");
        System.out.println("-------------------");
    }
}

// --- The Main Application Class ---
// This class contains our `main` method, which will create and use objects of
// our new `Car` class.
public class YourFirstClass {

    public static void main(String[] args) {

        System.out.println("Welcome to the Object-Oriented world!");

        // --- 3. Instantiation: Creating Objects from the Class ---
        // We use the `new` keyword followed by the class name to create an instance (an
        // object).
        // `myCar` is a variable that holds a reference to our newly created Car object
        // in memory.
        Car myCar = new Car();

        // Let's create another Car object. This object is completely separate from
        // `myCar`.
        Car neighborCar = new Car();

        // --- 4. Setting the State (Using Dot Notation) ---
        // We use the dot operator `.` to access the instance variables of a specific
        // object.
        System.out.println("\nSetting the state of myCar...");
        myCar.model = "Mustang";
        myCar.color = "Red";
        myCar.year = 2023;

        System.out.println("Setting the state of neighborCar...");
        neighborCar.model = "Civic";
        neighborCar.color = "Blue";
        neighborCar.year = 2021;

        // --- 5. Calling Methods (Using Dot Notation) ---
        // We use the dot operator `.` to call the methods on a specific object.
        // Let's see the initial state of both cars.
        myCar.displayInfo();
        neighborCar.displayInfo(); // Notice how each object has its own data.

        // Now let's perform some actions.
        System.out.println("\n--- Performing Actions ---");
        myCar.startEngine();
        myCar.accelerate(75.5);

        neighborCar.startEngine();
        neighborCar.accelerate(40);

        // Let's check the state again.
        System.out.println("\n--- Final State ---");
        myCar.displayInfo(); // Speed has changed.
        neighborCar.displayInfo(); // Speed has changed, but is different from myCar.

        // And finally, let's brake myCar.
        myCar.brake();
        myCar.displayInfo();
    }
}