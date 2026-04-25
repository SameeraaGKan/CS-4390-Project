# 🌐 NexusMath

**NexusMath** is a distributed network application implemented in Java that provides centralized mathematical computation services via a multi-client architecture. Utilizing **Java Sockets**, NexusMath enables seamless intercommunication between programs running on different network nodes, allowing for simultaneous processing of mathematical requests and real-time client telemetry.

---

## 🛠️ System Architecture

The application is built on a robust client-server model designed to handle concurrent operations with high reliability.

### **Centralized Math Server**
* **Connection Management:** Maintains simultaneous connections with multiple clients using a dedicated `ClientHandler` thread for each attachment.
* **User Telemetry:** Automatically tracks the client's identity, the timestamp of connection, and logs the total session duration in seconds upon disconnection.
* **Ordered Request Queue:** Implements a `LinkedBlockingQueue` and a dedicated worker thread to ensure that math requests from all clients are processed in the exact order they are received.
* **Event Logging:** Records client details upon attachment and logs every math request, including the sender's name and the specific operation performed.

### **Nexus Client**
* **Identity Handshake:** Provides a unique client name during the initial connection phase and waits for a success acknowledgement from the server.
* **Simulated Workload:** After a successful connection, the client sends multiple math requests at randomized intervals to simulate real-world network usage.
* **Robust Error Handling:** Detects and reports server-side errors, such as division by zero, back to the user interface.
* **Graceful Termination:** Issues a close request to the server to ensure the session is logged and the socket is closed properly.

---

## 🧬 Protocol Design
NexusMath utilizes a structured communication protocol to ensure data integrity:

| Message Type | Format |
| :--- | :--- |
| **Connection** | `CONNECT:[ClientName]` |
| **Acknowledgement** | `SUCCESS_ACK` |
| **Math Request** | `MATH_REQ:[OPERATOR]:[NUM1]:[NUM2]` |
| **Result** | `RESULT:[VALUE]` |
| **Error** | `ERROR:[ErrorMessage]` |
| **Termination** | `CLOSE` |

---

## 🚀 Execution & Setup

### **Compilation**
A `Makefile` is included to automate the build process using `javac`.
```bash
make all
```

### **Running the Application**
The system is configured to run on the default loopback address (`127.0.0.1`) and port `6789`.

1. **Start the Server:**
   ```bash
   make run-server
   ```
2. **Launch a Client:**
   ```bash
   make run-client
   ```

---

## 📂 Project Structure
* **MathServer.java**: The core engine utilizing a shared queue for ordered processing and a server socket for listening.
* **ClientHandler**: The logic responsible for managing individual client threads and session telemetry.
* **MathClient.java**: The end-user application that generates math requests and communicates with the server.
* **MathProtocol.java**: A shared utility class defining the message constants and formatting rules.
* **Makefile**: Automates compilation and cleanup tasks.

---

### Implementation Standards
> [!IMPORTANT]
> NexusMath is designed as a high-efficiency CLI application. All mathematical operations (ADD, SUB, MUL, DIV) are parsed server-side, and the system is built to provide clear visibility into all client-server activity through server-side logging.
