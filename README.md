# Server/Client Chat Project

### **Synopsis**

A simple server and client that send messages back and forth via TCP protocol. Implementented in two programs.

---

### **Implementation**

Both programs listen for messages in separate threads to prevent blocking the main thread while waiting for a response. Each program defines three classes that follow the MVC design pattern:

- `[Client/Server]Model` - Represents the socket connection.
- `[Client/Server]View` - User Interface to experience the program.
- `[Client/Server]Controller` - Listens for events from the view and calls methods in the model.

Both programs also have two additional classes:
- `[Client/Server]Driver` - Instantiates compononts inside a `Runnable`.
- `[Client/Server]Help` - Pop out window with program instructions.

---

### **Build**

The project uses gradle to define a multiproject build with two subprojects.

**Build**
- Windows
    ```
    .\gradlew.bat clean build
    ```
- Linux/mac
    ```
    ./gradlew clean build
    ```

**Run**
- Windows
    ```
    .\gradlew.bat clean run
    ```
- Linux/mac
    ```
    ./gradlew clean run
    ```
---

### **Notes**

The default settings configure the server and client to establish a connection on `localhost:5000`. The server provides the option to update the listening port. The client provides options to update the host address and the port to establish a connection with. The `Help` window for each program includes detailed instructions for updating the settings.
	
	