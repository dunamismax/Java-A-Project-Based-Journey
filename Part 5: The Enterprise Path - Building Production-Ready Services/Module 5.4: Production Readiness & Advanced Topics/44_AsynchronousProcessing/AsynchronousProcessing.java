
/**
 * This lesson introduces **Asynchronous Processing** in Spring Boot, a powerful
 * technique for improving application responsiveness and throughput.
 *
 * ## The Problem: Long-Running Tasks Block the API
 *
 * By default, web requests in Spring are handled synchronously. When a request comes in,
 * it is processed on a single thread. If that process involves a slow task (like sending an
 * email, generating a complex report, or calling a slow third-party API), the thread is
 * blocked, and the client has to wait until the entire task is finished. This can lead to
 * slow response times and poor user experience.
 *
 * ## The Solution: `@Async`
 *
 * Spring's `@Async` annotation provides a simple yet powerful way to execute a method
 * on a separate thread from the main request-handling thread.
 *
 * The new flow becomes:
 * 1. An HTTP request arrives at the controller.
 * 2. The controller immediately calls a method annotated with `@Async`.
 * 3. The `@Async` method starts running in a background thread from a managed thread pool.
 * 4. The controller method **does not wait** for the background task to finish. It returns
 *    a response to the client immediately (e.g., "Your report is being generated").
 *
 * ## Core Concepts & Annotations Covered:
 *
 * - **Synchronous vs. Asynchronous Execution**: Understanding the difference.
 * - **`@EnableAsync`**: The main switch to enable asynchronous processing support in your
 *   Spring Boot application.
 * - **`@Async`**: A method-level annotation that tells Spring to execute the method in a
 *   background thread.
 * - **Thread Pools**: Spring manages a pool of threads to execute these async tasks efficiently.
 *
 * ## PREQUISITES: A `pom.xml` with `spring-boot-starter-web`.
 *
 * ## HOW TO RUN AND TEST THIS API:
 * 1.  Run the `main` method.
 * 2.  Use `curl` to call the API endpoint: `curl http://localhost:8080/api/reports`
 * 3.  **Observe the timing**:
 *     - The `curl` command will return an `HTTP 202 Accepted` response **almost instantly**.
 *     - **Watch the application console logs**. You will see the "Report generation started..."
 *       message appear *after* the curl command has already finished. This proves the task
 *       is running in the background.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// --- Main Application Entry Point ---
// @EnableAsync is the crucial annotation that activates Spring's async capabilities.
@EnableAsync
@SpringBootApplication
public class AsynchronousProcessing {
    public static void main(String[] args) {
        SpringApplication.run(AsynchronousProcessing.class, args);
        System.out.println("✅ Application started with Asynchronous processing enabled.");
    }
}

// --- 1. The Asynchronous Service Layer ---
// This service contains the long-running task we want to offload.

@Service
class ReportService {
    private static final Logger log = LoggerFactory.getLogger(ReportService.class);

    // This method is marked as @Async. When called from another Spring bean,
    // it will be executed in a separate background thread.
    @Async
    public void generateReport() {
        // We get the current thread name to prove it's different from the web request
        // thread.
        String threadName = Thread.currentThread().getName();
        log.info("BACKGROUND TASK: Report generation started on thread: {}", threadName);

        try {
            // Simulate a long-running task that takes 5 seconds.
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            log.error("Report generation was interrupted.", e);
            Thread.currentThread().interrupt(); // Restore the interrupted status
        }

        log.info("BACKGROUND TASK: Report generation finished on thread: {}", threadName);
    }
}

// --- 2. The Web API Controller ---
// This controller triggers the asynchronous task.

@RestController
@RequestMapping("/api/reports")
class ReportController {
    private static final Logger log = LoggerFactory.getLogger(ReportController.class);
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping
    public ResponseEntity<String> triggerReportGeneration() {
        String threadName = Thread.currentThread().getName();
        log.info("WEB REQUEST: Received request to generate report on thread: {}", threadName);

        // Call the async method. This call will return immediately.
        reportService.generateReport();

        log.info("WEB REQUEST: Asynchronous task has been triggered. Returning immediate response to client.");

        // We return an HTTP 202 Accepted status. This is a standard way to tell the
        // client that their request has been accepted for processing, but is not yet
        // complete.
        return new ResponseEntity<>("Report generation has been started. You will be notified upon completion.",
                HttpStatus.ACCEPTED);
    }
}