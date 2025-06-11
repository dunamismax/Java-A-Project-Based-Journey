
/**
 * @file 26_JDBC_DatabaseApp.java
 * @author dunamismax
 * @date 2025-06-11
 *
 * @brief Project: Connect to a database using JDBC and perform CRUD operations.
 *
 * ---
 *
 * ## Talking to Databases: JDBC
 *
 * **JDBC (Java Database Connectivity)** is the standard Java API for connecting and interacting
 * with relational databases. It provides a common interface that allows a Java program to execute
 * SQL commands against a wide range of databases (like MySQL, PostgreSQL, Oracle, etc.) without
 * needing to know the low-level details of each one. [1, 2]
 *
 * ### How JDBC Works:
 * To connect to a specific database vendor, JDBC relies on a **driver**. A JDBC driver is a
 * software component (usually a JAR file) that implements the JDBC API for a specific database.
 * You include this driver JAR in your project's dependencies. [3]
 *
 * ### This Project:
 * We will build a simple console application that performs the four fundamental database
 * operations, known as **CRUD**:
 * - **C**reate: Add new records to a table.
 * - **R**ead: Retrieve records from a table.
 * - **U**pdate: Modify existing records.
 * - **D**elete: Remove records from a table.
 *
 * To make this example easy to run without any external database setup, we will use the **H2 Database Engine**.
 * H2 can run as an "in-memory" database, meaning it's created when our program starts and wiped clean
 * when it stops. It's perfect for development and testing. [6]
 *
 * ### Prerequisites (Maven `pom.xml`):
 * Your project must include the JDBC driver for the database you want to use.
 * For this H2 example, you would add this dependency to your `pom.xml`:
 * ```xml
 * <dependency>
 *     <groupId>com.h2database</groupId>
 *     <artifactId>h2</artifactId>
 *     <version>2.2.224</version> <!-- Use a recent version -->
 * </dependency>
 * ```
 *
 * ### What you will learn:
 * - How to load a JDBC driver and establish a `Connection` to a database.
 * - The importance of `PreparedStatement` to prevent SQL injection and improve performance. [8]
 * - How to execute queries and process results using a `ResultSet`.
 * - How to execute updates (INSERT, UPDATE, DELETE).
 * - The crucial role of `try-with-resources` for managing database connections safely. [10]
 *
 */

import java.sql.*;

public class JDBC_DatabaseApp {

    // --- Database Connection Details ---
    // The JDBC URL for an H2 in-memory database named 'testdb'.
    private static final String DB_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa"; // Default user for H2
    private static final String PASS = ""; // Default password for H2

    public static void main(String[] args) {
        System.out.println("--- JDBC CRUD Application ---");

        // The Connection object represents our session with the database.
        // We use a single try-with-resources block for the connection to ensure it's
        // always closed.
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            System.out.println("Successfully connected to the in-memory H2 database.");

            // 1. Create a table
            createEmployeeTable(conn);

            // 2. Create (INSERT) new employees
            System.out.println("\n--- Inserting Employees ---");
            insertEmployee(conn, "Alice", "Developer", 80000);
            insertEmployee(conn, "Bob", "Designer", 75000);
            insertEmployee(conn, "Charlie", "Manager", 95000);

            // 3. Read (SELECT) all employees
            System.out.println("\n--- Current Employees ---");
            readAllEmployees(conn);

            // 4. Update an employee's salary
            System.out.println("\n--- Updating Bob's Salary ---");
            updateEmployeeSalary(conn, "Bob", 78000);

            // 5. Delete an employee
            System.out.println("\n--- Deleting Alice ---");
            deleteEmployee(conn, "Alice");

            // 6. Read again to see the final state
            System.out.println("\n--- Final Employee Roster ---");
            readAllEmployees(conn);

        } catch (SQLException e) {
            // SQLException is a checked exception that JDBC methods throw.
            System.err.println("Database error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createEmployeeTable(Connection conn) throws SQLException {
        String createTableSQL = """
                CREATE TABLE employees (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(255),
                    position VARCHAR(255),
                    salary INT
                )
                """;
        // Use a plain Statement for DDL (Data Definition Language) commands like
        // CREATE.
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("Employee table created.");
        }
    }

    private static void insertEmployee(Connection conn, String name, String position, int salary) throws SQLException {
        String insertSQL = "INSERT INTO employees (name, position, salary) VALUES (?, ?, ?)";

        // Use a PreparedStatement for DML (Data Manipulation Language) commands with
        // parameters.
        // This is safer (prevents SQL injection) and more efficient.
        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            // Set the parameter values using the `?` placeholders (1-based index).
            pstmt.setString(1, name);
            pstmt.setString(2, position);
            pstmt.setInt(3, salary);

            int rowsAffected = pstmt.executeUpdate(); // `executeUpdate` is for INSERT, UPDATE, DELETE
            System.out.println(rowsAffected + " row(s) inserted for " + name);
        }
    }

    private static void readAllEmployees(Connection conn) throws SQLException {
        String selectSQL = "SELECT id, name, position, salary FROM employees";
        try (
                Statement stmt = conn.createStatement();
                // A ResultSet is a table of data representing the results of a database query.
                ResultSet rs = stmt.executeQuery(selectSQL) // `executeQuery` is for SELECT
        ) {
            // The `rs.next()` method moves the cursor to the next row. It returns false
            // when there are no more rows.
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String position = rs.getString("position");
                int salary = rs.getInt("salary");
                System.out.printf("ID: %d, Name: %s, Position: %s, Salary: %d%n", id, name, position, salary);
            }
        }
    }

    private static void updateEmployeeSalary(Connection conn, String name, int newSalary) throws SQLException {
        String updateSQL = "UPDATE employees SET salary = ? WHERE name = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            pstmt.setInt(1, newSalary);

            pstmt.setString(2, name);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println(rowsAffected + " row(s) updated for " + name);
        }
    }

    private static void deleteEmployee(Connection conn, String name) throws SQLException {
        String deleteSQL = "DELETE FROM employees WHERE name = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
            pstmt.setString(1, name);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println(rowsAffected + " row(s) deleted for " + name);
        }
    }
}