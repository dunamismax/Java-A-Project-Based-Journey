/**
 * @file 13_AbstractClassesAndInterfaces.java
 * @author dunamismax
 * @date 2025-06-11
 *
 * @brief Defines contracts and templates using Abstract Classes and Interfaces.
 *
 * ---
 *
 * ## Defining Contracts: Abstract Classes and Interfaces
 *
 * Sometimes, we want to create a class hierarchy where a base class defines a general concept
 * but is too abstract to be instantiated itself. For example, what would it mean to create a
 * generic "Shape" object? What would its area be?
 *
 * To handle this, Java provides two powerful mechanisms for defining "contracts" that other
 * classes must follow: **abstract classes** and **interfaces**. They allow you to enforce
 * that certain methods must be implemented by subclasses, forming the basis for powerful,
 * pluggable, and polymorphic designs. [3, 4]
 *
 * ### What you will learn:
 * - **Abstract Classes**: How to create a class that cannot be instantiated and is meant to be a base for subclasses.
 * - **Abstract Methods**: How to declare a method without a body, forcing subclasses to provide the implementation.
 * - **Interfaces**: How to define a pure contract of behaviors that any class can promise to implement.
 * - The key differences between `extends` (for classes) and `implements` (for interfaces).
 * - When to choose an abstract class versus an interface. [12, 17]
 *
 */

// --- PART 1: ABSTRACT CLASSES ---
// An abstract class establishes an "is-a" relationship.
// It can provide both abstract methods (contracts) and concrete methods (shared code).

/**
 * @brief A blueprint for any shape. It's `abstract` because a generic "Shape"
 *        cannot exist on its own; it must be a specific type like a Circle or
 *        Square.
 */
abstract class Shape {
    protected String name;

    public Shape(String name) {
        this.name = name;
    }

    // A CONCRETE method: This has an implementation and is inherited by all
    // subclasses.
    public void displayInfo() {
        System.out.println("This is a shape named: " + this.name);
    }

    // An ABSTRACT method: It has no body (no `{}`). It's a contract.
    // Any non-abstract class that extends Shape MUST provide an implementation for
    // this method. [1, 2]
    public abstract double calculateArea();
}

// Concrete subclass 1
class Circle extends Shape {
    private double radius;

    public Circle(String name, double radius) {
        super(name);
        this.radius = radius;
    }

    // We MUST implement the abstract method from the parent.
    @Override
    public double calculateArea() {
        return Math.PI * radius * radius;
    }
}

// Concrete subclass 2
class Rectangle extends Shape {
    private double width;
    private double height;

    public Rectangle(String name, double width, double height) {
        super(name);
        this.width = width;
        this.height = height;
    }

    // We MUST implement the abstract method from the parent.
    @Override
    public double calculateArea() {
        return width * height;
    }
}

// --- PART 2: INTERFACES ---
// An interface defines a "can-do" capability. A class `implements` an interface
// to promise it has that capability. It's a pure 100% abstract contract
// (pre-Java 8).

/**
 * @brief Defines a contract for any object that can be "drawable" on a screen.
 *        An interface only specifies what a class should do, not how.
 */
interface Drawable {
    // All methods in an interface are `public abstract` by default. [9, 11]
    void draw();

    // All fields in an interface are `public static final` by default (constants).
    String RENDER_ENGINE = "OpenGL";

    // Since Java 8, interfaces can have `default` methods with implementations.
    // This allows adding new functionality to interfaces without breaking existing
    // classes. [18]
    default void getRenderingEngine() {
        System.out.println("Drawing with engine: " + RENDER_ENGINE);
    }
}

// A `Player` is not a `Drawable`, but it "can-do" drawing. It has the Drawable
// capability.
class Player implements Drawable {
    private String playerName;

    public Player(String name) {
        this.playerName = name;
    }

    // This class MUST implement the `draw` method from the Drawable interface.
    @Override
    public void draw() {
        System.out.println("Drawing player: " + playerName);
    }
}

// A `Button` is also a completely unrelated object that can be drawn.
class Button implements Drawable {
    private String label;

    public Button(String label) {
        this.label = label;
    }

    @Override
    public void draw() {
        System.out.println("Drawing a UI button with label: '" + label + "'");
    }
}

// --- Main Application Class ---
public class AbstractClassesAndInterfaces {
    public static void main(String[] args) {

        System.out.println("--- DEMONSTRATING ABSTRACT CLASSES ---");
        // The following line would be a COMPILE ERROR. You cannot instantiate an
        // abstract class. [1]
        // Shape genericShape = new Shape("generic");

        // We can, however, use it as a polymorphic type.
        Shape myCircle = new Circle("My Circle", 10.0);
        Shape myRectangle = new Rectangle("My Rectangle", 4.0, 5.0);

        myCircle.displayInfo(); // Calling a concrete method from the abstract parent.
        System.out.printf("Area of %s is %.2f%n", myCircle.name, myCircle.calculateArea());

        myRectangle.displayInfo();
        System.out.printf("Area of %s is %.2f%n", myRectangle.name, myRectangle.calculateArea());

        System.out.println("\n\n--- DEMONSTRATING INTERFACES ---");
        // Create objects that have the "Drawable" capability.
        Drawable player = new Player("Hero");
        Drawable loginButton = new Button("Login");

        // We can create a collection of things that are all Drawable.
        Drawable[] gameObjects = { player, loginButton };

        // We can now process them all uniformly based on their shared capability.
        for (Drawable obj : gameObjects) {
            obj.draw(); // Calls the specific implementation for Player or Button.
            obj.getRenderingEngine(); // Calls the default method from the interface.
        }

        /*
         * --- WHEN TO USE WHICH? A QUICK GUIDE ---
         *
         * Use an ABSTRACT CLASS when:
         * - You have a strong "is-a" relationship between classes.
         * - You want to share code (concrete methods) and state (fields) among closely
         * related classes.
         * - You need to have non-public members (`private`, `protected`).
         * - A class can only EXTEND ONE abstract class.
         *
         * Use an INTERFACE when:
         * - You have a "can-do" or "has-a" capability that can apply to unrelated
         * classes.
         * - You want to define a contract for behavior without dictating
         * implementation.
         * - You need a class to inherit from multiple sources (a class can IMPLEMENT
         * MANY interfaces).
         * - You want to achieve a high degree of decoupling between components.
         */
    }
}