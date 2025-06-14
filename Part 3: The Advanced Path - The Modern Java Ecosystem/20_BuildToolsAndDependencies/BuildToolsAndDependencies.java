/**
 * This lesson explains how real-world Java projects are built using tools
 * like **Maven** or **Gradle**. This file is a conceptual guide, not meant
 * to be compiled in isolation.
 *
 * ## The Problem with `javac`
 *
 * Compiling single files (`javac MyFile.java`) is fine for simple lessons.
 * Real applications have hundreds of files and rely on external libraries.
 * Managing this manually is impossible.
 *
 * ## The Solution: Build Tools
 *
 * A build tool like Maven automates the entire process:
 * 1.  **Dependency Management:** It automatically downloads and includes third-party
 *     libraries (called dependencies) that your project needs.
 * 2.  **Standard Structure:** It enforces a standard project layout, making projects
 *     easy to understand (e.g., code goes in `src/main/java`).
 * 3.  **Build Lifecycle:** It defines standard commands to compile, test, and package
 *     your application into a runnable JAR file.
 *
 * ## The `pom.xml` File
 *
 * Maven's configuration file is `pom.xml` (Project Object Model). It's the "brain"
 * of your project. Here is where you list your dependencies.
 *
 * ---
 *
 * ### EXAMPLE: Adding a JSON Library
 *
 * To work with JSON, we want to use a popular library called **Gson**.
 * Instead of finding and downloading the JAR file ourselves, we simply
 * tell Maven we need it by adding this to our `pom.xml`:
 *
 * ```xml
 * <!-- Inside the pom.xml file -->
 * <dependencies>
 *     <dependency>
 *         <groupId>com.google.code.gson</groupId>
 *         <artifactId>gson</artifactId>
 *         <version>2.10.1</version>
 *     </dependency>
 * </dependencies>
 * ```
 *
 * When you build your project (`mvn compile`), Maven reads this, downloads
 * `gson-2.10.1.jar` from the internet (from a repository called Maven Central),
 * and makes it available to your code.
 *
 * That is why the `import` statement below works when using a build tool.
 *
 */

// This import is only possible because a build tool has managed the dependency for us.
// If you tried to compile this file with `javac` alone, it would FAIL with a
// "package com.google.gson does not exist" error.
import com.google.gson.Gson;

public class BuildToolsAndDependencies {

    // A simple record to demonstrate serialization.
    record User(String name, String email) {
    }

    public static void main(String[] args) {
        System.out.println("--- Demonstrating a Third-Party Dependency (Gson) ---");

        // 1. Create a Gson object. This is only possible if the Gson library is on our
        // "classpath".
        // Maven handles this for us automatically.
        Gson gson = new Gson();
        User user = new User("Alice", "alice@example.com");

        // 2. Use the library to perform a task. Here, we convert a Java object into a
        // JSON string.
        String jsonOutput = gson.toJson(user);

        // 3. Print the result.
        System.out.println("\nOur Java User object:");
        System.out.println(user);

        System.out.println("\nSame object converted to a JSON string by the Gson library:");
        System.out.println(jsonOutput);

        System.out.println("\nSuccess! This proves our build tool correctly managed the dependency.");
    }
}