/**
 * This lesson introduces the Java Collections Framework, a powerful library for
 * storing and managing groups of objects dynamically.
 *
 * Unlike arrays, which have a fixed size, Collections can grow and shrink as needed.
 * We will focus on two of the most common and useful collections:
 *
 * - A `List`: An ordered collection of items (allows duplicates).
 *   Think of it as a dynamic, flexible array.
 * - A `Map`: A collection of key-value pairs (keys must be unique).
 *   Perfect for lookups, like a dictionary or a phone book.
 *
 * We will also learn the best practice of "programming to the interface."
 *
 * HOW TO RUN THIS FILE:
 * 1. Compile: javac CollectionsFramework.java
 * 2. Run:     java CollectionsFramework
 */

// Import the specific interfaces and classes we will use.
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollectionsFramework {

    public static void main(String[] args) {

        // --- 1. The `List` Interface and `ArrayList` ---
        // A List is an ordered collection. `ArrayList` is the most common
        // implementation.
        // The `<String>` part uses Generics to ensure this list can only hold Strings,
        // which prevents errors at compile time.

        // Best Practice: Declare with the interface type (`List`), create with the
        // class type (`ArrayList`).
        // This makes your code more flexible if you ever need to change the
        // implementation.
        System.out.println("--- Using a List (ArrayList) for a To-Do List ---");
        List<String> todoList = new ArrayList<>();

        // Add items to the list
        todoList.add("Buy groceries");
        todoList.add("Pay bills");
        todoList.add("Walk the dog");

        System.out.println("Current tasks: " + todoList);
        System.out.println("Number of tasks: " + todoList.size());

        // Access an item by its index
        System.out.println("First task: " + todoList.get(0));

        // Remove an item
        todoList.remove("Pay bills");
        System.out.println("Tasks after completing one: " + todoList);

        System.out.println("\nIterating over the remaining tasks:");
        for (String task : todoList) {
            System.out.println("- " + task);
        }

        // --- 2. The `Map` Interface and `HashMap` ---
        // A Map stores key-value pairs. `HashMap` is the most common implementation.
        // It provides extremely fast lookups using the key. The order of items is not
        // guaranteed.
        System.out.println("\n\n--- Using a Map (HashMap) for Player Scores ---");
        // This map will store player names (String) and their scores (Integer).
        // Note: Collections must use Objects (like Integer), not primitives (like int).
        Map<String, Integer> playerScores = new HashMap<>();

        // Add key-value pairs using .put()
        playerScores.put("Alice", 95);
        playerScores.put("Bob", 82);
        playerScores.put("Charlie", 99);
        playerScores.put("Alice", 100); // Using an existing key will UPDATE the value.

        System.out.println("Player scores: " + playerScores);

        // Get a value by its key
        Integer aliceScore = playerScores.get("Alice");
        System.out.println("Alice's current score is: " + aliceScore);

        // Check if a player exists
        System.out.println("Does 'David' have a score? " + playerScores.containsKey("David"));

        // The best way to iterate over a Map is using its "entry set".
        // An entry is a single key-value pair object.
        System.out.println("\nIterating over all scores:");
        for (Map.Entry<String, Integer> entry : playerScores.entrySet()) {
            System.out.println("Player: " + entry.getKey() + " -> Score: " + entry.getValue());
        }
    }
}