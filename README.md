Based on the starter code you provided, here is exactly what should go into each file:

### **1. `MathServer.java` (The Architect)**
[cite_start]This file transforms the basic `TCPServer.java` you were given into a multithreaded machine[cite: 12].
* [cite_start]**Main Loop:** Uses `ServerSocket` to wait for connections on a specific port[cite: 11].
* [cite_start]**Thread Handler:** For every `accept()`, it must create a new thread (or use a Runnable) to handle that specific client[cite: 12].
* [cite_start]**Logging Logic:** Stores the `startTime` when a client connects and calculates the duration when they disconnect[cite: 10, 15].
* [cite_start]**Math Engine:** A method that parses strings like "ADD:5:10" and returns "15"[cite: 13].

### **2. `MathClient.java` (The Tester)**
This replaces your `TCPClient.java` with the automated logic required by the prompt.
* [cite_start]**Handshake:** Immediately sends the user's name (e.g., "Sameeraa") and waits for an acknowledgement from the server[cite: 17].
* [cite_start]**Randomized Loop:** Contains a `for` loop that runs at least 3 times[cite: 18]. [cite_start]Inside, it uses `Thread.sleep()` with a random duration to send math requests at irregular intervals[cite: 18].
* [cite_start]**Termination:** Sends a specific "close" string to the server before exiting the program[cite: 20].

### **3. `MathProtocol.java` (The Language)**
[cite_start]This is a helper class (or interface) that defines the "rules" of your communication[cite: 21].
* [cite_start]**Constants:** Defines strings like `CONNECT`, `MATH_REQ`, and `TERMINATE`[cite: 22, 23].
* [cite_start]**Formatting:** Provides a standard way to build strings so the Server and Client always understand each other (e.g., `ACTION:OPERATOR:NUM1:NUM2`)[cite: 22].

### **4. `Makefile` (The Automation)**
[cite_start]The project explicitly requires a Makefile for submission[cite: 51].
* **`all`:** Compiles all `.java` files using `javac`.
* **`server`:** Runs the `MathServer` class.
* **`client`:** Runs the `MathClient` class.
* **`clean`:** Removes the `.class` files to keep the directory tidy.

### **5. `Project_Report.pdf` & `Design_Document.pdf`**
[cite_start]These are your non-code deliverables[cite: 39, 52].
* [cite_start]**Report:** Must include all names and NetIDs, partner contributions, and screenshots of the server running with multiple clients[cite: 40, 41, 50].
* [cite_start]**Design Doc:** Explains your protocol format and your assumptions (e.g., "We assumed all math inputs are integers")[cite: 28, 42].

---

### **Task Summary for your 3-Person Team**
| File | Assigned To | Primary Responsibility |
| :--- | :--- | :--- |
| **MathServer.java** | **Partner 1** | [cite_start]Multithreading, Math logic, and User Logging[cite: 33]. |
| **MathClient.java** | **Partner 2** | [cite_start]Connection handshake, 3+ random math requests[cite: 34]. |
| **Protocol & Docs** | **Partner 3** | [cite_start]Makefile, Protocol design, and assembling the final PDF report[cite: 35, 36]. |

Since you have the basic TCP code already, your first step as a team should be defining the **Protocol** so Partner 1 and Partner 2 can code their respective files to match.
