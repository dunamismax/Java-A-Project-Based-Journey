# Java: A Project-Based Journey

[![Language: Java](https://img.shields.io/badge/Language-Java-orange.svg)](https://www.java.com)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat-square)](http://makeapullrequest.com)
[![Stars](https://img.shields.io/github/stars/dunamismax/Java-A-Project-Based-Journey?style=social)](https://github.com/dunamismax/Java-A-Project-Based-Journey/stargazers)

Welcome to your ultimate journey to mastering Java! This open-source curriculum is designed to guide you from your very first line of code to building a complete, database-connected web API. We believe in learning by *doing*, and this repository is your hands-on guide.

> Learning to code is like building a house. You don't start with the roof. You start with a solid foundation. This course is designed to be that foundation, laid one brick—one concept, one project—at a time.

---

## ✨ Why This Journey?

This isn't just a collection of code snippets. It's a structured learning path designed for deep, practical understanding of modern Java.

*   🧠 **Object-Oriented From Day One**: We introduce Objects and Classes early, building a strong OOP foundation which is essential for writing effective Java.
*   🚀 **Zero to Web Developer**: 30 carefully ordered lessons guide you from core syntax and the JVM to building a multithreaded database application and a live web API.
*   🛠️ **Build a Real-World Portfolio**: You won't just learn concepts; you'll apply them immediately by building practical tools like a data analyzer, a database app, and a JSON parser.
*   💪 **Master the Java Ecosystem**: We embrace the tools of modern Java development. You will learn the Collections Framework, Lambda Expressions, Streams, and even how to manage dependencies with Maven or Gradle.
*   🌍 **Open Source & Community Driven**: This curriculum is for the community, by the community. Contributions, suggestions, and corrections are always welcome.

---

## 🚀 Getting Started

All you need to begin your journey is a Java Development Kit (JDK) and a desire to build amazing things.

### Prerequisites

*   A **Java Development Kit (JDK)**, version 17 or higher is recommended. You can get one from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [Eclipse Adoptium](https://adoptium.net/).
*   A text editor or IDE (like VS Code, IntelliJ IDEA, or Eclipse).
*   A command-line terminal.
*   (For later lessons) A build tool like [Apache Maven](https://maven.apache.org/) is recommended.

### How to Use This Repository

1.  **Clone the repository:**
    ```sh
    git clone https://github.com/dunamismax/Java-A-Project-Based-Journey.git
    cd Java-A-Project-Based-Journey
    ```

2.  **Start with Lesson 1:** Each program is a self-contained `.java` file. Open `HelloWorld.java` and read through the heavily documented code.

3.  **Compile and Run:** Use the Java compiler (`javac`) to create a `.class` file, then use the Java runtime (`java`) to execute it.

    ```sh
    # Compile the .java source file
    javac HelloWorld.java

    # Run the compiled .class file (note: no .class extension)
    java HelloWorld
    ```

4.  **Proceed to the next lesson and enjoy the journey!**

---

## 📚 The Journey

The curriculum is divided into four distinct paths, each building on the last to take you from core concepts to professional-level skills.

### Part 1: The Beginner Path - Core Syntax & First Objects

| File | Key Concepts | Description |
| :--- | :--- | :--- |
| `1_HelloWorld.java` | `public class`, `main()`, `System.out.println()` | The essential first step: compiling and running a basic Java program. |
| `2_VariablesAndPrimitives.java` | `int`, `double`, `boolean`, `char`, strong typing | Learn to store, manage, and display information with Java's primitive types. |
| `3_UserInputAndStrings.java` | `Scanner`, `String` class, `.equals()`, concatenation | Make programs interactive and master Java's powerful `String` object. |
| `4_OperatorsAndFlowControl.java` | `+`, `/`, `==`, `&&`, `if-else`, `switch` | Perform calculations and give your program a brain to make logical decisions. |
| `5_Loops.java` | `for`, `while`, `do-while`, enhanced for-each loop | Teach your program to perform repetitive tasks on collections of data. |
| `6_Arrays.java` | `int[]`, `String[]`, `.length`, array iteration | Manage collections of fixed-size data. |
| `7_Methods.java` | Method definition, parameters, return types | Organize code into clean, reusable, and modular blocks. |
| `8_YourFirstClass.java` | `class`, instance variables, object instantiation | **The OOP Leap:** Create your first custom blueprint for creating objects. |

### Part 2: The Intermediate Path - Object-Oriented Programming

| File | Key Concepts | Description |
| :--- | :--- | :--- |
| `9_Constructors.java` | `new` keyword, `this`, constructor overloading | Master the proper way to create and initialize your objects. |
| `10_Encapsulation.java` | `private`, getters/setters, data hiding | Protect your object's state and create robust, maintainable code. |
| `11_Inheritance.java` | `extends`, `super`, `@Override`, "is-a" relationship | Build new classes on top of existing ones to create hierarchies of objects. |
| `12_Polymorphism.java` | Method overriding, dynamic dispatch | Unlock one of OOP's most powerful features for writing flexible, decoupled code. |
| `13_AbstractClassesAndInterfaces.java` | `abstract`, `interface`, `implements` | Define contracts and templates that your classes must follow. |
| `14_CollectionsFramework.java` | `ArrayList`, `HashMap`, Generics (`<>`) | Move beyond arrays to use Java's powerful, dynamic collection library. |
| `15_ExceptionHandling.java` | `try-catch-finally`, `throws`, checked vs. unchecked | Write resilient code that can gracefully handle errors and unexpected events. |

### Part 3: The Advanced Path - The Modern Java Ecosystem

| File | Key Concepts | Description |
| :--- | :--- | :--- |
| `16_FileIO.java` | `java.nio.file`, `Paths`, `Files`, `try-with-resources` | Persist data by reading from and writing to files using the modern Java API. |
| `17_EnumsAndRecords.java` | `enum`, `record`, immutable data carriers | Learn to create powerful, type-safe enumerations and simple, modern data classes. |
| `18_LambdaExpressions.java` | `(x) -> x * x`, functional interfaces | **Embrace Functional Java:** Write concise, powerful, and expressive anonymous functions. |
| `19_StreamsAPI.java` | `.stream()`, `.filter()`, `.map()`, `.collect()` | **Project:** Use the Streams API to perform complex data analysis on a collection. |
| `20_BuildToolsAndDependencies.java` | Maven (`pom.xml`), dependencies, `mvn compile` | Learn how real-world Java projects are built and how to include third-party libraries. |
| `21_WorkingWithJSON.java` | **Project:** Use a library like GSON or Jackson | Parse real-world data from a JSON file into your Java objects. |
| `22_UnitTesting.java` | JUnit 5, `@Test`, assertions (`assertEquals`) | Learn the professional discipline of writing tests to ensure your code is correct. |

### Part 4: The Expert Path - Concurrency, Databases & The Web

This is where you build professional-grade applications. These projects challenge you to handle multiple operations at once, talk to databases, and serve data over the internet.

| File | Key Concepts | Description |
| :--- | :--- | :--- |
| `23_BasicMultithreading.java` | `Thread` class, `Runnable` interface | Learn the fundamentals of making your program do multiple things at once. |
| `24_ConcurrencyTools.java` | `ExecutorService`, `synchronized`, `AtomicInteger` | Prevent race conditions and manage thread pools like a professional. |
| `25_SimpleSocketClientServer.java`| `Socket`, `ServerSocket`, `InputStream` | **Your Gateway to the Internet:** Build a client-server chat application in Java. |
| `26_JDBC_DatabaseApp.java` | **Project:** JDBC, SQL, `PreparedStatement` | **Connect to a Database:** Build an app that performs CRUD operations on a database. |
| `27_LoggingInPractice.java` | SLF4J & Logback, log levels (`INFO`, `DEBUG`) | Move beyond `System.out.println` to use a professional logging framework. |
| `28_SimpleWebAPI.java` | **Project:** SparkJava/Javalin, `GET`/`POST` requests | **Become a Backend Dev:** Build a simple REST API that can serve data over HTTP. |
| `29_PuttingItAllTogether.java` | **Final Capstone Project** | A database-backed web API with unit tests and logging. |
| `30_ExploringTheFuture.java` | A guide to next steps (`Spring Boot`, `Quarkus`, etc.) | A final document pointing you towards professional frameworks and future learning. |

---

## ⭐ Show Your Support

If this journey helps you on your path to mastering Java, please **give this repository a star!** It helps the project reach more learners and encourages us to keep creating great content.

## 🤝 Contributing

Contributions are the lifeblood of the open-source community. Any contributions you make are **greatly appreciated**.

*   **Reporting Bugs**: Find a bug in the code or a typo in the comments? Please [open an issue](https://github.com/dunamismax/Java-A-Project-Based-Journey/issues).
*   **Suggesting Enhancements**: Have an idea for a new lesson or a better way to explain a concept? Feel free to open an issue to discuss it.
*   **Pull Requests**: If you want to contribute directly, please fork the repo and create a pull request.

## 📜 License

This project is licensed under the MIT License. See the `LICENSE` file for more details.
