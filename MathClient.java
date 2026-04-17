import java.io.*;
import java.net.*;
import java.util.Random;

public class MathClient {
    public static void main(String[] args) throws Exception {
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter your name: ");
        String name = userInput.readLine();

        // Server IP and port to connect to
        String serverIP = "127.0.0.1";
        int port = 6789;

        // Open connection and set up input/output streams
        Socket clientSocket = new Socket(serverIP, port);
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        // Send client name to server for identification and wait for acknowledgement
        outToServer.writeBytes(MathProtocol.CONNECT + MathProtocol.SEPARATOR + name + "\n");
        String response = inFromServer.readLine();

        if (MathProtocol.ACK.equals(response)) {
            System.out.println("Connected to server successfully.");

            Random rand = new Random();
            String[] ops = {"ADD", "SUB", "MUL", "DIV"};

            // Send 3 math requests at random intervals to test server's request handling and queuing
            for (int i = 0; i < 3; i++) {
                Thread.sleep(rand.nextInt(3000)); // Random delay to simulate real usage
                String request = MathProtocol.buildMathRequest(ops[rand.nextInt(ops.length)], rand.nextInt(50), rand.nextInt(50));
                outToServer.writeBytes(request + "\n");
                String serverResponse = inFromServer.readLine();
                if (serverResponse != null && serverResponse.startsWith(MathProtocol.ERROR)) {
                    System.out.println("Sent: " + request + " | Server error: " + serverResponse.split(MathProtocol.SEPARATOR)[1]);
                } else {
                    System.out.println("Sent: " + request + " | Server: " + serverResponse);
                }
            }

            // Notify server to close connection after requests are done
            outToServer.writeBytes(MathProtocol.CLOSE + "\n");
            System.out.println("Connection closed.");
        }

        // Close socket after session ends
        clientSocket.close();
    }
}