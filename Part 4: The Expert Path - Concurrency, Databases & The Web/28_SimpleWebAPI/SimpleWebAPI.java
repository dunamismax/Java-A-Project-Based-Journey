
/**
 * This project is your entry into backend development! We will build a simple
 * **REST API** to manage a list of To-Do items.
 *
 * An API is a service that runs on a server and responds to HTTP requests. This
 * is how web browsers and mobile apps get their data. We will use **Javalin**,
 * a lightweight and modern web framework for Java.
 *
 * ## Our API Endpoints:
 * - `GET    /todos`: Get a list of all to-do items.
 * - `POST   /todos`: Create a new to-do item from a JSON request.
 * - `GET    /todos/{id}`: Get a single to-do item by its ID.
 *
 * ## PREQUISITES: Maven `pom.xml` Dependencies
 * ```xml
 * <dependency>
 *     <groupId>io.javalin</groupId>
 *     <artifactId>javalin</artifactId>
 *     <version>6.1.3</version>
 * </dependency>
 * <dependency>
 *     <groupId>org.slf4j</groupId>
 *     <artifactId>slf4j-simple</artifactId>
 *     <version>2.0.7</version>
 * </dependency>
 * <dependency>
 *     <groupId>com.google.code.gson</groupId>
 *     <artifactId>gson</artifactId>
 *     <version>2.10.1</version>
 * </dependency>
 * ```
 *
 * ## How to Test This API:
 * 1.  Run the `main` method.
 * 2.  Use a command-line tool like `curl` or an API client like Postman/Insomnia.
 *
 *     - **Get all:** `curl http://localhost:7070/todos`
 *     - **Get one:** `curl http://localhost:7070/todos/1`
 *     - **Create:** `curl -X POST -H "Content-Type: application/json" -d '{"task":"Learn Javalin"}' http://localhost:7070/todos`
 */

import com.google.gson.Gson;
import io.javalin.Javalin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleWebAPI {

    // --- Data Layer ---
    // A simple record to model our data.
    record Todo(Integer id, String task, boolean completed) {
    }

    // An in-memory, thread-safe map to act as our "database".
    private static final Map<Integer, Todo> todoDatabase = new ConcurrentHashMap<>();
    private static final AtomicInteger nextId = new AtomicInteger(0);

    // A reusable JSON converter.
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        // Initialize with some data.
        initializeData();

        // Create and start the Javalin web server on port 7070.
        Javalin app = Javalin.create().start(7070);
        System.out.println("✅ API Server started. Listening on http://localhost:7070");
        System.out.println("Try visiting http://localhost:7070/todos in your browser.");

        // --- API Endpoint Definitions ---
        app.get("/todos", SimpleWebAPI::getAllTodos);
        app.get("/todos/{id}", SimpleWebAPI::getTodoById);
        app.post("/todos", SimpleWebAPI::createTodo);
    }

    // --- Route Handler Implementations ---

    /**
     * Handles the `GET /todos` request.
     * Returns a JSON array of all to-do items.
     */
    private static void getAllTodos(io.javalin.http.Context ctx) {
        // `ctx.json()` automatically serializes the Java object collection to a JSON
        // string.
        ctx.json(todoDatabase.values());
    }

    /**
     * Handles the `GET /todos/{id}` request.
     * Returns a single to-do item or a 404 Not Found error.
     */
    private static void getTodoById(io.javalin.http.Context ctx) {
        try {
            // `ctx.pathParam()` extracts the `id` from the URL.
            Integer id = Integer.parseInt(ctx.pathParam("id"));
            Todo todo = todoDatabase.get(id);

            if (todo != null) {
                ctx.json(todo);
            } else {
                ctx.status(404).result("Todo not found for ID: " + id);
            }
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid ID format. Must be a number.");
        }
    }

    /**
     * Handles the `POST /todos` request.
     * Creates a new to-do item from the JSON body of the request.
     */
    private static void createTodo(io.javalin.http.Context ctx) {
        try {
            // `ctx.body()` gets the raw request body string. We use Gson to parse it.
            Todo request = gson.fromJson(ctx.body(), Todo.class);

            if (request.task() == null || request.task().isBlank()) {
                ctx.status(400).result("Task description cannot be empty.");
                return;
            }

            // Create a new Todo with a server-generated ID and default completed status.
            int newId = nextId.incrementAndGet();
            Todo newTodo = new Todo(newId, request.task(), false);

            todoDatabase.put(newId, newTodo);

            // Respond with HTTP status 201 Created and return the newly created object.
            ctx.status(201).json(newTodo);
        } catch (Exception e) {
            ctx.status(400).result("Invalid JSON format in request body.");
        }
    }

    private static void initializeData() {
        int id1 = nextId.incrementAndGet();
        todoDatabase.put(id1, new Todo(id1, "Buy milk", false));
        int id2 = nextId.incrementAndGet();
        todoDatabase.put(id2, new Todo(id2, "Finish Java course", true));
    }
}