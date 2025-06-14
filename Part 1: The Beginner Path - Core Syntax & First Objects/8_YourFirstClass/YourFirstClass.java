/**
 * Welcome to Object-Oriented Programming (OOP)!
 *
 * This lesson introduces the most important concept in Java: the class.
 * A CLASS is a blueprint for creating objects. (e.g., the architectural plan
 * for a house)
 * An OBJECT is an instance of a class, with its own data. (e.g., your specific
 * house built from the plan)
 *
 * In this file, we define a `Car` blueprint and then create and use actual
 * `Car` objects.
 *
 * HOW TO RUN THIS FILE:
 * 1. Compile: javac YourFirstClass.java
 * 2. Run: java YourFirstClass
 */

// --- THE BLUEPRINT ---
// This `Car` class is our template. In real projects, this would be in its own
// `Car.java` file.
class Car {

    // --- 1. Fields (Instance Variables) ---
    // These variables store the data for each individual car object.
    // Every car we create will have its own copy of these.
    String model;
    String color;
    int year;
    double currentSpeed;

    // --- 2. Methods (Behaviors) ---
    // These methods define the actions a car object can perform.
    // They can use the object's own fields (like `model` or `currentSpeed`).

    // Increases the car's speed.
    void accelerate(double amount) {
        currentSpeed += amount;
        System.out.println(this.model + " is accelerating. Speed is now " + currentSpeed + " mph.");
    }

    // Stops the car.
    void brake() {
        currentSpeed = 0;
        System.out.println(this.model + " is braking and has stopped.");
    }

    // Displays the current state of this specific car.
    void displayInfo() {
        System.out.println("--- Car Info ---");
        System.out.println("Model: " + this.year + " " + this.color + " " + this.model);
        System.out.println("Speed: " + this.currentSpeed + " mph");
        System.out.println("----------------");
    }
}

// --- THE MAIN APPLICATION ---
// This class will create and use objects from our Car blueprint.
public class YourFirstClass {

    public static void main(String[] args) {

        // --- 3. Creating Objects (Instantiation) ---
        // We use the `new` keyword to create an actual object from the Car class.
        // `myCar` and `neighborsCar` are two separate, independent objects in memory.
        Car myCar = new Car();
        Car neighborsCar = new Car();

        // --- 4. Setting the Data for Each Object ---
        // We use the dot (.) operator to set the fields for each object.
        myCar.model = "Tesla Model S";
        myCar.color = "Red";
        myCar.year = 2024;

        neighborsCar.model = "Honda Civic";
        neighborsCar.color = "Blue";
        neighborsCar.year = 2020;

        // --- 5. Calling Methods on Each Object ---
        // Each car has its own data, so calling the same method produces different
        // results.
        System.out.println("Initial state of cars:");
        myCar.displayInfo();
        neighborsCar.displayInfo();

        System.out.println("\nTaking the cars for a drive...");
        myCar.accelerate(100.5);
        neighborsCar.accelerate(60.0);

        // Check the state again. Notice each car has its own, separate speed.
        myCar.displayInfo();
        neighborsCar.displayInfo();

        // Now, let's stop only one of the cars.
        System.out.println("\nStopping my car...");
        myCar.brake();
        myCar.displayInfo();
        neighborsCar.displayInfo(); // The neighbor's car is unaffected.
    }
}