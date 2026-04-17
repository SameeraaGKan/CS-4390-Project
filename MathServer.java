import java.io.*;
import java.net.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.LinkedBlockingQueue;

// Main server class - listens for incoming client connections on specified port
public class MathServer {
    // Port number clients must connect to
    private static final int PORT = 6789;

    // Shared queue ensures all client requests are processed in order of arrival
    public static final LinkedBlockingQueue<Runnable> requestQueue = new LinkedBlockingQueue<>();

    public static void main(String[] args) {
        try (ServerSocket welcomeSocket = new ServerSocket(PORT)) {
            System.out.println("Math Server is UP and running on port " + PORT);

            // Single worker thread processes one request at a time to maintain order
            Thread worker = new Thread(() -> {
                while (true) {
                    try { requestQueue.take().run(); }
                    catch (InterruptedException e) { break; }
                }
            });
            worker.setDaemon(true);
            worker.start();

            // Continuously accept new client connections and spawn a handler thread for each
            while (true) {
                Socket connectionSocket = welcomeSocket.accept();
                new ClientHandler(connectionSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// Handles each client in its own thread to allow simultaneous connections
class ClientHandler extends Thread {
    private Socket socket;
    private String clientName;
    private LocalDateTime startTime;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        this.startTime = LocalDateTime.now(); // Record connection time to calculate session duration
    }

    public void run() {
        try (BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream())) {

            String clientSentence;
            while ((clientSentence = inFromClient.readLine()) != null) {
                String[] parts = clientSentence.split(MathProtocol.SEPARATOR);
                String command = parts[0];

                if (command.equals(MathProtocol.CONNECT)) {
                    this.clientName = parts[1];
                    System.out.println("[" + startTime + "] Client Attached: " + clientName);
                    outToClient.writeBytes(MathProtocol.ACK + "\n");
                }
                else if (command.equals(MathProtocol.MATH_REQ)) {
                    final String[] p = parts;
                    final String name = clientName;
                    // Add math request to shared queue for ordered processing
                    MathServer.requestQueue.put(() -> {
                        String op = p[1];
                        int n1 = Integer.parseInt(p[2]);
                        int n2 = Integer.parseInt(p[3]);

                        // Check for division by zero before calculating
                        if (op.equalsIgnoreCase("DIV") && n2 == 0) {
                            System.out.println("[" + LocalDateTime.now() + "] ERROR | " + name + " | DIV by zero");
                            try { outToClient.writeBytes(MathProtocol.ERROR + MathProtocol.SEPARATOR + "Division by zero\n"); }
                            catch (IOException e) { e.printStackTrace(); }
                            return;
                        }

                        int result = calculate(op, n1, n2);
                        System.out.println("[" + LocalDateTime.now() + "] REQUEST | " + name + " | " + op + " | " + n1 + " | " + n2);
                        try { outToClient.writeBytes("RESULT" + MathProtocol.SEPARATOR + result + "\n"); }
                        catch (IOException e) { e.printStackTrace(); }
                    });
                }
                else if (command.equals(MathProtocol.CLOSE)) {
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error handling client " + clientName);
        } catch (InterruptedException e) {
            System.out.println("Request queue interrupted for client " + clientName);
        } finally {
            // Log session duration and close socket when client disconnects
            long duration = Duration.between(startTime, LocalDateTime.now()).getSeconds();
            System.out.println("Client " + clientName + " disconnected. Session: " + duration + "s");
            try { socket.close(); } catch (IOException e) { e.printStackTrace(); }
        }
    }

    // Perform math calculation based on operator string
    private int calculate(String op, int a, int b) {
        switch (op.toUpperCase()) {
            case "ADD": return a + b;
            case "SUB": return a - b;
            case "MUL": return a * b;
            case "DIV": return (b != 0) ? a / b : 0;
            default: return 0;
        }
    }
}