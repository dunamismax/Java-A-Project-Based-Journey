/**
 * @file 1_HelloWorld.java
 * @author dunamismax
 * @date 2025-06-11
 *
 * @brief The traditional first program for learning a new language: "Hello,
 *        World!"
 *
 *        ---
 *
 *        ## Welcome to Your Java Journey!
 *
 *        Java is a powerful, versatile, and widely-used "Object-Oriented
 *        Programming" (OOP) language.
 *        This means that everything in Java is organized within a "class".
 *        Think of a class as a
 *        blueprint or a container for your program's logic and data. [1, 9]
 *
 *        This program is the simplest complete application you can write. Its
 *        only job is to
 *        display the message "Hello, World!" on your screen. We start here
 *        because it introduces
 *        the most basic, mandatory structure of a Java program without being
 *        overwhelming. [10]
 *
 *        ### What You Will Learn:
 *        - The fundamental structure of a Java class.
 *        - The special `main` method, which is the entry point of every
 *        standalone Java application. [10]
 *        - How to print text to the console.
 *        - The process of compiling and running your first Java program. [2]
 *
 *        ### The Java Philosophy: Write Once, Run Anywhere
 *
 *        When you compile this Java file, it doesn't turn into machine code for
 *        your specific
 *        operating system (like Windows or macOS). Instead, it becomes "Java
 *        bytecode". This
 *        bytecode is a universal language that can be run by the Java Virtual
 *        Machine (JVM). [15]
 *        As long as a device has a JVM, it can run your compiled code. This is
 *        the core principle
 *        that makes Java so portable.
 *
 *        ---
 *
 *        ### How to Compile and Run This Program:
 *
 *        1. **Save the Code:** Ensure this code is saved in a file named
 *        `1_HelloWorld.java`.
 *        **Crucially, the filename must exactly match the public class name,
 *        including case.** [4]
 *
 *        2. **Open Your Terminal:** This is your command-line interface (like
 *        Terminal on macOS/Linux,
 *        or Command Prompt/PowerShell on Windows). Navigate to the directory
 *        where you saved this file.
 *
 *        3. **Compile the Code (`javac`):** Use the Java compiler to transform
 *        this human-readable
 *        source code into Java bytecode. This creates a new file named
 *        `HelloWorld.class`. [2]
 *
 *        `javac 1_HelloWorld.java`
 *
 *        4. **Run the Bytecode (`java`):** Use the Java Virtual Machine (via
 *        the `java` command)
 *        to execute the compiled bytecode. Note that you do NOT include the
 *        `.class` extension. [2]
 *
 *        `java HelloWorld`
 *
 *        5. **You should see the following output on your screen:**
 *
 *        `Hello, World!`
 *
 */

// In Java, every piece of runnable code must belong to a class. [5]
// `public` is an access modifier, meaning this class is visible to everything.
// [14]
// `class` is the keyword used to define a class blueprint.
// `HelloWorld` is the name we've given our class. By convention, class names
// start with a capital letter (PascalCase). [4]
public class HelloWorld {

    /**
     * @brief This is the `main` method, the official entry point for any standalone
     *        Java application.
     *
     *        When you execute `java HelloWorld`, the JVM searches for this exact
     *        method signature
     *        inside the specified class and begins running the code within its
     *        curly braces `{}`. [8]
     *        Let's break down its signature: `public static void main(String[]
     *        args)`.
     *
     *        - `public`: An access modifier, making the method globally available
     *        so the JVM can call it. [14]
     *        - `static`: This keyword means the method belongs to the `HelloWorld`
     *        class itself, not to a specific
     *        instance (object) of the class. This allows the JVM to run the method
     *        without first needing
     *        to create an object, which would be a chicken-and-egg problem. [6, 14]
     *        - `void`: This is the return type. `void` signifies that this method
     *        does not return any value
     *        when it finishes executing. [14]
     *        - `main`: This is the special name that the JVM is hard-coded to look
     *        for as the starting point. [14]
     *        - `(String[] args)`: This defines a parameter named `args`. It is an
     *        array of `String` objects
     *        that can be used to pass command-line arguments to the program. We
     *        will explore this in later lessons.
     */
    public static void main(String[] args) {

        // This line of code executes the printing. Let's deconstruct it:
        // - `System`: A built-in final class from the `java.lang` package that contains
        // facilities
        // related to the standard input, output, and error streams of the system.
        // - `out`: A static member variable of the `System` class. It represents the
        // "standard output"
        // stream, which by default is your console or terminal. [2]
        // - `println()`: A method of the `out` stream. `println` stands for "print
        // line". It prints
        // the text (String) you provide inside the parentheses to the console, and then
        // moves the
        // cursor to the next line. [10]
        System.out.println("Hello, World!");

        // In Java, every statement must end with a semicolon (;).
        // This semicolon tells the compiler that you've finished a complete
        // instruction.

    } // The closing brace for the main method.

} // The closing brace for the HelloWorld class.