import java.io.*;
import java.net.*;
import java.util.Random;

public class MathClient {
    public static void main(String[] args) throws Exception {
        String name = "NAS"; // Example name
        String serverIP = "127.0.0.1";
        int port = 6789;

        Socket clientSocket = new Socket(serverIP, port);
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        // 1. Initial Attachment with Name
        outToServer.writeBytes(MathProtocol.CONNECT + MathProtocol.SEPARATOR + name + "\n");
        String response = inFromServer.readLine();

        if (MathProtocol.ACK.equals(response)) {
            System.out.println("Connected to server successfully.");

            // 2. Send 3 math requests at random times
            Random rand = new Random();
            String[] ops = {"ADD", "SUB", "MUL"};
            
            for (int i = 0; i < 3; i++) {
                Thread.sleep(rand.nextInt(3000));
                String request = MathProtocol.buildMathRequest(ops[rand.nextInt(3)], rand.nextInt(50), rand.nextInt(50));
                outToServer.writeBytes(request + "\n");
                System.out.println("Sent: " + request + " | Server: " + inFromServer.readLine());
            }

            // 3. Termination 
            outToServer.writeBytes(MathProtocol.CLOSE + "\n");
            System.out.println("Connection closed.");
        }
        clientSocket.close();
    }
}