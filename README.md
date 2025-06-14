<div align="center">
  <img src="https://e2ehiring.com/_next/image?url=https%3A%2F%2Fe2ehiring-cms-assets.s3.ap-south-1.amazonaws.com%2FLogo_6_1ededc4ca8.png&w=3840&q=75" alt="Java Journey Banner" width="800"/>
  <h1>⭐ Java: A Project-Based Journey ⭐</h1>
  <p>
    <b>A comprehensive, open-source curriculum that guides you from "Hello, World" to building and deploying production-grade, cloud-native microservices.</b>
  </p>
  
  <p>
    <a href="https://www.oracle.com/java/technologies/downloads/"><img src="https://img.shields.io/badge/Java-17+-orange.svg" alt="Java 17+"></a>
    <a href="https://github.com/dunamismax/Java-A-Project-Based-Journey/blob/main/LICENSE"><img src="https://img.shields.io/badge/License-MIT-yellow.svg" alt="License: MIT"></a>
    <a href="https://github.com/dunamismax/Java-A-Project-Based-Journey/pulls"><img src="https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat-square" alt="PRs Welcome"></a>
    <a href="https://github.com/dunamismax/Java-A-Project-Based-Journey/stargazers"><img src="https://img.shields.io/github/stars/dunamismax/Java-A-Project-Based-Journey?style=social" alt="GitHub stars"></a>
  </p>
</div>

---

Welcome to your ultimate journey to mastering modern Java! This isn't just a collection of code snippets; it's a meticulously designed, project-based curriculum that takes you from the absolute basics of the language to the advanced skills required for professional backend development. We believe in learning by *doing*, and this repository is your hands-on guide.

> Learning to code is like building a house. You don't start with the roof. You start with a solid foundation. This course is designed to be that foundation, laid one brick—one concept, one project—at a time.

## ✨ Why This Journey?

*   🧠 **From Syntax to Services**: A comprehensive curriculum of over 60 lessons that guides you from core Java syntax to building and deploying distributed microservices.
*   🚀 **Zero to Cloud-Native**: Go far beyond a simple web app. You will learn the entire lifecycle of a modern application, including API security, database migrations, containerization with Docker, and even concepts for Kubernetes.
*   🛠️ **Build a Professional Portfolio**: You won't just learn concepts; you'll apply them immediately by building practical projects, culminating in a fully-realized, database-backed, secure REST API.
*   💪 **Master the Enterprise Standard**: We embrace the tools of modern software engineering. You will master the **Spring Boot** framework, the de facto standard for enterprise Java, alongside essential tools like **Maven**, **Git**, and **Docker**.
*   🌍 **Open Source & Community Driven**: This curriculum is for the community, by the community. Contributions, suggestions, and corrections are always welcome.

---

## 🚀 Getting Started

All you need to begin is a modern Java Development Kit (JDK), a good IDE, and a desire to build amazing things.

### Prerequisites

*   A **Java Development Kit (JDK)**, version 17 or higher. We recommend [Eclipse Adoptium](https://adoptium.net/).
*   A modern IDE like **IntelliJ IDEA Community** or **VS Code** with the Java Extension Pack.
*   **Git** for cloning the repository.
*   **Docker Desktop** (required for lessons in Part 5 & 6).

### How to Use This Repository

1.  **Clone the repository:**
    ```sh
    git clone https://github.com/dunamismax/Java-A-Project-Based-Journey.git
    ```

2.  **Open the entire project in your IDE:**
    This is a Maven project. Your IDE will automatically detect the `pom.xml` file, download the necessary dependencies, and set up the project structure.
    *   **IntelliJ:** Use `File > Open` and select the cloned `Java-A-Project-Based-Journey` folder.
    *   **VS Code:** Simply open the project folder and ensure the Java extensions are installed.

3.  **Navigate and Run:**
    *   **For early lessons (Part 1-4):** Each lesson is a self-contained `.java` file with a `main` method. Right-click on the file in your IDE and select "Run" to see the output.
    *   **For advanced lessons (Part 5+):** These are Spring Boot applications. Find the main application class (e.g., `IntroToSpringBoot.java`) and run its `main` method to start the server.

---

## 📚 The Journey

The curriculum is divided into six distinct paths, each building on the last to take you from core concepts to professional-level skills.

<details>
<summary><strong>Part 1: The Beginner Path - Core Syntax & First Objects</strong></summary>
<br>

Master the absolute fundamentals of the Java language, from variables and loops to your first custom class.

| Lesson                        | Key Concepts                                    |
| :---------------------------- | :---------------------------------------------- |
| `HelloWorld.java`             | The `main` method, `System.out.println()`, compiling & running. |
| `VariablesAndPrimitives.java` | `int`, `double`, `boolean`, `String`, static typing. |
| `Loops.java` & `FlowControl.java` | `if-else`, `switch`, `for`, `while` loops.      |
| `Arrays.java` & `Methods.java`  | Storing data in lists and organizing code into reusable blocks. |
| `YourFirstClass.java`         | **The OOP Leap:** Creating your first object blueprint. |

</details>

<details>
<summary><strong>Part 2: The Intermediate Path - Object-Oriented Programming</strong></summary>
<br>

Dive deep into the four pillars of OOP, learning to write code that is modular, flexible, and robust.

| Lesson                              | Key Concepts                                    |
| :---------------------------------- | :---------------------------------------------- |
| `Constructors.java` & `Encapsulation.java` | Creating objects properly and protecting their data. |
| `Inheritance.java` & `Polymorphism.java`   | Building class hierarchies and writing flexible code. |
| `AbstractClassesAndInterfaces.java` | Defining contracts and templates for your classes. |
| `CollectionsFramework.java`         | Using `ArrayList` and `HashMap` for dynamic data. |
| `ExceptionHandling.java`            | Writing resilient code that can gracefully handle errors. |

</details>

<details>
<summary><strong>Part 3: The Advanced Path - The Modern Java Ecosystem</strong></summary>
<br>

Move beyond core syntax to embrace the tools and libraries of modern Java development.

| Lesson                             | Key Concepts                                    |
| :--------------------------------- | :---------------------------------------------- |
| `EnumsAndRecords.java`             | Creating type-safe constants and immutable data carriers. |
| `LambdaExpressions.java` & `StreamsAPI.java` | **Functional Java:** Writing declarative, expressive data-processing pipelines. |
| `BuildToolsAndDependencies.java`   | Understanding **Maven** and the `pom.xml` file. |
| `WorkingWithJSON.java`             | Using the **Gson** library to parse and create JSON. |
| `UnitTesting.java`                 | Writing automated tests with **JUnit 5**.       |

</details>

<details>
<summary><strong>Part 4: The Expert Path - Concurrency, Databases & The Web</strong></summary>
<br>

Build your first fully functional, professional-grade applications.

| Lesson                                  | Key Concepts                                    |
| :-------------------------------------- | :---------------------------------------------- |
| `BasicMultithreading.java` & `ConcurrencyTools.java` | `Thread`, `ExecutorService`, `synchronized`, `AtomicInteger`. |
| `SimpleSocketClientServer.java`         | Low-level networking with `Socket` and `ServerSocket`. |
| `JDBC_DatabaseApp.java`                 | Connecting to a database with raw JDBC.         |
| `LoggingInPractice.java`                | Professional logging with **SLF4J & Logback**.  |
| `SimpleWebAPI.java` & `PuttingItAllTogether.java` | **Project:** Build a database-backed REST API with **Javalin**. |

</details>

<details>
<summary><strong>Part 5: The Enterprise Path - Building Production-Ready Services</strong></summary>
<br>

Transition to **Spring Boot**, the de facto standard for enterprise Java, and learn to build robust, secure, and data-driven applications.

| Lesson                                  | Key Concepts                                    |
| :-------------------------------------- | :---------------------------------------------- |
| `IntroToSpringBoot.java`                | **Spring Core:** Dependency Injection, IoC, Beans. |
| `ConfigurationAndProfiles.java`         | Externalized configuration with `application.properties`. |
| `TheServiceLayer.java`                  | Architecting for testability and separation of concerns. |
| `JPA & SpringDataJPA.java`              | Modern database persistence with Hibernate and ORM. |
| `DatabaseMigrationsWithFlyway.java`     | **Critical Skill:** Version control for your database schema. |
| `DTOsAndModelMapping.java`              | Designing secure and flexible public APIs.     |
| `SpringSecurity & JWT.java`             | **Project:** Secure your API with JSON Web Tokens and Role-Based Access. |
| `IntegrationTesting.java`               | Testing the full application stack with **Testcontainers**. |
| `ContainerizingWithDocker.java`         | Packaging your application into a portable Docker container. |

</details>

<details>
<summary><strong>Part 6: The Cloud-Native Path - Distributed Systems & Microservices</strong></summary>
<br>

Enter the world of modern cloud architecture by breaking down your application into a system of distributed microservices.

| Lesson                               | Key Concepts                                    |
| :----------------------------------- | :---------------------------------------------- |
| `MonolithToMicroservices.md`         | Understanding the architecture and its trade-offs. |
| `API Gateway & ServiceDiscovery.java`  | Using **Spring Cloud Gateway** and **Eureka** to manage a distributed system. |
| `ResilienceWithCircuitBreakers.java` | Building fault-tolerant systems with **Resilience4j**. |
| `AsynchronousCommunication.java`     | Decoupling services with message queues like **RabbitMQ**. |
| `Observability.md`                   | Distributed tracing and metrics with **Micrometer, Zipkin & Prometheus**. |
| `DockerComposeForDevelopment.md`     | **Project:** Creating a complete, multi-container local dev environment. |
| `DeployingToKubernetes.md`           | Introduction to container orchestration with **Kubernetes**. |
| `TheJourneyContinues.md`             | A final summary and guide to future learning paths. |

</details>

---

## ⭐ Show Your Support

If this journey helps you on your path to mastering Java, please **give this repository a star!** It helps the project reach more learners and encourages us to keep creating great, free content.

## 🤝 Connect & Contribute

This project is for the community. Contributions, suggestions, and corrections are all welcome! Feel free to [open an issue](https://github.com/dunamismax/Java-A-Project-Based-Journey/issues) to discuss an idea or submit a pull request with an improvement.

Connect with the author, **dunamismax**, on:

*   **Twitter:** [@dunamismax](https://twitter.com/dunamismax)
*   **Bluesky:** [@dunamismax.bsky.social](https://bsky.app/profile/dunamismax.bsky.social)
*   **Reddit:** [u/dunamismax](https://www.reddit.com/user/dunamismax)
*   **Discord:** `dunamismax`
*   **Signal:** `dunamismax.66`

## 📜 License

This project is licensed under the **MIT License**. See the `LICENSE` file for details.