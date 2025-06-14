/**
 * This lesson is a hands-on project demonstrating how to work with JSON,
 * the standard data format for web APIs and configuration files.
 *
 * We will use a popular third-party library called **Gson** to handle two key tasks:
 *
 * 1.  **Deserialization:** Converting a JSON string into a Java object.
 *     (JSON String -> Java Object)
 * 2.  **Serialization:** Converting a Java object into a JSON string.
 *     (Java Object -> JSON String)
 *
 * PREQUISITE: This project requires a build tool like Maven or Gradle with the
 * Gson library added as a dependency.
 *
 * For Maven (`pom.xml`):
 * ```xml
 * <dependency>
 *     <groupId>com.google.code.gson</groupId>
 *     <artifactId>gson</artifactId>
 *     <version>2.10.1</version>
 * </dependency>
 * ```
 *
 * HOW TO RUN THIS FILE:
 * 1. Ensure Gson is in your pom.xml.
 * 2. Compile and run through your IDE or via Maven.
 */

// These imports are from the Gson library.
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class WorkingWithJSON {

  // --- 1. Modeling the Data ---
  // We create a `record` to perfectly match the structure of our JSON data.
  // The field names (`id`, `title`, `completed`) must match the JSON keys.
  record Todo(int userId, int id, String title, boolean completed) {
  }

  public static void main(String[] args) {

    // --- 2. Deserialization (JSON String -> Java Object) ---
    // This is a sample JSON string, like one you'd get from a web API.
    String todoJson = """
        {
          "userId": 1,
          "id": 1,
          "title": "delectus aut autem",
          "completed": false
        }
        """;

    System.out.println("--- Deserialization: JSON to Java Object ---");
    System.out.println("Incoming JSON data:\n" + todoJson);

    // Create an instance of the Gson library.
    Gson gson = new Gson();

    // The `fromJson()` method is the workhorse. We provide the JSON string
    // and the class to map it to. Gson handles the conversion automatically.
    Todo myTodo = gson.fromJson(todoJson, Todo.class);

    System.out.println("\nSuccessfully parsed JSON into a Java `Todo` record!");
    System.out.println("Todo Title: " + myTodo.title());
    System.out.println("Is Completed: " + myTodo.completed());

    // --- 3. Serialization (Java Object -> JSON String) ---
    System.out.println("\n\n--- Serialization: Java Object to JSON ---");

    // Let's create a new Todo object in our Java code.
    Todo newTodo = new Todo(1, 2, "quis ut nam facilis et officia qui", false);
    System.out.println("Created a new Java object: " + newTodo);

    // To make the output JSON readable, we use a `GsonBuilder`.
    Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();

    // The `toJson()` method takes our Java object and converts it into a JSON
    // string.
    String jsonOutput = prettyGson.toJson(newTodo);

    System.out.println("\nSerialized our Java object back into a formatted JSON string:");
    System.out.println(jsonOutput);
  }
}