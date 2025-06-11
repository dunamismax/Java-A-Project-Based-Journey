/**
 * @file 11_Inheritance.java
 * @author dunamismax
 * @date 2025-06-11
 *
 * @brief Introduces inheritance, the OOP mechanism for creating new classes
 *        from existing ones.
 *
 *        ---
 *
 *        ## Building on What Exists: Inheritance
 *
 *        **Inheritance** is another core pillar of Object-Oriented Programming.
 *        It allows a new class
 *        (called a **subclass**, **child class**, or **derived class**) to
 *        acquire the properties
 *        and methods of an existing class (called a **superclass**, **parent
 *        class**, or **base class**). [2, 5]
 *
 *        This creates an **"is-a"** relationship. For example, a `Dog` is an
 *        `Animal`. A `Car` is a `Vehicle`.
 *        The subclass inherits the state and behavior from its superclass and
 *        can then add its
 *        own unique features or modify the inherited behaviors. [8, 11]
 *
 *        ### Key Benefits of Inheritance:
 *        1. **Code Reusability:** You can write common code once in a
 *        superclass and reuse it across
 *        multiple subclasses, following the DRY (Don't Repeat Yourself)
 *        principle. [7]
 *        2. **Logical Structure:** It helps create a clear and logical
 *        hierarchy among your classes,
 *        mirroring real-world relationships.
 *        3. **Polymorphism:** Inheritance is the foundation for polymorphism
 *        (which we'll cover next),
 *        one of OOP's most powerful concepts. [11]
 *
 *        ### What you will learn:
 *        - The `extends` keyword to establish an inheritance relationship.
 *        - How a subclass inherits fields and methods from its superclass.
 *        - The `super` keyword to call the superclass's constructor and
 *        methods. [4]
 *        - The `@Override` annotation to change the behavior of an inherited
 *        method. [1]
 *
 */

// --- 1. The Superclass (Parent Class) ---
// This is a general class that defines common attributes and behaviors for all
// animals.
class Animal {
    protected String name; // `protected` means accessible by this class and its subclasses. [9]
    protected int age;

    public Animal(String name, int age) {
        System.out.println("Animal constructor called.");
        this.name = name;
        this.age = age;
    }

    public void eat() {
        System.out.println(this.name + " is eating.");
    }

    public void sleep() {
        System.out.println(this.name + " is sleeping.");
    }

    public void makeSound() {
        System.out.println("The animal makes a generic sound.");
    }
}

// --- 2. The Subclass (Child Class) ---
// A `Dog` "is-a" specific type of `Animal`. It `extends` the Animal class.
// This means a Dog automatically gets the `name`, `age`, `eat()`, and `sleep()`
// members.
class Dog extends Animal {

    private String breed; // A specific field that only Dogs have.

    // --- 3. The Subclass Constructor and `super()` ---
    public Dog(String name, int age, String breed) {
        // The first line of a subclass constructor MUST be a call to the superclass
        // constructor
        // using `super()`. This ensures the 'Animal' part of the Dog is properly
        // initialized. [4, 13]
        super(name, age);
        System.out.println("Dog constructor called.");
        this.breed = breed; // Initialize the subclass-specific field.
    }

    // --- 4. Overriding a Method ---
    // A subclass can provide its own specific implementation for an inherited
    // method. [1]
    // The `@Override` annotation is a best practice. It tells the compiler you
    // intend
    // to override a method, and it will give you an error if you make a mistake
    // (e.g., typo in the method name). [1, 10]
    @Override
    public void makeSound() {
        // We can optionally call the parent's version of the method if needed.
        // super.makeSound();
        System.out.println(this.name + " says: Woof! Woof!");
    }

    // A specific method that only Dogs have.
    public void fetch() {
        System.out.println(this.name + " the " + this.breed + " is fetching a ball.");
    }
}

class Bird extends Animal {
    private double wingSpan;

    public Bird(String name, int age, double wingSpan) {
        super(name, age);
        this.wingSpan = wingSpan;
    }

    @Override
    public void makeSound() {
        System.out.println(this.name + " says: Chirp! Chirp!");
    }

    public void fly() {
        System.out.println(this.name + " is flying with a wingspan of " + this.wingSpan + " inches.");
    }
}

// --- Main Application Class ---
public class Inheritance {
    public static void main(String[] args) {

        System.out.println("--- Creating a Dog object ---");
        // When we create a Dog, both the Animal and Dog constructors are called.
        Dog myDog = new Dog("Buddy", 5, "Golden Retriever");

        System.out.println("\n--- Interacting with the Dog object ---");
        // We can call methods inherited from the Animal class.
        myDog.eat();
        myDog.sleep();

        // We can call the Dog's overridden version of the method.
        myDog.makeSound();

        // We can call methods specific to the Dog class.
        myDog.fetch();

        System.out.println("\n-----------------------------------\n");

        System.out.println("--- Creating a Bird object ---");
        Bird myBird = new Bird("Polly", 2, 12.5);

        System.out.println("\n--- Interacting with the Bird object ---");
        // The Bird also has access to the Animal methods.
        myBird.eat();

        // It uses its own overridden version of makeSound().
        myBird.makeSound();

        // And its own specific method.
        myBird.fly();

        // Note: `myDog.fly()` or `myBird.fetch()` would be COMPILE ERRORS.
        // A Dog is not a Bird, and a Bird is not a Dog.
    }
}