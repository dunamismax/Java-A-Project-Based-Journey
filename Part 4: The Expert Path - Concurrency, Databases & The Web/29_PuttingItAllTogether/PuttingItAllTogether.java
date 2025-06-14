/**
 * This is the capstone project, combining all major concepts from this course
 * into a single, professional-grade application.
 *
 * We will build a database-backed REST API that is structured, testable, and
 * maintainable, integrating the following key skills:
 *
 * - **Web API (Javalin):** To handle HTTP requests.
 * - **Database (JDBC & H2):** For persistent data storage.
 * - **Logging (SLF4J & Logback):** For professional, structured logging.
 * - **JSON (Gson):** For serializing and deserializing data.
 * - **Dependency Management (Maven):** To manage all our libraries.
 * - **Architecture:** We use a **Data Access Object (DAO)** pattern to cleanly
 * separate database logic from our web request handlers.
 *
 * ## The Flow of a Request:
 * `Client -> HTTP Request -> Javalin Handler -> DAO Method -> JDBC -> Database`
 *
 * PREQUISITE: A full `pom.xml` with dependencies for Javalin, SLF4J/Logback,
 * H2, Gson, and JUnit is required.
 *
 * HOW TO RUN THIS FILE:
 * 1. Ensure all dependencies are in your pom.xml.
 * 2. Ensure you have a `logback.xml` in `src/main/resources`.
 * 3. Compile and run. Test with `curl` or an API client.
 */

// --- Main Application Entry Point ---
public class PuttingItAllTogether {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PuttingItAllTogether.class);

    public static void main(String[] args) {
        try {
            // 1. Initialize the database connection and the DAO layer.
            var dbConnection = DatabaseManager.getConnection();
            var productDao = new ProductDao(dbConnection);
            productDao.initialize(); // Create tables if they don't exist.

            // 2. Initialize the API layer, injecting the DAO.
            var apiServer = new ApiServer(productDao);

            // 3. Start the server.
            apiServer.start(7070);

            log.info("✅ Application started successfully.");
        } catch (java.sql.SQLException e) {
            log.error("❌ FATAL: Application failed to start due to a database error.", e);
        }
    }
}

// --- 1. The Data Model ---
record Product(Integer id, String name, double price) {
}

// --- 2. The Data Access Layer (DAO) ---
// This class encapsulates all database logic.
class ProductDao {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ProductDao.class);
    private final java.sql.Connection connection;

    public ProductDao(java.sql.Connection connection) {
        this.connection = connection;
    }

    public void initialize() throws java.sql.SQLException {
        log.info("Initializing database schema...");
        String sql = "CREATE TABLE IF NOT EXISTS products (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255) NOT NULL, price DOUBLE NOT NULL)";
        try (var stmt = connection.createStatement()) {
            stmt.execute(sql);
            log.info("Schema initialized.");
        }
    }

    public java.util.List<Product> getAll() throws java.sql.SQLException {
        var products = new java.util.ArrayList<Product>();
        String sql = "SELECT * FROM products";
        try (var stmt = connection.createStatement(); var rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                products.add(new Product(rs.getInt("id"), rs.getString("name"), rs.getDouble("price")));
            }
        }
        return products;
    }

    public Product create(String name, double price) throws java.sql.SQLException {
        String sql = "INSERT INTO products(name, price) VALUES (?, ?)";
        try (var pstmt = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.executeUpdate();
            try (var keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    log.info("Created product with ID: {}", id);
                    return new Product(id, name, price);
                }
            }
        }
        throw new java.sql.SQLException("Failed to create product, no ID obtained.");
    }
}

// --- 3. The Web API Layer ---
// This class configures and runs the Javalin web server.
class ApiServer {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ApiServer.class);
    private final ProductDao productDao;
    private final com.google.gson.Gson gson = new com.google.gson.Gson();

    public ApiServer(ProductDao productDao) {
        this.productDao = productDao;
    }

    public void start(int port) {
        io.javalin.Javalin app = io.javalin.Javalin.create(config -> {
            config.http.showJavalinBanner = false;
        });

        // Define API endpoints.
        app.get("/products", this::getAllProducts);
        app.post("/products", this::createProduct);

        // Add a global exception handler for better error responses.
        app.exception(Exception.class, (e, ctx) -> {
            log.error("An unhandled exception occurred for request: {}", ctx.path(), e);
            ctx.status(500).result("Internal Server Error");
        });

        app.start(port);
        log.info("🚀 API server listening on http://localhost:{}", port);
    }

    // --- API Request Handlers ---
    private void getAllProducts(io.javalin.http.Context ctx) throws java.sql.SQLException {
        log.info("Handling request: GET /products");
        var products = productDao.getAll();
        ctx.json(products);
    }

    private void createProduct(io.javalin.http.Context ctx) throws java.sql.SQLException {
        log.info("Handling request: POST /products");
        var productRequest = gson.fromJson(ctx.body(), Product.class);
        var newProduct = productDao.create(productRequest.name(), productRequest.price());
        ctx.status(201).json(newProduct);
    }
}

// --- 4. Database Manager ---
// A utility class to handle the database connection details.
class DatabaseManager {
    private static final String DB_URL = "jdbc:h2:mem:capstone_db;DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    public static java.sql.Connection getConnection() throws java.sql.SQLException {
        return java.sql.DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}