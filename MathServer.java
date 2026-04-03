import java.io.*;
import java.net.*;
import java.time.Duration;
import java.time.LocalDateTime;

public class MathServer {
    private static final int PORT = 6789; // Matches your starter code

    public static void main(String[] args) {
        try (ServerSocket welcomeSocket = new ServerSocket(PORT)) {
            System.out.println("Math Server is UP and running on port " + PORT);

            while (true) {
                // Requirement: Accept multiple connections simultaneously [cite: 12]
                Socket connectionSocket = welcomeSocket.accept();
                new ClientHandler(connectionSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler extends Thread {
    private Socket socket;
    private String clientName;
    private LocalDateTime startTime;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        this.startTime = LocalDateTime.now();
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
                    // Requirement: Show who sent what request [cite: 13]
                    String op = parts[1];
                    int n1 = Integer.parseInt(parts[2]);
                    int n2 = Integer.parseInt(parts[3]);
                    int result = calculate(op, n1, n2);
                    
                    System.out.println("Request from " + clientName + ": " + op + " " + n1 + ", " + n2);
                    outToClient.writeBytes("RESULT" + MathProtocol.SEPARATOR + result + "\n");
                } 
                else if (command.equals(MathProtocol.CLOSE)) {
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error handling client " + clientName);
        } finally {
            // Requirement: Log connection duration and closure [cite: 10, 15]
            long duration = Duration.between(startTime, LocalDateTime.now()).getSeconds();
            System.out.println("Client " + clientName + " disconnected. Session: " + duration + "s");
            try { socket.close(); } catch (IOException e) { e.printStackTrace(); }
        }
    }

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