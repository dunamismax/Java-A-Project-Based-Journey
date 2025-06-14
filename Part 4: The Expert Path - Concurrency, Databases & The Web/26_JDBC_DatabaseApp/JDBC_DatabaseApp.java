
/**
 * This lesson is a hands-on project demonstrating how to connect a Java application
 * to a database using **JDBC (Java Database Connectivity)**.
 *
 * JDBC is the standard Java API for interacting with relational databases. It allows
 * us to execute the four fundamental **CRUD** operations:
 * - **C**reate: Add new data.
 * - **R**ead:   Retrieve data.
 * - **U**pdate: Modify existing data.
 * - **D**elete: Remove data.
 *
 * To run this project without any database installation, we use **H2**, an "in-memory"
 * database. It's created when the program starts and is destroyed when it stops,
 * making it perfect for development and learning.
 *
 * PREQUISITE: This project requires a build tool like Maven with the H2 database
 * driver added as a dependency in `pom.xml`.
 * ```xml
 * <dependency>
 *     <groupId>com.h2database</groupId>
 *     <artifactId>h2</artifactId>
 *     <version>2.2.224</version>
 * </dependency>
 * ```
 *
 * HOW TO RUN THIS FILE:
 * 1. Ensure the H2 dependency is in your pom.xml.
 * 2. Compile and run through your IDE or via Maven.
 */

import java.sql.*;

public class JDBC_DatabaseApp {

    // The JDBC URL for an H2 in-memory database named 'user_db'.
    private static final String DB_URL = "jdbc:h2:mem:user_db;DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    public static void main(String[] args) {
        // A single `try-with-resources` block ensures the database connection is
        // always closed automatically, preventing resource leaks.
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            System.out.println("--- Database Connection Successful ---");

            // 1. Setup: Create our 'users' table.
            createUsersTable(conn);

            // 2. CREATE: Add new records to the table.
            System.out.println("\n[CREATE] Inserting initial users...");
            addUser(conn, "Alice", "alice@example.com");
            addUser(conn, "Bob", "bob@example.com");

            // 3. READ: Display the current state.
            System.out.println("\n[READ] Current users in the database:");
            displayAllUsers(conn);

            // 4. UPDATE: Modify an existing record.
            System.out.println("\n[UPDATE] Changing Alice's email...");
            updateUserEmail(conn, "Alice", "alice_new@example.com");

            // 5. DELETE: Remove a record.
            System.out.println("\n[DELETE] Removing Bob from the database...");
            deleteUser(conn, "Bob");

            // 6. READ: Display the final state of the database.
            System.out.println("\n[READ] Final state of users:");
            displayAllUsers(conn);

        } catch (SQLException e) {
            // SQLException is a checked exception that all JDBC methods can throw.
            System.err.println("Database Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createUsersTable(Connection conn) throws SQLException {
        String sql = """
                CREATE TABLE users (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    email VARCHAR(255) UNIQUE
                )
                """;
        // Use a simple `Statement` for table creation (no parameters needed).
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    private static void addUser(Connection conn, String name, String email) throws SQLException {
        // Use a `PreparedStatement` to safely insert data with parameters (`?`).
        // This is crucial for preventing SQL injection attacks.
        String sql = "INSERT INTO users(name, email) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name); // Set the first '?' to the name
            pstmt.setString(2, email); // Set the second '?' to the email
            pstmt.executeUpdate(); // Use `executeUpdate` for INSERT, UPDATE, or DELETE
        }
    }

    private static void updateUserEmail(Connection conn, String name, String newEmail) throws SQLException {
        String sql = "UPDATE users SET email = ? WHERE name = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newEmail);
            pstmt.setString(2, name);
            pstmt.executeUpdate();
        }
    }

    private static void deleteUser(Connection conn, String name) throws SQLException {
        String sql = "DELETE FROM users WHERE name = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        }
    }

    private static void displayAllUsers(Connection conn) throws SQLException {
        String sql = "SELECT id, name, email FROM users";
        try (
                Statement stmt = conn.createStatement();
                // The `ResultSet` holds the data returned from a SELECT query.
                ResultSet rs = stmt.executeQuery(sql)) {
            boolean found = false;
            // `rs.next()` moves the cursor to the next row and returns false if there are
            // no more.
            while (rs.next()) {
                found = true;
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                System.out.printf("  - ID: %d, Name: %s, Email: %s%n", id, name, email);
            }
            if (!found) {
                System.out.println("  - No users found.");
            }
        }
    }
}