/**
 * @file 27_LoggingInPractice.java
 * @author dunamismax
 * @date 2025-06-11
 *
 * @brief Moves beyond `System.out.println` to use a professional logging framework like SLF4J & Logback.
 *
 * ---
 *
 * ## The Problem with `System.out.println`
 *
 * While `System.out.println()` is great for quick debugging, it's unsuitable for real applications because:
 * - **It's all or nothing:** You can't turn it off without removing the code.
 * - **No context:** It doesn't tell you WHEN (timestamp), WHERE (class), or the SEVERITY of the message.
 * - **Inflexible:** It always prints to the standard console and can't be easily redirected to a file.
 *
 * ## A Better Way: Logging Frameworks
 *
 * Professional applications use logging frameworks. The standard practice in the Java world is to use
 * a **logging facade** with a concrete **logging implementation**.
 *
 * 1.  **SLF4J (Simple Logging Facade for Java):** This is the API. It's an abstraction layer. Your application code
 *     only uses SLF4J classes. This decouples your code from the actual logging engine, so you can swap
 *     it out later without changing your source code. [1, 3]
 *
 * 2.  **Logback:** This is the implementation. It's the powerful engine that actually performs the logging. It is
 *     the modern successor to the popular Log4j 1.x and is highly configurable. [1, 2]
 *
 * ### Key Logging Concepts
 * - **Log Levels:** Messages have a severity level. The standard hierarchy is:
 *   `TRACE < DEBUG < INFO < WARN < ERROR`. You can configure your logger to only show messages
 *   at or above a certain level (e.g., setting the level to `INFO` will show `INFO`, `WARN`, and `ERROR` messages, but hide `DEBUG` and `TRACE`). [4, 6]
 * - **Appenders:** Define where log messages are written to (e.g., the console, a file, a database).
 * - **Configuration:** Logback is configured via a file named `logback.xml` placed in the `src/main/resources` directory.
 *
 * ### Prerequisites (Maven `pom.xml`):
 * ```xml
 * <!-- Logging Facade (The API you code against) -->
 * <dependency>
 *     <groupId>org.slf4j</groupId>
 *     <artifactId>slf4j-api</artifactId>
 *     <version>2.0.7</version>
 * </dependency>
 * <!-- Logging Implementation (The engine that does the work) -->
 * <dependency>
 *     <groupId>ch.qos.logback</groupId>
 *     <artifactId>logback-classic</artifactId>
 *     <version>1.4.11</version>
 * </dependency>
 * ```
 *
 * ### Sample `logback.xml` (`src/main/resources/logback.xml`):
 * ```xml
 * <configuration>
 *   <!-- Define an appender to write to the console -->
 *   <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
 *     <encoder>
 *       <!-- Define the pattern for the log output -->
 *       <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
 *     </encoder>
 *   </appender>
 *
 *   <!-- The root logger applies to the whole application -->
 *   <!-- Set the level to INFO. Change to DEBUG to see more messages. -->
 *   <root level="info">
 *     <appender-ref ref="STDOUT" />
 *   </root>
 * </configuration>
 * ```
 *
 * ### What you will learn:
 * - How to get a Logger instance for a class.
 * - How to write log messages at different severity levels.
 * - The performance benefit of using parameterized logging (`{}`).
 * - How to log exceptions properly.
 *
 */

// Imports from the SLF4J API
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A sample service class that performs some operations and logs its progress.
 */
class BusinessService {

    // 1. GET THE LOGGER
    // It's a standard convention to get a static final logger instance for each
    // class.
    // The `getLogger()` method takes the class itself as an argument, which
    // automatically
    // names the logger after the class (e.g., "com.example.BusinessService").
    private static final Logger log = LoggerFactory.getLogger(BusinessService.class);

    public void processData(String data) {
        // 2. USE PARAMETERIZED LOGGING
        // The `{}` syntax is a placeholder. SLF4J only constructs the full message
        // string
        // if the log level is enabled. This is much more efficient than string
        // concatenation,
        // which would always create the string even if it wasn't going to be logged.
        log.info("Starting to process data for input: '{}'", data);

        if (data == null || data.trim().isEmpty()) {
            log.warn("Input data is null or empty. Skipping processing.");
            return;
        }

        log.debug("Data validation passed. Length of data is {}.", data.length());

        try {
            // Simulate a complex operation that might fail.
            if (data.equalsIgnoreCase("error")) {
                throw new IllegalArgumentException("Simulated processing error for input 'error'");
            }
            log.debug("Complex processing step completed successfully.");

        } catch (IllegalArgumentException e) {
            // 3. LOG EXCEPTIONS PROPERLY
            // The last argument to a logging method can be a Throwable.
            // This tells the logging framework to print the full stack trace, which is
            // essential for debugging.
            log.error("An error occurred while processing data.", e);
            return;
        }

        log.info("Successfully finished processing data.");
    }
}

public class LoggingInPractice {

    public static void main(String[] args) {
        BusinessService service = new BusinessService();

        System.out.println("--- Running with valid input ---");
        service.processData("SampleData123");

        System.out.println("\n--- Running with empty input ---");
        service.processData("  ");

        System.out.println("\n--- Running with input designed to cause an error ---");
        service.processData("error");

        System.out.println("\n\nNOTE: The format of the output above (timestamp, level, etc.)");
        System.out.println("is controlled by the <pattern> in logback.xml, not by this Java code.");
        System.out.println(
                "Try changing the <root level> in logback.xml to 'DEBUG' and run this again to see more detailed logs!");
    }
}