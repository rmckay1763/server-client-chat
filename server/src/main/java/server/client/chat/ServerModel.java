package server.client.chat;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;

/**
 * Server listens for and connects to a client and opens an I/O stream.
 * 
 * @author Robert McKay
 */
public class ServerModel {
    private boolean isStarted;
    private boolean isConnected;
    private int port;
    private InetAddress serverAddress;
    private ServerSocket server;
    private Socket connection;
    private DataOutputStream outputStream;
    private BufferedReader inputStream;

    /**
     * Constructor.
     * @param port The port number to use for connections.
     */
    public ServerModel(int port) {
        isStarted = false;
        isConnected = false;
        this.port = port;
        serverAddress = InetAddress.getLoopbackAddress();
    }

    /**
     * Accessor method for isStarted.
     * @return True if the ServerSocket is initialized, false otherwise.
     */
    public boolean isStarted() {
        return isStarted;
    }

    /**
     * Accessor method for isConnected.
     * @return True if an open I/O stream exits with a client, false otherwise.
     */
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * Accessor method for the server address.
     * @return The local address of the this server.
     */
    public InetAddress getServerAddress() {
        return serverAddress;
    }

    /**
     * Accessor method for the port.
     * @return The listening port associated with this server.
     */
    public int getPort() {
        return port;
    }

    /**
     * Mutator method for the port.
    * @param port The new port to use for connections.
    * @throws ServerModelException If the port is out of range [0 -65535].
    */
    public void setPort(int port) throws ServerModelException {
        if (port < 0 || port > 65535) {
            throw new ServerModelException("Port number out of range");
        }
        if (isStarted) {
            throw new ServerModelException("kill server before changing port");
        }
        this.port = port;
    }

    /**
     * Initializes the ServerSocket with the stored port value.
     * @throws ServerModelException If server already started or fails to start.
     */
    public void start() throws ServerModelException {
        if(isStarted) {
            throw new ServerModelException("Server already started");
        }
        try {
            server = new ServerSocket(port);
            isStarted = true;
        } catch (IOException err) {
            throw new ServerModelException("Failed to start server");
        }
    }

    /**
     * Waits for a client to connect and establishes I/O stream.
     * @return True if I/O stream establishes successfully, false otherwise.
     */

    /**
     * Waits for a client to connect and establishes I/O stream.
     * @throws ServerModelException If server not started.
     * @throws IOException If I/O stream connection fails.
     */
    public void connect() throws ServerModelException, IOException {
        if (!isStarted) {
            throw new ServerModelException("Server not started");
        }
        try {
            connection = server.accept();
            inputStream = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            outputStream = new DataOutputStream(connection.getOutputStream());
            isConnected = true;
        } catch (IOException err) {
            throw err;
        }
    }

    /**
     * Terminates the current I/O stream and current connection.
     * @return True if I/O stream closes successfully, false otherwise.
     */

    /**
     * Terminates the current I/O stream and current connection.
     * @throws ServerModelException If not connected to a client or fails to close I/O streams.
     */
    public void disconnect() throws ServerModelException {
        if (!isConnected()) {
            throw new ServerModelException("Server not connected to a client");
        }
        try {
            outputStream.close();
            inputStream.close();
            connection.close();
            isConnected = false;
        } catch(IOException err) {
            throw new ServerModelException(err.getMessage());
        }
    }

    /**
     * Terminates the ServerSocket associated with this Server.
     * @throws ServerModelException If server not started or fails to close connection.
     */
    public void kill() throws ServerModelException {
        if (!isStarted()) {
            throw new ServerModelException("Server already inactive");
        }
        try {
            server.close();
            isStarted = false;
        } catch(IOException err) {
            throw new ServerModelException(err.getMessage());
        }
    }

    /**
     * Pushes a message onto the output stream.
     * @param message The message to push.
     * @throws ServerModelException If not connected with a client or if fails to write to output stream.
     */
    public void sendMessage(String message) throws ServerModelException {
        if (!isConnected) {
            throw new ServerModelException("Not connected with a client");
        }
        try {
            outputStream.writeBytes(message + "\n");
        } catch (IOException err) {
            throw new ServerModelException(err.getMessage());
        }
    }

    /**
     * Pulls a message form the input stream.
     * @returnThe message from the input stream.
     * @throws ServerModelException If fails to read from the input stream.
     */
    public String receiveMessage() throws ServerModelException {
        try {
            return inputStream.readLine();
        } catch (IOException err) {
            throw new ServerModelException(err.getMessage());
        }
    }
}
