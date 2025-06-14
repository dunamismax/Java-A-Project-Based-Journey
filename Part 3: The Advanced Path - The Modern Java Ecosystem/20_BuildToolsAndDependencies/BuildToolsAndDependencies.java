/**
 * @file 20_BuildToolsAndDependencies.java
 * @author dunamismax
 * @date 2025-06-11
 *
 * @brief Explains how real-world Java projects are built and how to include third-party libraries using a build tool like Maven.
 *
 * ---
 *
 * ## Beyond `javac`: Real-World Project Management
 *
 * So far, we've compiled single Java files using `javac HelloWorld.java`. This is fine for
 * small programs, but it quickly becomes unmanageable for real applications which may have:
 * - Hundreds or thousands of source files.
 * - A need for third-party libraries (called **dependencies**).
 * - A structured process for compiling, testing, and packaging the code.
 *
 * This is where **Build Automation Tools** come in. These tools manage the entire lifecycle
 * of a project for you. The two most popular build tools in the Java ecosystem are
 * **Apache Maven** and **Gradle**. [1, 2] This lesson focuses on Maven.
 *
 * ### What is Maven?
 * Maven is a powerful project management tool that standardizes and automates the build process.
 * It is built around the concept of a **Project Object Model (POM)**, which is defined in
 * a file named `pom.xml`. This file is the heart of a Maven project. [3, 4]
 *
 * ### Core Responsibilities of a Build Tool:
 * 1.  **Dependency Management:** Automatically downloads and manages external libraries (JAR files) your project needs. [5]
 * 2.  **Build Lifecycle:** Defines a standard sequence of steps like `compile`, `test`, and `package` (create a runnable JAR).
 * 3.  **Project Structure:** Enforces a standard directory layout, making projects easy to navigate ("convention over configuration").
 *
 * ### What you will learn:
 * - The purpose of a build tool like Maven.
 * - The structure of a `pom.xml` file.
 * - How to declare a **dependency** to include a third-party library.
 * - The concept of a central repository (Maven Central) where libraries are stored. [8]
 *
 * ---
 *
 * ## A Practical Example: Adding a JSON Library
 *
 * Let's say our next task is to parse a JSON file. We could write a JSON parser from scratch,
 * but that's a lot of work. A much better approach is to use a pre-existing, well-tested library.
 * **Gson** from Google is a popular choice for this. [12]
 *
 * ### 1. Declaring the Dependency in `pom.xml`
 *
 * To tell Maven we need Gson, we would add a `<dependency>` block inside the `<dependencies>`
 * section of our `pom.xml` file. It looks like this:
 *
 * ```xml
 * <dependencies>
 *     <dependency>
 *         <groupId>com.google.code.gson</groupId>
 *         <artifactId>gson</artifactId>
 *         <version>2.10.1</version>
 *     </dependency>
 *     <!-- Other dependencies would go here -->
 * </dependencies>
 * ```
 *
 * - **`<groupId>`**: Usually the reverse domain name of the organization that created the library.
 * - **`<artifactId>`**: The unique name of the library itself.
 * - **`<version>`**: The specific version of the library to use.
 *
 * These three values are the "coordinates" that uniquely identify the library in the world.
 *
 * ### 2. How Maven Gets the File
 *
 * When you build your project (e.g., by running `mvn compile`), Maven reads your `pom.xml`.
 * It sees the Gson dependency, connects to a massive online database called **Maven Central Repository**,
 * downloads the specified `gson-2.10.1.jar` file, and saves it in a local cache on your computer
 * (in a `.m2` directory in your user home). [8]
 *
 * Now, when it compiles your code, it automatically makes that JAR file available to `javac`.
 * This is why the `import` statement in the code below works!
 *
 * ### 3. The Standard Directory Structure
 *
 * Maven expects your files to be in a standard layout:
 * - `src/main/java`: For your main Java source code.
 * - `src/test/java`: For your unit test code.
 * - `pom.xml`: In the root directory of the project.
 *
 * By following this convention, Maven knows exactly where to find everything without any extra configuration.
 *
 */

// This import statement is only possible because a build tool like Maven
// has downloaded the Gson library and made it available on the project's "classpath".
// Without Maven or Gradle, this line would cause a "package com.google.gson does not exist" error.
import com.google.gson.Gson;

public class BuildToolsAndDependencies {

    public static void main(String[] args) {
        System.out.println("--- Build Tools & Dependencies ---");
        System.out.println("This program demonstrates that a third-party library can be used.");

        try {
            // We can now create an instance of the `Gson` class from the external library.
            // This proves that Maven successfully managed our dependency.
            Gson gson = new Gson();

            System.out.println("\nSuccessfully instantiated a Gson object!");
            System.out.println("Gson object's class: " + gson.getClass().getName());
            System.out.println(
                    "This means Maven has correctly downloaded the 'gson.jar' and added it to our project's classpath.");
            System.out.println("\nWe are now ready to use this library to work with JSON in the next lesson!");

        } catch (NoClassDefFoundError e) {
            // This catch block is for educational purposes. If you run this file directly
            // with `java`, you'll get this error because the Gson library isn't available.
            // It only works when run through the build tool (e.g., `mvn exec:java`).
            System.err.println("\nERROR: NoClassDefFoundError!");
            System.err.println("This error occurs because the Gson class was not found at runtime.");
            System.err.println("It proves that just having the `import` statement is not enough.");
            System.err.println(
                    "You MUST use a build tool like Maven or Gradle to manage the project's dependencies and classpath.");
        }
    }
}