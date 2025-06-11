/**
 * @file 23_BasicMultithreading.java
 * @author dunamismax
 * @date 2025-06-11
 *
 * @brief Introduces the fundamentals of concurrency by creating and running
 *        multiple threads of execution.
 *
 *        ---
 *
 *        ## Doing Multiple Things at Once: Concurrency
 *
 *        All the programs we've written so far have been **single-threaded**.
 *        They follow one path of
 *        execution from top to bottom, doing one thing at a time.
 *        **Concurrency** is the ability of
 *        a program to handle multiple tasks at the same time. This is achieved
 *        through **multithreading**.
 *
 *        A **thread** is the smallest sequence of programmed instructions that
 *        can be managed independently
 *        by an operating system scheduler. Think of it like having multiple
 *        chefs in a kitchen instead of
 *        just one; the kitchen can produce more food faster because multiple
 *        tasks (chopping, sautéing, plating)
 *        are happening in parallel. [1, 2, 4]
 *
 *        In Java, you can make your programs multithreaded to perform
 *        long-running tasks in the background
 *        (like a network download) without freezing the main application, or to
 *        take advantage of
 *        modern multi-core processors to speed up intensive computations.
 *
 *        ### What you will learn:
 *        - The concept of a `Thread` and its lifecycle.
 *        - The two primary ways to define a task for a thread: extending
 *        `Thread` and implementing `Runnable`. [5]
 *        - The critical difference between calling `thread.start()` and
 *        `thread.run()`.
 *        - How to use `Thread.sleep()` to pause a thread's execution.
 *        - How to use `thread.join()` to wait for a thread to finish.
 *        - A preview of the dangers of multithreading: race conditions.
 *
 */

public class BasicMultithreading {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("Main thread started. ID: " + Thread.currentThread().getId());

        // --- METHOD 1: Extending the Thread class ---
        // This is simple, but less flexible, as Java does not support multiple
        // inheritance.
        System.out.println("\n--- Starting a thread by extending the Thread class ---");
        TaskByExtendingThread thread1 = new TaskByExtendingThread();
        // `start()` creates a new OS-level thread and calls the `run()` method on it.
        // It returns immediately, allowing the main thread to continue. [7]
        thread1.start();

        // --- METHOD 2: Implementing the Runnable interface ---
        // This is the PREFERRED approach. It separates the task (`Runnable`) from the
        // execution mechanism (`Thread`), which is better design. Your task class can
        // still extend another class. [5, 8]
        System.out.println("\n--- Starting a thread by implementing the Runnable interface ---");
        TaskByImplementingRunnable myTask = new TaskByImplementingRunnable();
        Thread thread2 = new Thread(myTask);
        thread2.start();

        // --- METHOD 3: The Modern Way with Lambdas ---
        // Since `Runnable` is a functional interface (it has only one abstract method,
        // `run()`),
        // we can use a concise lambda expression. This is very common.
        System.out.println("\n--- Starting a thread with a lambda expression ---");
        Thread thread3 = new Thread(() -> {
            System.out.println("Task started via Lambda. Thread ID: " + Thread.currentThread().getId());
            // Simulating some work
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Lambda task finished.");
        });
        thread3.start();

        // **IMPORTANT:** Never call `run()` directly (e.g., `thread1.run()`).
        // This would NOT start a new thread. It would simply execute the `run()` method
        // on the current (main) thread, just like any other normal method call.

        // --- Waiting for Threads to Finish with `join()` ---
        // `thread.join()` makes the current thread (in this case, `main`) block and
        // wait
        // until the thread it's called on has completed its execution. [9]
        // This is essential for coordinating tasks.
        System.out.println("\nMain thread is now waiting for thread1 and thread2 to finish...");
        thread1.join(); // Wait for thread1 to die.
        thread2.join(); // Wait for thread2 to die.
        thread3.join(); // Wait for thread3 to die.
        System.out.println("All worker threads have finished.");

        // --- The Danger: A Preview of Race Conditions ---
        System.out.println("\n--- Demonstrating a Race Condition ---");
        SharedCounter counter = new SharedCounter();

        // Create two threads that will both try to increment the same counter.
        Thread t_inc1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                counter.increment();
            }
        });

        Thread t_inc2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                counter.increment();
            }
        });

        t_inc1.start();
        t_inc2.start();

        // Wait for both threads to finish their work
        t_inc1.join();
        t_inc2.join();

        // The expected result is 20,000. However, the actual result will likely be
        // LESS.
        // This is because the `count++` operation is not atomic. One thread can read
        // the value
        // while the other is in the middle of writing it, causing some increments to be
        // lost.
        // This is a classic "race condition".
        System.out.println("Expected final count: 20000");
        System.out.println("Actual final count:   " + counter.count);
        System.out.println("This unpredictable result shows why we need synchronization, which we'll cover next!");

        System.out.println("\nMain thread finished.");
    }
}

/**
 * Approach 1: Define a task by extending the `Thread` class.
 */
class TaskByExtendingThread extends Thread {
    @Override
    public void run() {
        System.out.println("Task started by extending Thread. Thread ID: " + this.getId());
        try {
            // Pausing a thread with `Thread.sleep()`. It takes milliseconds.
            // It throws a checked InterruptedException, which we must handle.
            Thread.sleep(1000); // Sleep for 1 second
        } catch (InterruptedException e) {
            System.err.println("Thread was interrupted.");
        }
        System.out.println("Task by extending Thread finished.");
    }
}

/**
 * Approach 2: Define a task by implementing the `Runnable` interface.
 */
class TaskByImplementingRunnable implements Runnable {
    @Override
    public void run() {
        // We use `Thread.currentThread()` to get a reference to the thread executing
        // this task.
        System.out.println("Task started by implementing Runnable. Thread ID: " + Thread.currentThread().getId());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Task by implementing Runnable finished.");
    }
}

/**
 * A simple counter object that will be shared between multiple threads.
 * This class is NOT thread-safe.
 */
class SharedCounter {
    int count = 0;

    void increment() {
        count++; // This is not an atomic operation.
    }
}