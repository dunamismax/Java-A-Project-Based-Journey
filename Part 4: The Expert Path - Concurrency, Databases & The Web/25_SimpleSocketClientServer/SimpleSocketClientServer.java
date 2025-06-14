
/**
 * @file 25_SimpleSocketClientServer.java
 * @author dunamismax
 * @date 2025-06-11
 *
 * @brief Your Gateway to the Internet: Build a client-server chat application using Sockets.
 *
 * ---
 *
 * ## Network Programming with Sockets
 *
 * Sockets are the fundamental building blocks of network communication. A **socket** is one
 * endpoint of a two-way communication link between two programs running on the network. They allow
 * applications to send and receive data as a stream of bytes.
 *
 * Java's `java.net` package provides a simple and powerful API for socket programming. This project
 * demonstrates how to build a basic "Echo Server" and a client that talks to it. The client will
 * send a message, and the server will "echo" it back.
 *
 * ### Core Components:
 * 1.  **`ServerSocket`**: A special socket that runs on the server and listens for incoming
 *     connection requests from clients on a specific port. [2]
 * 2.  **`Socket`**: An object representing the connection endpoint. Both the client and the server
 *     use a `Socket` object to communicate once a connection is established. [1]
 * 3.  **Port**: A number (0-65535) that identifies a specific application or process on a machine.
 *     Think of the IP address as the building's street address, and the port as the apartment number. [4]
 * 4.  **Streams**: We use `InputStream` and `OutputStream` (wrapped in more convenient Readers/Writers)
 *     to send and receive data through the socket.
 *
 * ### How to Run This Project:
 * This file contains two independent programs: a server and a client. You must run them in separate terminals.
 *
 * 1.  **Compile the file:**
 *     ```sh
 *     javac 25_SimpleSocketClientServer.java
 *     ```
 *
 * 2.  **Open Terminal 1 and start the Server:**
 *     The server will start and wait for a client to connect.
 *     ```sh
 *     java EchoServer
 *     ```
 *
 * 3.  **Open Terminal 2 and start the Client:**
 *     ```sh
 *     java EchoClient
 *     ```
 *
 * 4.  **Interact:** Type messages into the client terminal and press Enter. You will see the server
 *     receive the message and echo it back to the client. Type "exit" to close the connection.
 *
 * ### What you will learn:
 * - How to create a server that listens for client connections.
 * - How to create a client that connects to a server.
 * - How to send and receive text data over a network socket.
 * - The importance of `try-with-resources` for automatically closing network connections.
 *
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

// This class is just a placeholder to contain the instructions.
// The actual runnable classes are EchoServer and EchoClient below.
public class SimpleSocketClientServer {
    public static void main(String[] args) {
        System.out.println("This file contains two programs: EchoServer and EchoClient.");
        System.out.println("Please follow the instructions in the file's header comments to run them.");
    }
}

/**
 * The Server program. It listens for one client connection and echoes messages
 * back.
 */
class EchoServer {
    public static void main(String[] args) {
        final int PORT = 6000;
        System.out.println("Echo Server started...");

        // `try-with-resources` ensures the ServerSocket is automatically closed.
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Waiting for a client to connect on port " + PORT + "...");

            // `serverSocket.accept()` is a blocking call. It waits until a client connects.
            // It returns a Socket object representing the connection to that client.
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

            // Use another `try-with-resources` for the client-specific resources.
            try (
                    // A PrintWriter to send text data TO the client
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    // A BufferedReader to read text data FROM the client
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                String inputLine;
                // Read from the client as long as the connection is open and there's data.
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Received from client: " + inputLine);
                    // Echo the message back to the client.
                    out.println("Server echoes: " + inputLine);
                }
            }

        } catch (IOException e) {
            System.err.println("Server exception: " + e.getMessage());
        }

        System.out.println("Echo Server shutting down.");
    }
}

/**
 * The Client program. It connects to the server and sends user-typed messages.
 */
class EchoClient {
    public static void main(String[] args) {
        final String HOST = "127.0.0.1"; // "localhost" - the same machine
        final int PORT = 6000;
        System.out.println("Echo Client started...");

        try (
                // Create a socket to connect to the server at the specified host and port.
                Socket socket = new Socket(HOST, PORT);
                // A PrintWriter to send text TO the server.
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                // A BufferedReader to read text FROM the server.
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                // A BufferedReader to read text from the user's console input.
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Connected to server. Type a message and press Enter to send.");
            System.out.println("Type 'exit' to quit.");

            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                // Send the user's message to the server.
                out.println(userInput);

                if ("exit".equalsIgnoreCase(userInput)) {
                    break;
                }

                // Wait for and print the server's echoed response.
                String serverResponse = in.readLine();
                System.out.println("Response from server: " + serverResponse);
            }

        } catch (IOException e) {
            System.err.println("Client exception: " + e.getMessage());
        }

        System.out.println("Echo Client shutting down.");
    }
}