
/**
 * This lesson moves beyond basic threads to the professional tools needed to
 * write safe and efficient concurrent applications.
 *
 * We will solve two key problems of multithreading:
 * 1.  **Inefficient Thread Management:** Constantly creating `new Thread()` is expensive.
 *     SOLUTION: Use an `ExecutorService` to manage a reusable pool of threads.
 * 2.  **Data Corruption (Race Conditions):** Uncontrolled access to shared data
 *     leads to unpredictable results.
 *     SOLUTION: Use Synchronization (`synchronized` or `Atomic` variables).
 *
 * HOW TO RUN THIS FILE:
 * 1. Compile: javac ConcurrencyTools.java
 * 2. Run:     java ConcurrencyTools
 */

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

// --- A common interface for our counter examples ---
interface Counter {
    void increment();

    int getCount();
}

// --- 1. The Problem: An Unsafe, Non-Thread-Safe Counter ---
class UnsafeCounter implements Counter {
    private int count = 0;

    public void increment() {
        count++;
    } // Not atomic, subject to race conditions

    public int getCount() {
        return count;
    }
}

// --- 2. Solution A: The `synchronized` Keyword ---
// `synchronized` acts as a lock, ensuring only one thread can execute this
// method on the same object at any given time. It is a simple but effective way
// to ensure thread safety, though it can have performance costs.
class SynchronizedCounter implements Counter {
    private int count = 0;

    public synchronized void increment() {
        count++;
    }

    public int getCount() {
        return count;
    }
}

// --- 3. Solution B: Atomic Variables (Modern & Often Faster) ---
// `AtomicInteger` is a class designed for thread-safe, lock-free operations
// on a single integer. It is highly optimized and often preferred for simple
// counters or flags.
class AtomicCounter implements Counter {
    private final AtomicInteger count = new AtomicInteger(0);

    public void increment() {
        count.incrementAndGet();
    } // This operation is atomic

    public int getCount() {
        return count.get();
    }
}

public class ConcurrencyTools {

    public static void main(String[] args) throws InterruptedException {
        // We will run the same test on three different counter implementations.
        runConcurrencyTest("1. Unsafe Counter", new UnsafeCounter());
        runConcurrencyTest("2. Synchronized Counter", new SynchronizedCounter());
        runConcurrencyTest("3. Atomic Counter", new AtomicCounter());
    }

    /**
     * A helper method to run a standardized concurrency test on any Counter
     * implementation.
     */
    private static void runConcurrencyTest(String testName, Counter counter) throws InterruptedException {
        System.out.println("\n--- Running Test: " + testName + " ---");
        final int NUM_THREADS = 4;
        final int INCREMENTS_PER_THREAD = 10000;
        final int EXPECTED_VALUE = NUM_THREADS * INCREMENTS_PER_THREAD;

        // Use an ExecutorService to manage a pool of threads.
        // This is more efficient than creating `new Thread()` every time.
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

        // Submit tasks to the thread pool.
        for (int i = 0; i < NUM_THREADS; i++) {
            executor.submit(() -> {
                for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    counter.increment();
                }
            });
        }

        // It's crucial to shut down the executor gracefully.
        executor.shutdown(); // Stops accepting new tasks.
        executor.awaitTermination(1, TimeUnit.MINUTES); // Waits for running tasks to finish.

        System.out.println("Expected final value: " + EXPECTED_VALUE);
        System.out.println("Actual final value:   " + counter.getCount());
        if (counter.getCount() == EXPECTED_VALUE) {
            System.out.println("Result: CORRECT!");
        } else {
            System.out.println("Result: INCORRECT due to a race condition!");
        }
    }
}