/**
 * Welcome to Java! This is the traditional first program for any new
 * programmer.
 * Its only job is to print the message "Hello, World!" to the console.
 *
 * In Java, every application must begin with a class definition. The class name
 * must match the filename. For example, this file must be named
 * `HelloWorld.java`.
 *
 * HOW TO RUN THIS FILE:
 * 1. Open your computer's terminal or command prompt.
 * 2. Navigate to the directory where this file is saved.
 * 3. Compile the file by typing: javac HelloWorld.java
 * 4. Run the compiled code by typing: java HelloWorld
 */

// `public class HelloWorld` defines a class named "HelloWorld".
// A class is a container for your program's code.
public class HelloWorld {

    // `public static void main(String[] args)` is the main "method".
    // This is the required starting point for every Java application. The Java
    // Virtual Machine (JVM) looks for this exact signature to begin execution. [8]
    public static void main(String[] args) {

        // `System.out.println()` is a built-in Java statement that prints text
        // to the console. [10] Whatever is inside the parentheses will be displayed.
        System.out.println("Hello, World!");

    } // End of the main method.

} // End of the HelloWorld class.