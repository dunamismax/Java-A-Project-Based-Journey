/**
 * @file 17_EnumsAndRecords.java
 * @author dunamismax
 * @date 2025-06-11
 *
 * @brief Introduces two powerful, modern Java features for creating type-safe sets of constants (Enums)
 *        and immutable data carrier objects (Records).
 * ---
 *
 * ## Modern, Expressive Data Types
 *
 * Java has evolved to include language features that make code more readable, safer, and
 * less boilerplate-heavy. `enum` and `record` are two prime examples.
 *
 * An **Enum** (enumeration) is a special class that represents a fixed set of constant values.
 * It's the perfect tool when you have a variable that can only take one of a small, predefined
 * set of possible values (e.g., days of the week, status codes, playing card suits). They provide
 * type safety and are far superior to using simple Strings or integers. [1, 3]
 *
 * A **Record** (introduced as a standard feature in Java 16) is a concise syntax for declaring
 * classes that are simple, immutable data carriers. It drastically reduces boilerplate code by
 * automatically generating constructors, getters, `equals()`, `hashCode()`, and `toString()` methods. [6, 11]
 *
 * ### What you will learn:
 * - How to declare and use simple and complex `enum` types.
 * - The power of using enums in `switch` statements for clean logic.
 * - How to create immutable data objects with a single line using `record`.
 * - The benefits of conciseness and immutability that records provide.
 * - How records work with modern features like Pattern Matching (Java 21). [9]
 */

// --- PART 1: ENUMS ---

/**
 * A simple enum representing the status of an order.
 * By convention, enum constants are in all-caps.
 */
enum Status {
    PENDING,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED
}

/**
 * A more complex enum. Enums are full-fledged classes; they can have fields,
 * constructors, and methods.
 */
enum Planet {
    // The list of enum constants must be the first thing declared.
    // The constructor is called for each constant here.
    MERCURY(3.303e+23, 2.4397e6),
    VENUS(4.869e+24, 6.0518e6),
    EARTH(5.976e+24, 6.37814e6),
    MARS(6.421e+23, 3.3972e6);

    // Fields for each enum constant
    private final double mass; // in kilograms
    private final double radius; // in meters

    // The constructor for an enum must be private or package-private.
    Planet(double mass, double radius) {
        this.mass = mass;
        this.radius = radius;
    }

    // A method available on each enum constant
    public double surfaceGravity() {
        final double G = 6.67300E-11;
        return G * mass / (radius * radius);
    }
}

// --- PART 2: RECORDS ---

/**
 * A record to represent a User. This single line is a complete, immutable data
 * carrier class.
 * The compiler automatically generates:
 * - A private final field for each component (`id`, `username`).
 * - A canonical constructor that takes all components.
 * - Public "accessor" methods for each component (`id()`, `username()`).
 * - Implementations of `toString()`, `equals()`, and `hashCode()`.
 */
record User(int id, String username) {
    // Records can have additional members, like static factory methods or more
    // complex constructors for validation.
}

public class EnumsAndRecords {

    public static void main(String[] args) {

        // --- Using a simple Enum ---
        System.out.println("--- ENUM DEMO ---");
        Status currentStatus = Status.SHIPPED;
        processStatus(currentStatus);

        currentStatus = Status.DELIVERED;
        processStatus(currentStatus);

        // --- Using a complex Enum ---
        System.out.println("\n--- Complex Enum Demo ---");
        Planet home = Planet.EARTH;
        System.out.printf("The surface gravity of %s is %.2f m/s^2.%n", home, home.surfaceGravity());

        System.out.println("\nAll planets in our enum:");
        // The `values()` method returns an array of all enum constants.
        for (Planet p : Planet.values()) {
            System.out.printf("- %s%n", p);
        }

        // --- Using a Record ---
        System.out.println("\n\n--- RECORD DEMO ---");
        User user1 = new User(101, "alex_d");
        User user2 = new User(102, "beta_user");
        User user1_copy = new User(101, "alex_d");

        // Accessing data using the generated accessor methods
        System.out.println("User 1 ID: " + user1.id());
        System.out.println("User 1 Username: " + user1.username());

        // The automatically generated toString() is clean and informative.
        System.out.println("User 2 details: " + user2);

        // The automatically generated equals() correctly compares the data inside the
        // objects.
        System.out.println("Is user1 equal to user2? " + user1.equals(user2)); // false
        System.out.println("Is user1 equal to its copy? " + user1.equals(user1_copy)); // true

        // Immutability: The following line would cause a COMPILE ERROR. Record fields
        // are final.
        // user1.id = 200; // COMPILE ERROR!

        // --- Records and Pattern Matching (Java 21) ---
        System.out.println("\n--- Records and Pattern Matching ---");
        Object obj = new User(205, "charlie_g");

        // Pattern matching for `instanceof` deconstructs the record for you.
        // If obj is a User, its components are automatically extracted into the `id`
        // and `name` variables.
        if (obj instanceof User(int id, String name)) {
            System.out.printf("Pattern matching successful! Deconstructed User: ID=%d, Name=%s%n", id, name);
        }
    }

    public static void processStatus(Status status) {
        // Enums are perfect for switch statements. The compiler can even warn you
        // if you haven't handled all the possible enum constants.
        switch (status) {
            case PENDING:
                System.out.println("The order is waiting for approval.");
                break;
            case PROCESSING:
                System.out.println("The order is being prepared.");
                break;
            case SHIPPED:
                System.out.println("The order is on its way to you!");
                break;
            case DELIVERED:
                System.out.println("The order has been delivered. Enjoy!");
                break;
            case CANCELLED:
                System.out.println("The order has been cancelled.");
                break;
            default:
                // A good practice, though often not needed if all cases are covered.
                System.out.println("Unknown status.");
                break;
        }
    }
}