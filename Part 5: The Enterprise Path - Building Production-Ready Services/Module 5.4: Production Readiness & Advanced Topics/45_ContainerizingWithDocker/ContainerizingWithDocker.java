
/**
 * This lesson explains how to package a Spring Boot application into a **Docker container**,
 * making it portable, scalable, and ready for modern cloud deployment.
 *
 * ## What is Docker and Why Use It?
 *
 * **Docker** is a platform that allows you to package an application and all its dependencies
 * (like the JVM, libraries, and application server) into a single, isolated unit called a
 * **container**.
 *
 * ### Key Benefits:
 * - **"It works on my machine" is no longer a problem.** A container runs identically
 *   everywhere, from a developer's laptop to a production server.
 * - **Portability:** Docker containers can run on any system that has Docker installed
 *   (Linux, Windows, macOS, cloud platforms like AWS, GCP, Azure).
 * - **Isolation:** Containers run in their own isolated environment, so they don't
 *   interfere with other applications on the same host machine.
 * - **Scalability:** It's easy to start, stop, and manage multiple instances of your
 *   application to handle changing loads.
 *
 * ## The `Dockerfile`
 *
 * A `Dockerfile` is a simple text file that contains a set of instructions for building
 * a Docker image. An **image** is the blueprint for a container. Our `Dockerfile` will:
 * 1.  Start from a base image that already has Java installed.
 * 2.  Copy our compiled Spring Boot application JAR file into the image.
 * 3.  Tell Docker how to run the application when a container is started from the image.
 *
 * ## SETUP:
 *
 * 1.  **Install Docker Desktop** on your machine.
 *
 * 2.  **Create a `Dockerfile`** in the root directory of your Spring Boot project (the same
 *     level as your `pom.xml`).
 *
 *     **File: `Dockerfile`**
 *     ```dockerfile
 *     # --- Stage 1: Build the application using Maven ---
 *     # Use an official Maven image with a specific Java version as the builder
 *     FROM maven:3.9-eclipse-temurin-17 AS build
 *
 *     # Set the working directory inside the container
 *     WORKDIR /app
 *
 *     # Copy the pom.xml file to download dependencies
 *     COPY pom.xml .
 *     RUN mvn dependency:go-offline
 *
 *     # Copy the rest of the source code
 *     COPY src ./src
 *
 *     # Package the application into a JAR file
 *     RUN mvn package -DskipTests
 *
 *
 *     # --- Stage 2: Create the final, lightweight production image ---
 *     # Use a slim Java runtime image for the final container
 *     FROM eclipse-temurin:17-jre-jammy
 *
 *     # Set the working directory
 *     WORKDIR /app
 *
 *     # Copy the built JAR from the 'build' stage into this final image
 *     COPY --from=build /app/target/*.jar app.jar
 *
 *     # Expose the port the application runs on
 *     EXPOSE 8080
 *
 *     # The command to run when the container starts
 *     ENTRYPOINT ["java", "-jar", "app.jar"]
 *     ```
 *
 * ## HOW TO BUILD AND RUN THE CONTAINER:
 * 1.  Open a terminal in the root directory of your project.
 *
 * 2.  **Build the Docker image:** The `-t` flag "tags" (names) your image.
 *     The `.` at the end tells Docker to look for the `Dockerfile` in the current directory.
 *     ```sh
 *     docker build -t my-java-app .
 *     ```
 *
 * 3.  **Run the Docker container from the image:** The `-p` flag maps port 8080 from
 *     your local machine to port 8080 inside the container.
 *     ```sh
 *     docker run -p 8080:8080 my-java-app
 *     ```
 *
 * 4.  **Test it!** Your application is now running inside a container. You can access it
 *     just like before: `curl http://localhost:8080`.
 *
 * 5.  To see your running container, open another terminal and run `docker ps`.
 *     To stop it, run `docker stop <container_id>`.
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// --- Main Application Entry Point ---
// This is a very simple Spring Boot application that we will containerize.
@SpringBootApplication
public class ContainerizingWithDocker {

    public static void main(String[] args) {
        SpringApplication.run(ContainerizingWithDocker.class, args);
    }
}

// A simple controller to verify the application is running.
@RestController
class DockerController {

    @GetMapping("/")
    public String getContainerizedGreeting() {
        // We can use System.getenv() to prove we're inside a Docker environment.
        String hostname = System.getenv("HOSTNAME");
        if (hostname != null && !hostname.isEmpty()) {
            return "Hello from inside a Docker container! My hostname is: " + hostname;
        }
        return "Hello from a Spring Boot application!";
    }
}