/**
 * @file 21_WorkingWithJSON.java
 * @author dunamismax
 * @date 2025-06-11
 *
 * @brief Project: Use a third-party library like Gson to parse real-world data from a JSON string into Java objects and vice-versa.
 *
 * ---
 *
 * ## The Language of the Web: JSON
 *
 * **JSON (JavaScript Object Notation)** is a lightweight, human-readable data-interchange format.
 * It has become the de facto standard for transmitting data between web servers and clients (like
 * a browser or mobile app), and is also commonly used for configuration files. [1, 2] As a Java
 * developer, working with JSON is an essential, everyday skill.
 *
 * This lesson is a hands-on project that puts the previous lesson into practice. We will use
 * the **Gson** library, managed by our build tool (Maven), to seamlessly convert between JSON
 * text and Java objects. [4]
 *
 * ### Key Concepts:
 * 1.  **Serialization (Marshalling):** The process of converting a Java object into its JSON string representation. (Java Object -> JSON String) [6]
 * 2.  **Deserialization (Unmarshalling):** The process of parsing a JSON string and converting it into an equivalent Java object. (JSON String -> Java Object) [6]
 *
 * ### Prerequisites:
 * This code assumes you are using a build tool like Maven or Gradle and have added a JSON
 * library as a dependency in your `pom.xml` or `build.gradle` file.
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
 * ### What you will learn:
 * - How to model JSON data using Java records or classes.
 * - How to use a library like Gson to deserialize JSON into Java objects.
 * - How to serialize Java objects into a nicely formatted JSON string.
 *
 */

// These imports are from the Gson library we added as a dependency.
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

// --- 1. Modeling the Data ---
// We use records to create simple, immutable classes that perfectly match the structure
// of our JSON data. The field names in the record (`street`, `city`, etc.) must match
// the key names in the JSON object for automatic mapping to work.

record Address(String street, String suite, String city, String zipcode) {
}

record User(int id, String name, String username, String email, Address address) {
}

public class WorkingWithJSON {

    public static void main(String[] args) {

        // --- 2. Deserialization (JSON String -> Java Object) ---

        // This is a sample JSON string, similar to what you might get from a web API.
        // Note the nested `address` object.
        String userJson = """
                {
                  "id": 1,
                  "name": "Leanne Graham",
                  "username": "Bret",
                  "email": "Sincere@april.biz",
                  "address": {
                    "street": "Kulas Light",
                    "suite": "Apt. 556",
                    "city": "Gwenborough",
                    "zipcode": "92998-3874"
                  }
                }
                """;

        System.out.println("--- 1. Deserialization Demo ---");
        System.out.println("Incoming JSON data:\n" + userJson);

        // Create an instance of the Gson parser.
        Gson gson = new Gson();

        try {
            // `fromJson()` is the magic method. We give it the JSON string and the
            // class we want to map it to. Gson handles the rest.
            User user = gson.fromJson(userJson, User.class);

            System.out.println("\nSuccessfully deserialized JSON into a Java User object!");
            // Now we can work with it as a normal Java object.
            System.out.println("User ID: " + user.id());
            System.out.println("Username: " + user.username());
            // We can even access the nested object's properties.
            System.out.println("User's City: " + user.address().city());

        } catch (JsonSyntaxException e) {
            System.err.println("Error: The provided JSON string is malformed. " + e.getMessage());
        }

        // --- 3. Serialization (Java Object -> JSON String) ---
        System.out.println("\n\n--- 2. Serialization Demo ---");

        // Let's create a new User object in Java.
        Address newAddress = new Address("Innovation Drive", "Building 7", "Tech City", "13370");
        User newUser = new User(101, "Jane Doe", "jdoe", "jane.doe@example.com", newAddress);

        System.out.println("Created a new User object in Java: " + newUser);

        // We can use a `GsonBuilder` to create a Gson instance that produces
        // nicely formatted ("pretty printed") JSON output.
        Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();

        // `toJson()` is the serialization method. It takes a Java object
        // and returns its JSON string representation.
        String jsonOutput = prettyGson.toJson(newUser);

        System.out.println("\nSerialized Java object into JSON string:\n" + jsonOutput);
    }
}