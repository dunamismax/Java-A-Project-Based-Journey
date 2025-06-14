/**
 * This lesson explains how to move beyond `System.out.println` to a professional
 * logging framework, a crucial skill for building real-world applications.
 *
 * ## Why is `System.out.println` bad for real projects?
 * - It's always on. You can't disable it for production without changing code.
 * - It provides no context (like timestamp, severity, or class name).
 * - It's inflexible, always printing to the console.
 *
 * ## The Solution: SLF4J and Logback
 * The standard practice is to use a logging "facade" with an "implementation".
 * 1.  **SLF4J (The Facade):** The API you use in your code. It's an abstraction
 *     that lets you switch out the logging engine without changing your code.
 * 2.  **Logback (The Implementation):** The powerful engine that actually writes
 *     the logs. It is configured via a `logback.xml` file.
 *
 * ## Key Concepts
 * - **Log Levels:** `TRACE < DEBUG < INFO < WARN < ERROR`. You can configure the
 *   system to only show messages above a certain level (e.g., `INFO` in production).
 * - **Configuration:** All aspects (levels, output format, file destinations) are
 *   controlled by `logback.xml` in `src/main/resources`, NOT by the Java code.
 *
 * ### PREQUISITE: Maven `pom.xml` Dependencies
 * ```xml
 * <dependency>
 *     <groupId>org.slf4j</groupId>
 *     <artifactId>slf4j-api</artifactId>
 *     <version>2.0.7</version>
 * </dependency>
 * <dependency>
 *     <groupId>ch.qos.logback</groupId>
 *     <artifactId>logback-classic</artifactId>
 *     <version>1.4.11</version>
 * </dependency>
 * ```
 *
 * HOW TO RUN THIS FILE:
 * 1. Ensure the dependencies are in your pom.xml.
 * 2. Ensure you have a `logback.xml` file in `src/main/resources`.
 * 3. Compile and run.
 */

// Imports from the SLF4J API. Your code only depends on the facade.
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class OrderProcessor {

    // 1. Get a logger instance. It's a static final constant.
    // Naming it by the class (`OrderProcessor.class`) is a universal convention.
    private static final Logger log = LoggerFactory.getLogger(OrderProcessor.class);

    public void processOrder(int orderId) {
        // 2. Use parameterized logging `{}`.
        // This is highly efficient. The message string is only constructed if the
        // log level is enabled, avoiding unnecessary work.
        log.info("Processing order ID: {}.", orderId);

        if (orderId < 0) {
            log.warn("Invalid Order ID: {}. ID cannot be negative. Aborting.", orderId);
            return;
        }

        log.debug("Order ID {} is valid. Fetching from database...", orderId);
        // ... database fetching logic would go here ...

        try {
            // Simulate a step that might fail, like connecting to a payment gateway.
            if (orderId == 999) {
                throw new IllegalStateException("Payment gateway timed out");
            }
            log.debug("Payment processed successfully for order {}.", orderId);

        } catch (IllegalStateException e) {
            // 3. Log the exception properly.
            // Passing the exception `e` as the last argument tells Logback to print
            // the full stack trace, which is essential for debugging errors.
            log.error("Failed to process payment for order ID: {}", orderId, e);
            return;
        }

        log.info("Successfully processed order ID: {}.", orderId);
    }
}

public class LoggingInPractice {
    public static void main(String[] args) {
        OrderProcessor processor = new OrderProcessor();

        System.out.println("--- Scenario 1: Successful Order ---");
        processor.processOrder(101);

        System.out.println("\n--- Scenario 2: Invalid Input ---");
        processor.processOrder(-5);

        System.out.println("\n--- Scenario 3: Processing Error ---");
        processor.processOrder(999);

        System.out.println("\n\nNOTE: The format of the output above is controlled by logback.xml.");
        System.out.println("Try changing the <root level> in logback.xml to 'DEBUG' to see more detailed logs!");
    }
}