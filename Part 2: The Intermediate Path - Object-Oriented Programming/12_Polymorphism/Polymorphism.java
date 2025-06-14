/**
 * This lesson demonstrates Polymorphism, one of the most powerful features of
 * OOP.
 * Polymorphism means "many forms." In Java, it's the ability of a parent class
 * reference to hold a child class object.
 *
 * This allows us to write flexible, generic code that works with a family of
 * objects
 * without needing to know the specific type of each one.
 *
 * We will see how Java automatically calls the correct method for an object at
 * runtime,
 * a process called "dynamic method dispatch."
 *
 * HOW TO RUN THIS FILE:
 * 1. Compile: javac Polymorphism.java
 * 2. Run: java Polymorphism
 */

// --- The Superclass (Parent) ---
// This defines a generic "Shape" with a "draw" behavior.
class Shape {
    public void draw() {
        System.out.println("Drawing a generic shape.");
    }
}

// --- Subclasses (Children) ---
// Each subclass provides its own specific version of the `draw` method.

class Circle extends Shape {
    @Override
    public void draw() {
        System.out.println("Drawing a circle: O");
    }
}

class Square extends Shape {
    @Override
    public void draw() {
        System.out.println("Drawing a square: []");
    }
}

public class Polymorphism {

    public static void main(String[] args) {

        // --- 1. The Core of Polymorphism ---
        // A `Shape` reference can hold a `Circle` object because a Circle "is-a" Shape.
        Shape myShape = new Circle();

        // When we call draw(), which version runs?
        // Java looks at the *actual object* (a Circle) at runtime and calls its method.
        myShape.draw(); // Prints "Drawing a circle: O"

        // Let's point the SAME reference to a DIFFERENT object.
        myShape = new Square();
        myShape.draw(); // Now it calls the Square's method.

        // --- 2. The Real Power: Processing Collections ---
        // We can create an array of `Shape` that holds various kinds of shapes.
        // This is incredibly flexible!
        System.out.println("\n--- Drawing all shapes in our collection ---");
        Shape[] allShapes = new Shape[4];
        allShapes[0] = new Circle();
        allShapes[1] = new Square();
        allShapes[2] = new Square();
        allShapes[3] = new Circle();

        // Now, we can process them all with a single, simple loop.
        // We don't need to check `if (shape is a Circle)` or `if (shape is a Square)`.
        for (Shape currentShape : allShapes) {
            // Java's dynamic dispatch automatically calls the correct `draw()` method
            // for whatever object `currentShape` is currently pointing to.
            currentShape.draw();
        }

        // This is the magic of polymorphism. If we create a new `Triangle` class
        // tomorrow, we could add it to the `allShapes` array and this loop would
        // still work perfectly without any changes.
    }
}