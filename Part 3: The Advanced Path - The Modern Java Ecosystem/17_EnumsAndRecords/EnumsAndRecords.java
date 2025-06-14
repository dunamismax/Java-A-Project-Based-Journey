/**
 * This lesson introduces two powerful, modern Java features for creating more
 * expressive and safer data types: Enums and Records.
 *
 * An ENUM (Enumeration) is a type-safe class that represents a fixed set of
 * constants.
 * It's perfect for things like days of the week, user roles, or product
 * categories.
 * Using enums prevents errors from using invalid String or integer constants.
 *
 * A RECORD is a concise way to create an immutable data carrier class.
 * Java automatically generates the constructor, getters, `toString()`,
 * `equals()`,
 * and `hashCode()` methods, saving you from writing tons of boilerplate code.
 *
 * HOW TO RUN THIS FILE:
 * 1. Compile: javac EnumsAndRecords.java
 * 2. Run: java EnumsAndRecords
 */

// --- 1. The Enum: A Type-Safe Set of Constants ---
// This enum represents product categories. Enums can also have fields and
// methods,
// making them much more powerful than simple constants.
enum ProductCategory {
    ELECTRONICS("High-Value Shipping"),
    APPAREL("Standard Shipping"),
    GROCERY("Refrigerated Shipping");

    // Enums can have fields.
    private final String shippingMethod;

    // The constructor for an enum is private by default.
    ProductCategory(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    // And they can have methods.
    public String getShippingMethod() {
        return this.shippingMethod;
    }
}

// --- 2. The Record: A Hassle-Free, Immutable Data Object ---
// This single line declares a complete `Product` class.
// The compiler provides a constructor, `name()`, `price()`, and `category()`
// methods,
// as well as `toString()`, `equals()`, and `hashCode()`.
record Product(String name, double price, ProductCategory category) {
    // Records are immutable by default (their fields are `final`).
    // This makes them inherently thread-safe and predictable.
}

public class EnumsAndRecords {

    public static void main(String[] args) {

        System.out.println("--- Creating Objects with Enums and Records ---");
        // Create two products using our new, concise record class.
        Product laptop = new Product("Laptop Pro", 1299.99, ProductCategory.ELECTRONICS);
        Product tShirt = new Product("Java Logo T-Shirt", 19.99, ProductCategory.APPAREL);

        // --- 3. Using the Record ---
        // The auto-generated `toString()` method gives a clean output.
        System.out.println("\nProduct 1: " + laptop);
        System.out.println("Product 2: " + tShirt);

        // Access data using the auto-generated "accessor" methods.
        System.out.println("\nThe laptop's price is: $" + laptop.price());
        // Immutability: The following line would be a COMPILE ERROR because record
        // fields are final.
        // laptop.price = 999.99; // ERROR!

        // --- 4. Using the Enum ---
        // We can access the enum constant and its methods from the product object.
        System.out.println("\nShipping method for the laptop: " + laptop.category().getShippingMethod());

        // Enums are perfect for `switch` statements, making the logic clear and safe.
        System.out.println("Processing shipping for the T-Shirt:");
        switch (tShirt.category()) {
            case APPAREL:
                System.out.println("-> Placed in a standard poly mailer.");
                break;
            case ELECTRONICS:
                System.out.println("-> Placed in a shock-proof box with fragile sticker.");
                break;
            case GROCERY:
                System.out.println("-> Placed in a refrigerated container.");
                break;
        }

        // --- 5. Pattern Matching with Records (Java 21+) ---
        // Modern Java allows you to deconstruct a record cleanly inside an `instanceof`
        // check.
        System.out.println("\n--- Pattern Matching Demo ---");
        Object someObject = laptop;
        if (someObject instanceof Product(String name, double price, ProductCategory cat)) {
            // If `someObject` is a Product, its data is automatically extracted into
            // the `name`, `price`, and `cat` variables.
            System.out.printf("Deconstructed Product: Name='%s', Price=%.2f, Category=%s%n", name, price, cat);
        }
    }
}