
/**
 * This lesson introduces **Application Metrics**, the final pillar of observability
 * (alongside Logging and Tracing). Metrics provide quantitative, time-series data
 * about the health and performance of your application.
 *
 * ## The Problem: How do we know if our service is healthy?
 *
 * Logs tell us what happened. Traces tell us where it happened. But neither tells us
 * about the overall state of the system in an easily quantifiable way. We need to answer
 * questions like:
 * - What is the average response time for my API?
 * - How many requests per second are we handling?
 * - What is our error rate right now?
 * - How much memory is the JVM using?
 *
 * ## The Solution: Micrometer, Prometheus, and Grafana
 *
 * This combination is the industry standard for metrics collection and visualization:
 *
 * 1.  **Micrometer (The Instrumentation Library):** Spring Boot Actuator integrates deeply
 *     with Micrometer. It automatically instruments your application to gather a huge
 *     number of useful metrics out-of-the-box (e.g., HTTP request latency, JVM stats,
 *     CPU usage). Micrometer acts as a facade, similar to SLF4J for logging.
 *
 * 2.  **Prometheus (The Time-Series Database):** Prometheus is a powerful database specifically
 *     designed to store and query time-series data like metrics. It operates on a "pull"
 *     model: it periodically scrapes a `/actuator/prometheus` endpoint that our application
 *     exposes.
 *
 * 3.  **Grafana (The Visualization Dashboard):** Grafana is a dashboarding tool that connects
 *     to Prometheus as a data source. It allows you to build powerful, beautiful graphs
 *     and dashboards to visualize your application's metrics in real-time.
 *
 * ## PREQUISITES:
 * 1.  **Docker Desktop** must be running.
 * 2.  **Maven `pom.xml` Dependencies** for your Spring Boot application:
 *     ```xml
 *     <!-- Spring Boot Actuator: Exposes management endpoints like /actuator -->
 *     <dependency>
 *         <groupId>org.springframework.boot</groupId>
 *         <artifactId>spring-boot-starter-actuator</artifactId>
 *     </dependency>
 *
 *     <!-- Micrometer's Prometheus Registry: Formats metrics for Prometheus -->
 *     <dependency>
 *         <groupId>io.micrometer</groupId>
 *         <artifactId>micrometer-registry-prometheus</artifactId>
 *         <scope>runtime</scope>
 *     </dependency>
 *     ```
 *
 * 3.  **Configuration in `application.properties`**:
 *     ```properties
 *     # Expose the 'health' and 'prometheus' actuator endpoints over HTTP
 *     management.endpoints.web.exposure.include=health,prometheus
 *     # Add application tags to every metric for better filtering
 *     management.metrics.tags.application=${spring.application.name}
 *     ```
 *
 * 4.  **Prometheus Configuration File (`prometheus.yml`):**
 *     Create this file in the root of your project. It tells Prometheus where to find your app.
 *     ```yaml
 *     global:
 *       scrape_interval: 15s
 *     scrape_configs:
 *       - job_name: 'my-spring-app'
 *         metrics_path: '/actuator/prometheus'
 *         static_configs:
 *           # Use 'host.docker.internal' to allow the Prometheus container to reach your app running on the host
 *           - targets: ['host.docker.internal:8080']
 *     ```
 *
 * ## HOW TO RUN AND TEST THIS:
 * 1.  **Start Prometheus & Grafana using Docker Compose (Terminal 1):**
 *     You would typically have a `docker-compose.yml` for this. For now, you can run them manually
 *     or use a pre-existing Compose file for Prometheus/Grafana.
 *     *Alternatively, run Prometheus directly:*
 *     `docker run -d --name prometheus -p 9090:9090 -v $(pwd)/prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus`
 *
 * 2.  **Start the Spring Boot Application (Terminal 2):** `java com.yourpackage.MetricsAndAlerting`
 *
 * 3.  **Verify Metrics are Exposed:** In your browser, go to `http://localhost:8080/actuator/prometheus`.
 *     You will see a wall of text-based metrics. This is what Prometheus is scraping.
 *
 * 4.  **Generate some traffic:** Run this command a few times to create metric data:
 *     `curl http://localhost:8080/api/hello`
 *     `curl http://localhost:8080/api/slow-task`
 *
 * 5.  **Explore in Prometheus:**
 *     - Open the Prometheus UI: `http://localhost:9090`
 *     - In the query bar, start typing `http_server_requests` and execute the query. You can see
 *       the requests you just made!
 *
 * 6.  **(Optional) Explore in Grafana:** If you have Grafana running, you can add Prometheus
 *     as a data source and build dashboards to graph these metrics over time.
 */

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// --- Main Application Entry Point & Controller ---
@SpringBootApplication
@RestController
public class MetricsAndAlerting {

    private final MeterRegistry meterRegistry;
    private final Counter customRequestCounter;

    /**
     * We can inject the MeterRegistry to create our own custom metrics.
     */
    public MetricsAndAlerting(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Create a custom counter metric.
        this.customRequestCounter = Counter.builder("api.custom.requests.total")
                .description("Total number of requests to custom endpoints.")
                .tag("region", "us-east-1") // Tags are key-value pairs for filtering.
                .register(meterRegistry);
    }

    /**
     * The @Timed annotation from Micrometer automatically creates a timer metric
     * for this specific endpoint. It will record the duration and count of calls.
     * Metric name will be `http.server.requests` with a tag `uri="/api/hello"`.
     */
    @GetMapping("/api/hello")
    @Timed(value = "api.hello.requests", description = "Time taken to return the hello greeting")
    public String sayHello() {
        // We can increment our custom counter programmatically.
        customRequestCounter.increment();
        return "Hello, Metrics World!";
    }

    /**
     * Another timed endpoint to simulate a slower operation.
     */
    @GetMapping("/api/slow-task")
    @Timed("api.slowtask.requests")
    public String performSlowTask() {
        try {
            Thread.sleep(1500); // Simulate a 1.5 second task
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "Slow task completed!";
    }

    public static void main(String[] args) {
        SpringApplication.run(MetricsAndAlerting.class, args);
        System.out.println("✅ Application started. Actuator endpoints enabled.");
        System.out.println("Metrics available at: http://localhost:8080/actuator/prometheus");
    }
}