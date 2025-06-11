/**
 * @file 12_Polymorphism.java
 * @author dunamismax
 * @date 2025-06-11
 *
 * @brief Demonstrates polymorphism, one of the most powerful concepts in OOP.
 *
 *        ---
 *
 *        ## The Power of "Many Forms": Polymorphism
 *
 *        **Polymorphism** (from Greek, meaning "many forms") is the third
 *        pillar of OOP. It is the
 *        ability of an object to take on many forms. In practice, this means
 *        that a **superclass
 *        - * reference variable can point to a subclass object**. [1, 3]
 *
 *        Building on inheritance, polymorphism allows us to write extremely
 *        flexible and decoupled code.
 *        We can design programs that work with a general type (like `Animal`)
 *        without needing to
 *        know the specific details of its subtypes (like `Dog`, `Bird`, or a
 *        future `Cat` class).
 *
 *        ### The Magic Behind Polymorphism: Dynamic Method Dispatch
 *
 *        When you call an overridden method through a superclass reference,
 *        Java doesn't decide
 *        which method to run at compile time. Instead, it waits until
 *        **runtime** to see what kind
 *        of object the variable is actually pointing to, and then it calls the
 *        appropriate method
 *        for that specific object. This is called **dynamic method dispatch**
 *        or **late binding**. [6, 12, 14]
 *        It's what makes polymorphism work.
 *
 *        ### What you will learn:
 *        - The core concept of polymorphism and why it relies on the "is-a"
 *        relationship.
 *        - How to use a superclass reference to hold objects of various
 *        subclasses.
 *        - How dynamic method dispatch automatically calls the correct
 *        overridden method.
 *        - How to write generic, flexible code that can process a collection of
 *        different objects uniformly.
 *
 */

// We'll reuse our class hierarchy from the previous lesson to demonstrate
// polymorphism.

// The Superclass
class Animal {
    protected String name;

    public Animal(String name) {
        this.name = name;
    }

    // This is the general version of the method.
    public void makeSound() {
        System.out.println("The animal '" + name + "' makes a generic sound.");
    }
}

// The Dog Subclass
class Dog extends Animal {
    public Dog(String name) {
        super(name);
    }

    // The Dog's specific, overridden version of the method.
    @Override
    public void makeSound() {
        System.out.println("The dog '" + name + "' says: Woof! Woof!");
    }

    public void fetch() {
        System.out.println(name + " is fetching.");
    }
}

// The Bird Subclass
class Bird extends Animal {
    public Bird(String name) {
        super(name);
    }

    // The Bird's specific, overridden version of the method.
    @Override
    public void makeSound() {
        System.out.println("The bird '" + name + "' says: Chirp! Chirp!");
    }
}

// --- Main Application Class ---
public class Polymorphism {

    public static void main(String[] args) {

        // --- 1. Polymorphism in Action ---
        // Here, we declare a reference variable of the superclass type (`Animal`)
        // but point it to an object of the subclass type (`Dog`).
        // This is possible because a Dog "is-a"n Animal.
        Animal myPet = new Dog("Rex");

        // Now, when we call makeSound(), which version runs?
        // Dynamic method dispatch ensures the Dog's version is called because
        // the object is actually a Dog.
        System.out.println("Calling makeSound() on an Animal reference pointing to a Dog object:");
        myPet.makeSound(); // Output: The dog 'Rex' says: Woof! Woof!

        // However, the compiler only knows about the methods defined in the reference
        // type (`Animal`).
        // Therefore, you cannot call subclass-specific methods through a superclass
        // reference.
        // The following line would cause a COMPILE ERROR because `fetch()` is not in
        // the Animal class.
        // myPet.fetch(); // COMPILE ERROR!

        // --- 2. The True Power of Polymorphism: Processing Collections ---

        // Let's create an array of Animals. This array can hold ANY object that
        // is a subclass of Animal. This is where the flexibility shines.
        System.out.println("\n--- The Polymorphic Zoo ---");
        Animal[] zoo = new Animal[4];
        zoo[0] = new Dog("Buddy");
        zoo[1] = new Bird("Polly");
        zoo[2] = new Bird("Sparrow");
        zoo[3] = new Animal("Generic Creature"); // A plain Animal object

        // Now, we can loop through this array with a single, simple loop.
        // We don't need to know or care what specific type of animal is in each slot.
        // We just treat them all as `Animal`s.
        for (Animal currentAnimal : zoo) {
            // For each animal, we call the same method: makeSound().
            // Java's dynamic dispatch takes care of calling the correct
            // version (`Dog`, `Bird`, or `Animal`) for each object at runtime.
            System.out.print("Processing " + currentAnimal.name + ": ");
            currentAnimal.makeSound();
        }

        // This is incredibly powerful. If tomorrow we add a `Cat` class that extends
        // `Animal`,
        // we can add `new Cat("Whiskers")` to our `zoo` array, and this loop will
        // work perfectly without changing a single line of code! This is the essence
        // of creating flexible, maintainable, and extensible software.
    }
}