/**
 * @file 14_CollectionsFramework.java
 * @author dunamismax
 * @date 2025-06-11
 *
 * @brief Moves beyond arrays to use Java's powerful, dynamic collection library.
 *
 * ---
 *
 * ## Beyond Arrays: The Java Collections Framework
 *
 * Arrays are useful, but they have a major limitation: they are fixed in size. You can't
 * easily add or remove elements. The **Java Collections Framework (JCF)** is the solution.
 * It's a rich library of classes and interfaces that provide powerful and optimized ways to
 * store and manipulate groups of objects. [1, 2]
 *
 * ### Key Advantages over Arrays:
 * - **Dynamic Size:** Collections can grow or shrink as needed.
 * - **High Performance:** Provides highly optimized implementations of common data structures
 *   (linked lists, hash maps, trees, etc.).
 * - **Reduces Effort:** Comes with built-in algorithms for sorting, searching, reversing, etc.
 *
 * ### What you will learn:
 * - **Generics (`<>`):** The mechanism for creating type-safe collections. [5]
 * - The three core collection interfaces: `List`, `Set`, and `Map`. [14, 19]
 * - **`ArrayList`**: The most common implementation of a dynamic, ordered list.
 * - **`HashMap`**: The most common implementation for storing key-value pairs.
 * - **Best Practice**: The importance of "programming to the interface". [4]
 *
 */

// Import the specific interfaces and classes we will use.
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectionsFramework {

    public static void main(String[] args) {

        // --- Introducing Generics (`<>`) ---
        // The angle brackets `<>` are used to specify the type of objects the
        // collection will hold.
        // This is called using "Generics". It provides compile-time type safety,
        // meaning the
        // compiler will give you an error if you try to add an incompatible type. [5,
        // 9]
        // This prevents nasty `ClassCastException` errors at runtime.

        // --- 1. The `List` Interface and `ArrayList` ---
        // A List is an ordered collection of elements that allows duplicates. [6]
        // You can access elements by their integer index, just like with arrays.
        // `ArrayList` is the most common implementation of the `List` interface.

        // **Best Practice: Program to the Interface**
        // Notice we declare the variable as `List`, but create an `ArrayList`. [4]
        // This makes our code more flexible. We could easily swap `ArrayList` for
        // another
        // `List` implementation (like `LinkedList`) without changing the rest of the
        // code. [4, 7]
        System.out.println("--- The List: ArrayList ---");
        List<String> fruits = new ArrayList<>();

        // Add elements to the list
        fruits.add("Apple");
        fruits.add("Banana");
        fruits.add("Cherry");
        fruits.add("Banana"); // Duplicates are allowed

        System.out.println("Fruits in the list: " + fruits);
        System.out.println("Number of fruits: " + fruits.size());

        // Get an element by its index
        String firstFruit = fruits.get(0);
        System.out.println("The first fruit is: " + firstFruit);

        // Check if an element exists
        System.out.println("Does the list contain 'Cherry'? " + fruits.contains("Cherry"));

        // Iterate through the list using a for-each loop
        System.out.println("\nIterating through fruits:");
        for (String fruit : fruits) {
            System.out.println("- " + fruit);
        }

        // Remove an element
        fruits.remove("Banana"); // Removes the first occurrence
        System.out.println("\nList after removing one 'Banana': " + fruits);

        // --- 2. The `Map` Interface and `HashMap` ---
        // A Map stores data as key-value pairs. Each key must be unique. [10, 15]
        // It's incredibly fast for looking up a value if you know its key.
        // `HashMap` is the most common implementation of `Map`. It does not guarantee
        // order. [10]

        System.out.println("\n\n--- The Map: HashMap ---");
        // This map will store student names (String keys) and their exam scores
        // (Integer values).
        // Note: Collections can only store objects, not primitives. Java automatically
        // converts
        // the `int` to an `Integer` object. This is called "autoboxing".
        Map<String, Integer> studentScores = new HashMap<>();

        // Add key-value pairs using put()
        studentScores.put("Alice", 95);
        studentScores.put("Bob", 82);
        studentScores.put("Charlie", 99);
        studentScores.put("Alice", 98); // Putting with an existing key will UPDATE the value.

        System.out.println("Student scores: " + studentScores);

        // Get a value by its key
        int aliceScore = studentScores.get("Alice");
        System.out.println("Alice's score is: " + aliceScore);

        // Check if a key exists
        System.out.println("Does the map contain a score for 'David'? " + studentScores.containsKey("David"));

        // The most efficient way to iterate over a map is using its `entrySet`.
        // An entry is a single key-value pair.
        System.out.println("\nIterating through student scores:");
        for (Map.Entry<String, Integer> entry : studentScores.entrySet()) {
            String studentName = entry.getKey();
            Integer score = entry.getValue();
            System.out.println(studentName + " scored " + score);
        }
    }
}