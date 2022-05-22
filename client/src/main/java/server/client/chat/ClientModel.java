package server.client.chat;

import java.net.Socket;
import java.net.UnknownHostException;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;

/**
 * ClientModel connects to a server and opens an I/O stream with the connected server.
 * 
 * @author Robert McKay
 */
public class ClientModel {
    private boolean isConnected;
    private int port;
    private InetAddress clientAddress;
    private InetAddress serverAddress;
    private Socket connection;
    private DataOutputStream outputStream;
    private BufferedReader inputStream;

    /**
     * Constructor.
     * @param address The target address (the server) for establishing a connection.
     * @param port The target port for establishing a connection.
     */
    public ClientModel(InetAddress address, int port) {
        isConnected = false;
        serverAddress = address;
        this.port = port;
        try {
            clientAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException err) {
            clientAddress = InetAddress.getLoopbackAddress();
        }
    }

    /**
     * Accessor method for isConnected
     * @return True if an open I/O stream exists with the server, false otherwise.
     */
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * Accessor method for the address of the client.
     * @return The local address of this client.
     */
    public InetAddress getClientAddress() {
        return clientAddress;
    }

    /**
     * Mutator method for the server address.
     * @param address InetAddress of the target server.
     * @throws ClientModelException If the client has an established connection with a server.
     */
    public void setServerAddress(InetAddress address) throws ClientModelException {
        if (isConnected) {
            throw new ClientModelException("Disconnect before updating server address");
        }
        serverAddress = address;
    }

    /**
     * Mutator method for the port.
     * @param port The new port to use for connections.
     * @throws ClientModelException If the port is out of range or the client is connected to a server.
     */
    public void setPort(int port) throws ClientModelException {
        if (port < 0 || port > 65535) {
            throw new ClientModelException("port number out of range");
        }
        if (isConnected) {
            throw new ClientModelException("disconnect server before changing port");
        }
        this.port = port;      
    }

    /**
     * Establishes a connection and I/O stream to the server.
     * @throws ClientModelException If the client is already connected or the new connection fails.
     */
    public void connect() throws ClientModelException {
        if (isConnected) {
            throw new ClientModelException("Already connected to server");
        } try {
            connection = new Socket(serverAddress, port);
            inputStream = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            outputStream = new DataOutputStream(connection.getOutputStream());
            isConnected = true;
        } catch (IOException err) {
            isConnected = false;
            throw new ClientModelException("Failed to connect to server");
        }
    }

    /**
     * Terminates the current I/O stream and current connection.
     * @throws ClientModelException If the client is already disconnected or if disconnecting fails.
     */
    public void disconnect() throws ClientModelException {
        if (!isConnected) {
            throw new ClientModelException("Client not connected to server");
        }
        try {
            outputStream.close();
            inputStream.close();
            connection.close();
            isConnected = false;
        } catch(IOException err) {
            throw new ClientModelException("Failed to disconnect from the server");
        }
    }

    /**
     * Pushes a message into the output stream.
     * @param message The message to push.
     * @throws ClientModelException If the client is not connected or fails to send the message.
     */
    public void sendMessage(String message) throws ClientModelException {
        if (!isConnected) {
            throw new ClientModelException("Client not connected to server");
        }
        try {
            outputStream.writeBytes(message + "\n");
        } catch (IOException err) {
            throw new ClientModelException(err.getMessage());
        }    
    }

    /**
     * Pulls a message form the input stream.
     * @throws ClientModelException If the input stream fails to read from the server.
     */
    public String receiveMessage() throws ClientModelException  {
        try {
            return inputStream.readLine();
        } catch (IOException err) {
            throw new ClientModelException(err.getMessage());
        }
    }
}