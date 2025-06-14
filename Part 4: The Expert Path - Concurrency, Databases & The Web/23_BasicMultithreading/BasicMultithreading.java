/**
 * This lesson introduces the fundamentals of multithreading, allowing a program
 * to perform multiple tasks concurrently.
 *
 * So far, our programs have been single-threaded, doing one thing at a time.
 * A **thread** is an independent path of execution. By creating multiple
 * threads,
 * we can perform long-running tasks (like a network download) in the background
 * without freezing the main application.
 *
 * We will learn:
 * - How to create and run a new thread using the modern lambda syntax.
 * - The difference between `thread.start()` (correct) and `thread.run()`
 * (incorrect).
 * - How to coordinate threads using `join()` to wait for them to finish.
 * - A demonstration of a **race condition**, a common bug in multithreaded
 * code.
 *
 * HOW TO RUN THIS FILE:
 * 1. Compile: javac BasicMultithreading.java
 * 2. Run: java BasicMultithreading
 */
public class BasicMultithreading {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("Main thread started. Let's begin our tasks.");

        // --- 1. Creating a Thread with a Lambda Expression ---
        // This is the modern, preferred way to define a task for a thread.
        // The `Runnable` interface (which `Thread` accepts) is a functional interface,
        // so we can provide its implementation with a concise lambda.
        Thread dataProcessingThread = new Thread(() -> {
            System.out.println("-> Data processing task started in a new thread.");
            for (int i = 1; i <= 5; i++) {
                System.out.println("-> Processing data chunk " + i);
                try {
                    // `Thread.sleep()` pauses the current thread for a specified time.
                    Thread.sleep(500); // Pause for 0.5 seconds
                } catch (InterruptedException e) {
                    System.out.println("Data processing thread was interrupted.");
                }
            }
            System.out.println("-> Data processing task finished.");
        });

        Thread networkCallThread = new Thread(() -> {
            System.out.println("--> Network call task started in a new thread.");
            try {
                Thread.sleep(1500); // Simulate a 1.5 second network delay.
            } catch (InterruptedException e) {
                System.out.println("Network call thread was interrupted.");
            }
            System.out.println("--> Network call task finished.");
        });

        // --- 2. Starting the Threads ---
        // `thread.start()` creates a new OS-level thread and executes the `run` logic.
        // It returns immediately, allowing the main thread to continue its own work.
        System.out.println("Starting worker threads. Main thread continues its work in parallel.");
        dataProcessingThread.start();
        networkCallThread.start();

        // --- 3. Coordinating Threads with `join()` ---
        // `thread.join()` makes the current thread (the `main` thread) wait until
        // the other thread has completed its execution. This is essential for ensuring
        // tasks are finished before proceeding.
        System.out.println("Main thread is now waiting for worker threads to complete...");
        dataProcessingThread.join(); // Wait for the data processing to finish.
        networkCallThread.join(); // Wait for the network call to finish.
        System.out.println("All worker threads have finished. Main thread continues.");

        // --- 4. The Danger: Race Conditions ---
        // A race condition occurs when multiple threads access and modify shared data,
        // leading to unpredictable results.
        System.out.println("\n--- Demonstrating a Race Condition ---");
        UnsafeCounter counter = new UnsafeCounter();

        // We'll create two threads that both increment the same counter 10,000 times.
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++)
                counter.increment();
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++)
                counter.increment();
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        // The expected result is 20,000. However, the actual result is likely to be
        // less.
        // This is because the `count++` operation is not "atomic" - one thread can
        // interfere with another, causing some increments to be lost.
        System.out.println("Expected final count: 20000");
        System.out.println("Actual final count:   " + counter.getCount());
        System.out.println("This unpredictable result highlights the need for synchronization.");
        System.out.println("\nMain thread finished.");
    }
}

/**
 * A simple counter object that will be shared between multiple threads.
 * This class is NOT thread-safe.
 */
class UnsafeCounter {
    private int count = 0;

    public void increment() {
        // This operation reads the value, adds one, and writes it back.
        // Another thread can read the old value before this one finishes,
        // causing an update to be lost.
        count++;
    }

    public int getCount() {
        return count;
    }
}