
/**
 * This lesson is a hands-on project to build your first networked application:
 * a basic command-line chat server and client.
 *
 * This project uses **Sockets**, the fundamental building block for all network
 * communication, allowing two programs to talk to each other over a network.
 *
 * We will create two separate programs in this file:
 * 1.  `ChatServer`: Listens for a single client connection.
 * 2.  `ChatClient`: Connects to the server to send and receive messages.
 *
 * ### How to Run This Project:
 *
 * 1.  **Compile the file:**
 *     ```sh
 *     javac SimpleSocketClientServer.java
 *     ```
 *
 * 2.  **Open Terminal 1 and start the Server:**
 *     ```sh
 *     java ChatServer
 *     ```
 *
 * 3.  **Open Terminal 2 and start the Client:**
 *     ```sh
 *     java ChatClient
 *     ```
 *
 * 4.  Type messages into the client terminal and press Enter. The server will
 *     receive and display them, and then send a response back.
 *     Type "exit" in the client to end the session.
 *
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

// This class is a placeholder for instructions. The real work is in the classes below.
public class SimpleSocketClientServer {
}

/**
 * The Server program. It waits for a single client to connect and then chats
 * with it.
 */
class ChatServer {
    public static void main(String[] args) {
        final int PORT = 12345;
        System.out.println("[SERVER] Starting up...");

        // Use `try-with-resources` to auto-close the sockets, which is crucial.
        try (
                // 1. Create a ServerSocket to listen on a specific port.
                ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("[SERVER] Waiting for a client to connect on port " + PORT);

            // 2. `accept()` is a blocking method. It waits until a client connects.
            // It then returns a `Socket` object representing the connection to that client.
            Socket clientSocket = serverSocket.accept();
            System.out.println("[SERVER] Client connected: " + clientSocket.getRemoteSocketAddress());

            try (
                    // 3. Set up streams to communicate with the connected client.
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                out.println("[SERVER] Welcome! You are connected. I will echo your messages.");

                String clientMessage;
                // 4. Read messages from the client until the connection is closed.
                while ((clientMessage = in.readLine()) != null) {
                    System.out.println("[SERVER] Received from client: " + clientMessage);
                    if ("exit".equalsIgnoreCase(clientMessage)) {
                        break;
                    }
                    // Echo the message back to the client.
                    out.println("Echo from server: " + clientMessage);
                }
            }

        } catch (IOException e) {
            System.err.println("[SERVER] Error: " + e.getMessage());
        }

        System.out.println("[SERVER] Shutting down.");
    }
}

/**
 * The Client program. It connects to the server and sends user input.
 */
class ChatClient {
    public static void main(String[] args) {
        final String HOST = "127.0.0.1"; // "localhost", meaning the same machine.
        final int PORT = 12345;
        System.out.println("[CLIENT] Starting up...");

        try (
                // 1. Create a socket to connect to the server.
                Socket socket = new Socket(HOST, PORT);
                // 2. Set up streams for sending data TO the server and reading FROM it.
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                // 3. Set up a reader for console input from the user.
                BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("[CLIENT] Connected to server at " + HOST + ":" + PORT);

            // Read the initial welcome message from the server.
            System.out.println("Server says: " + in.readLine());

            System.out.println("Type a message and press Enter. Type 'exit' to quit.");
            String userInput;
            // 4. Loop to read user input and send it to the server.
            while ((userInput = consoleReader.readLine()) != null) {
                out.println(userInput); // Send message to server.

                if ("exit".equalsIgnoreCase(userInput)) {
                    break;
                }

                // Wait for and print the server's response.
                String serverResponse = in.readLine();
                System.out.println("Server echoes: " + serverResponse);
            }
        } catch (UnknownHostException e) {
            System.err.println("[CLIENT] Error: Server not found at " + HOST);
        } catch (IOException e) {
            System.err.println("[CLIENT] I/O Error: " + e.getMessage());
        }

        System.out.println("[CLIENT] Shutting down.");
    }
}