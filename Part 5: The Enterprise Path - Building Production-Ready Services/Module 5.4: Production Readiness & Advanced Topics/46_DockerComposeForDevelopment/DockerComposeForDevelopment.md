# Lesson 46: Docker Compose for a Powerful Development Environment

This final lesson introduces **Docker Compose**, a tool for defining and running multi-container Docker applications. It's the key to creating a realistic, consistent, and easy-to-manage local development environment.

## The Problem: Managing Multiple Services

Our application now has two main components:
1.  **The Java Application**: The Spring Boot service we built.
2.  **The Database**: A PostgreSQL (or other) database that our application depends on.

So far, to run our application locally, we've relied on an in-memory H2 database or assumed a database was already installed and running on our machine. This is not ideal because:
-   It's not representative of our production environment.
-   It requires manual setup for every new developer joining the team.
-   Managing the state and configuration of a local database can be tedious.

## The Solution: Docker Compose

Docker Compose uses a simple YAML file (named `docker-compose.yml`) to define all the services your application needs. With a single command (`docker-compose up`), it will:
1.  Read the `docker-compose.yml` file.
2.  Pull or build the required Docker images for each service (our app and the database).
3.  Create a dedicated, isolated network for them to communicate on.
4.  Start all the services with the specified configuration.

This creates a complete, self-contained environment for your application stack that is 100% reproducible.

---

## Setup

### 1. The `Dockerfile`
You must have a `Dockerfile` for your Java application as created in the previous lesson.

### 2. Update `application.properties` to Use Environment Variables

To make our Spring Boot application connect to the database provided by Docker Compose, we need to stop hard-coding connection details and start using environment variables. This is a critical practice for production-ready applications.

Update your `src/main/resources/application.properties` to look like this:

```properties
# --- Database Connection ---
# The values for these properties will be injected by Docker Compose.
# The `DB_URL` uses the service name `postgres-db` as the hostname, which works
# inside the Docker network.
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/mydatabase}
spring.datasource.username=${DB_USER:myuser}
spring.datasource.password=${DB_PASSWORD:mypassword}
spring.datasource.driver-class-name=org.postgresql.Driver

# --- JPA & Flyway ---
# Use `validate` to ensure our entities match the Flyway schema.
spring.jpa.hibernate.ddl-auto=validate
spring.flyway.enabled=true
```

The values after the colon (`:`) are defaults, so you can still run the app outside of Docker if needed.

### 3. Create the `docker-compose.yml` File

In the root directory of your project (same level as `pom.xml` and `Dockerfile`), create a file named `docker-compose.yml`.

**File: `docker-compose.yml`**

```yaml
# Specify the Docker Compose file format version.
version: '3.8'

# Define all the services (containers) our application needs.
services:
  # 1. The PostgreSQL Database Service
  postgres-db:
    image: 'postgres:16-alpine' # Use the official PostgreSQL image.
    container_name: my-app-db
    environment:
      # Set environment variables for the database container.
      # Flyway and our Spring app will use these to connect.
      - POSTGRES_USER=myuser
      - POSTGRES_PASSWORD=mypassword
      - POSTGRES_DB=mydatabase
    volumes:
      # Optional: This "volume" persists the database data on your local machine
      # so it's not lost when the container stops.
      - postgres_data:/var/lib/postgresql/data
    ports:
      # Map port 5432 on your host machine to port 5432 in the container,
      # so you can connect to the DB with a tool like DBeaver or pgAdmin.
      - "5432:5432"

  # 2. Our Java Application Service
  my-java-app:
    # Build the image from the Dockerfile in the current directory.
    build: .
    container_name: my-app-service
    # This service depends on the database service. Compose will start the
    # database first before starting our application.
    depends_on:
      - postgres-db
    ports:
      # Map port 8080 on the host to 8080 in the container.
      - "8080:8080"
    environment:
      # Inject the database connection details as environment variables.
      # Our application.properties file will read these values.
      - DB_URL=jdbc:postgresql://postgres-db:5432/mydatabase
      - DB_USER=myuser
      - DB_PASSWORD=mypassword

# Define the named volume used by the database service.
volumes:
  postgres_data:
```

---

## How to Run the Entire Application Stack

1.  **Open a terminal** in the root directory of your project.

2.  **Build and start all services:**
    ```sh
    docker-compose up --build
    ```
    -   `up`: Starts the services.
    -   `--build`: Forces Docker to rebuild your Java application's image if you've made code changes.

3.  **Observe the logs.** You will see the interleaved logs from both the PostgreSQL container and your Spring Boot application as they start up. You'll see Flyway run its migrations against the new database, and then Spring Boot will connect and start the web server.

4.  **Test your application.** The API is now running and connected to a real PostgreSQL database, all managed by Docker.
    ```sh
    curl http://localhost:8080/api/your-endpoint
    ```

5.  **To stop everything**, press `Ctrl+C` in the terminal, then run:
    ```sh
    docker-compose down
    ```
    This command stops and removes the containers and the network.

Congratulations! You have now mastered the fundamentals of building, testing, and running a modern Java application in a containerized, production-like environment.