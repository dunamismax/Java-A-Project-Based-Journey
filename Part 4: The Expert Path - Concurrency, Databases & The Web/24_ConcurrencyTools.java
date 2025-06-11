
/**
 * @file 24_ConcurrencyTools.java
 * @author dunamismax
 * @date 2025-06-11
 *
 * @brief Moves beyond basic threads to professional concurrency tools for preventing race conditions and managing thread pools.
 *
 * ---
 *
 * ## From Chaos to Control: Professional Concurrency
 *
 * In the last lesson, we saw how multithreading can lead to chaos. When multiple threads
 * try to modify the same shared data, we get a **race condition**, resulting in corrupted data
 * and unpredictable outcomes. To write correct concurrent programs, we must control access
 * to shared resources. This control is called **synchronization**.
 *
 * This lesson introduces the essential tools provided by Java's `java.util.concurrent` package
 * to write safe, robust, and efficient multithreaded applications. We will solve the race
 * condition from the previous lesson and learn the modern way to manage threads.
 *
 * ### What you will learn:
 * - **`ExecutorService`**: The modern, preferred way to manage a pool of threads, avoiding the cost of creating new threads for every task. [1, 2, 3]
 * - **The `synchronized` keyword**: The classic Java mechanism to create a "mutex" (mutual exclusion), ensuring only one thread can execute a block of code at a time. [5, 8]
 * - **`Atomic` Variables**: High-performance, thread-safe classes (like `AtomicInteger`) that are perfect for simple counters and flags, often avoiding the need for heavier locks. [10, 12]
 *
 */

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

// --- 1. The Unsafe Counter (The Problem) ---
class UnsafeCounter {
    private int count = 0;

    public void increment() {
        count++;
    }

    public int getCount() {
        return count;
    }
}

// --- 2. Solution using `synchronized` ---
class SynchronizedCounter {
    private int count = 0;

    // The `synchronized` keyword ensures that only one thread can execute this
    // method
    // on a given instance of SynchronizedCounter at a time. It acquires the
    // object's
    // intrinsic lock, executes the code, and then releases the lock. [5, 8]
    public synchronized void increment() {
        count++;
    }

    public int getCount() {
        return count;
    }
}

// --- 3. Solution using Atomic Variables ---
class AtomicCounter {
    // AtomicInteger is a thread-safe class. Its operations, like
    // `incrementAndGet()`,
    // are "atomic", meaning they happen as a single, indivisible operation without
    // interference from other threads. [10, 12]
    private final AtomicInteger count = new AtomicInteger(0);

    public void increment() {
        count.incrementAndGet(); // This operation is thread-safe.
    }

    public int getCount() {
        return count.get();
    }
}

public class ConcurrencyTools {

    public static void main(String[] args) throws InterruptedException {

        final int NUM_THREADS = 4;
        final int INCREMENTS_PER_THREAD = 10000;
        final int EXPECTED_RESULT = NUM_THREADS * INCREMENTS_PER_THREAD;

        // --- DEMO 1: The Race Condition using an ExecutorService ---
        System.out.println("--- 1. Demonstrating the Race Condition ---");
        // An ExecutorService manages a pool of threads. It's far more efficient
        // than creating `new Thread()` for every small task. [1, 2, 3]
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        UnsafeCounter unsafeCounter = new UnsafeCounter();

        // Submit tasks to the thread pool for execution.
        for (int i = 0; i < NUM_THREADS; i++) {
            executor.submit(() -> {
                for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    unsafeCounter.increment();
                }
            });
        }
        // It's crucial to shut down the executor service. `shutdown()` stops it from
        // accepting new tasks, and `awaitTermination` blocks until all running tasks
        // finish. [4]
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println("Expected value: " + EXPECTED_RESULT);
        System.out.println("Unsafe counter value: " + unsafeCounter.getCount() + " (INCORRECT due to race condition)");

        // --- DEMO 2: Solving with `synchronized` ---
        System.out.println("\n--- 2. Solving with the `synchronized` keyword ---");
        executor = Executors.newFixedThreadPool(NUM_THREADS);
        SynchronizedCounter synchronizedCounter = new SynchronizedCounter();
        for (int i = 0; i < NUM_THREADS; i++) {
            executor.submit(() -> {
                for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    synchronizedCounter.increment();
                }
            });
        }
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println("Expected value: " + EXPECTED_RESULT);
        System.out.println("Synchronized counter value: " + synchronizedCounter.getCount() + " (Correct)");

        // --- DEMO 3: Solving with `AtomicInteger` ---
        System.out.println("\n--- 3. Solving with AtomicInteger (Modern & Preferred for counters) ---");
        executor = Executors.newFixedThreadPool(NUM_THREADS);
        AtomicCounter atomicCounter = new AtomicCounter();
        for (int i = 0; i < NUM_THREADS; i++) {
            executor.submit(() -> {
                for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    atomicCounter.increment();
                }
            });
        }
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println("Expected value: " + EXPECTED_RESULT);
        System.out
                .println("Atomic counter value: " + atomicCounter.getCount() + " (Correct and often more performant)");
    }
}