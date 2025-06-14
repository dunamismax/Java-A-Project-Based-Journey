
/**
 * @file 28_SimpleWebAPI.java
 * @author dunamismax
 * @date 2025-06-11
 *
 * @brief Project: Become a Backend Dev by building a simple REST API that can serve data over HTTP using a lightweight framework.
 *
 * ---
 *
 * ## Building a Backend: Your First REST API
 *
 * Welcome to backend development! A **REST API (Representational State Transfer Application Programming Interface)**
 * is an architectural style for building web services. In simple terms, it's a program that runs on a
 * server and responds to HTTP requests from clients (like a web browser, a mobile app, or another server).
 * APIs are the backbone of the modern web, allowing different applications to communicate with each other.
 *
 * Today, we will use **Javalin**, a very lightweight and modern web framework for Java, to build a simple
 * API for managing a collection of users. It's incredibly easy to get started with and makes building
 * web services fun. [1, 2]
 *
 * ### Our API will have three "endpoints":
 * 1.  `GET /api/users`: Retrieve a list of all users.
 * 2.  `GET /api/users/{id}`: Retrieve a single user by their ID.
 * 3.  `POST /api/users`: Create a new user from a JSON payload.
 *
 * ### Prerequisites (Maven `pom.xml`):
 * You need to add dependencies for Javalin and a JSON library like Gson. Javalin uses SLF4J for logging, so we also need a simple logger implementation.
 * ```xml
 * <!-- Javalin Web Framework -->
 * <dependency>
 *     <groupId>io.javalin</groupId>
 *     <artifactId>javalin</artifactId>
 *     <version>6.1.3</version> <!-- Use a recent version -->
 * </dependency>
 *
 * <!-- Logger implementation for Javalin -->
 * <dependency>
 *     <groupId>org.slf4j</groupId>
 *     <artifactId>slf4j-simple</artifactId>
 *     <version>2.0.7</version>
 * </dependency>
 *
 * <!-- JSON library -->
 * <dependency>
 *     <groupId>com.google.code.gson</groupId>
 *     <artifactId>gson</artifactId>
 *     <version>2.10.1</version>
 * </dependency>
 * ```
 *
 * ### How to Run and Test This API:
 * 1.  Run the `main` method in this file. The server will start and print a message.
 * 2.  Open a separate terminal or an API client like Postman or Insomnia.
 * 3.  Use `curl` (a command-line tool) or your client to send requests:
 *     - **Get all users:** `curl http://localhost:7070/api/users`
 *     - **Get user with ID 1:** `curl http://localhost:7070/api/users/1`
 *     - **Create a new user:** `curl -X POST -H "Content-Type: application/json" -d '{"name":"Charlie"}' http://localhost:7070/api/users`
 *
 * ### What you will learn:
 * - How to set up and run a web server in Java.
 * - How to define API endpoints (routes) for different HTTP methods (`GET`, `POST`).
 * - How to handle path parameters (e.g., the `{id}` in the URL).
 * - How to receive and send JSON data in the body of a request/response.
 *
 */

import com.google.gson.Gson;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

// A simple record to model our User data.
record User(Integer id, String name) {
}

public class SimpleWebAPI {

    // --- In-Memory "Database" ---
    // A thread-safe map to store our users. In a real app, this would be a real
    // database.
    private static final Map<Integer, User> userDatabase = new ConcurrentHashMap<>();
    private static final AtomicInteger nextId = new AtomicInteger(0);
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        // Create and start a Javalin server on port 7070.
        Javalin app = Javalin.create().start(7070);

        System.out.println("✅ Web server started. Listening on http://localhost:7070");
        System.out.println("Try visiting http://localhost:7070/api/users in your browser or with curl.");

        // Initialize with some dummy data.
        initializeData();

        // --- DEFINE API ENDPOINTS (ROUTES) ---

        // The Handler lambda for each route takes a `Context` object, which provides
        // everything
        // needed to handle the request (e.g., get headers, body, params) and send a
        // response.

        // 1. GET /api/users -> Returns all users
        app.get("/api/users", SimpleWebAPI::getAllUsersHandler);

        // 2. GET /api/users/{id} -> Returns a specific user
        app.get("/api/users/{id}", SimpleWebAPI::getOneUserHandler);

        // 3. POST /api/users -> Creates a new user
        app.post("/api/users", SimpleWebAPI::createUserHandler);
    }

    // --- Route Handler Implementations ---

    private static void getAllUsersHandler(Context ctx) {
        // `ctx.json()` automatically serializes the given Java object to a JSON string
        // and sets the response `Content-Type` header to `application/json`.
        ctx.json(userDatabase.values());
    }

    private static void getOneUserHandler(Context ctx) {
        // `ctx.pathParam()` extracts a parameter from the URL path.
        // It's always a String, so we need to convert it to an Integer.
        Integer id = Integer.parseInt(ctx.pathParam("id"));
        User user = userDatabase.get(id);

        if (user != null) {
            ctx.json(user);
        } else {
            // Set the HTTP status code to 404 Not Found and return an error message.
            ctx.status(404).result("User not found");
        }
    }

    private static void createUserHandler(Context ctx) {
        // `ctx.bodyAsClass()` automatically deserializes the JSON request body
        // into an instance of the specified Java class.
        User newUserRequest = gson.fromJson(ctx.body(), User.class);

        // In a real app, you'd validate the request here.

        int id = nextId.incrementAndGet();
        User createdUser = new User(id, newUserRequest.name());

        userDatabase.put(id, createdUser);

        // Set the HTTP status to 201 Created and return the new user object.
        ctx.status(201).json(createdUser);
    }

    private static void initializeData() {
        // Create User 1
        int aliceId = nextId.incrementAndGet();
        userDatabase.put(aliceId, new User(aliceId, "Alice"));

        // Create User 2
        int bobId = nextId.incrementAndGet();
        userDatabase.put(bobId, new User(bobId, "Bob"));
    }
}