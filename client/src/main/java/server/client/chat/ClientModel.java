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
        } catch (UnknownHostException uhe) {
            clientAddress = InetAddress.getLoopbackAddress();
        }
    }

    /**
     * Accessor method for isConnected
     * @return True if the an open I/O stream exists with the server, false otherwise.
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
     * @param address The address of the target server.
     */
    public void setServerAddress(InetAddress address) {
        serverAddress = address;
    }

    /**
     * Mutator method for the port.
     * @param port The new port to use for connections.
     * @return True if the port is in range (0 - 65535), false otherwise.
     */
    public boolean setPort(int port) {
        if (port < 0 || port > 65535) {
            return false;
        } else {
            this.port = port;
            return true;
        }       
    }

    /**
     * Establishes a connection and I/O stream the server using the stored server address and port.
     * @return True if the I/O stream establishes successfully, false, otherwise.
     */
    public boolean connect() {
        if (isConnected) {
            return true;
        } try {
            connection = new Socket(serverAddress, port);
            inputStream = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            outputStream = new DataOutputStream(connection.getOutputStream());
            isConnected = true;
            return true;
        } catch (IOException ioe) {
            isConnected = false;
            return false;
        }
    }

    /**
     * Terminates the current I/O stream and current connection.
     * @return True if I/O stream closes successfully, false otherwise.
     */
    public boolean disconnect() {
        if (!isConnected()) {
            return true;
        }
        try {
            outputStream.close();
            inputStream.close();
            connection.close();
            isConnected = false;
            return true;
        } catch(IOException ioe) {
            return false;
        }
    }

    /**
     * Pushes a message into the output stream.
     * @param message The message to push.
     * @return True if the messages sends successfully, false otherwise.
     */
    public boolean sendMessage(String message) {
        if (!isConnected) {
            return false;
        }
        try {
            outputStream.writeBytes(message + "\n");
            return true;
        } catch (IOException ioe) {
            return false;
        }    
    }

    /**
     * Pulls a message form the input stream.
     * @return The message from the input stream. Null if not successful.
     */
    public String receiveMessage() {
        if (!isConnected) {
            return null;
        }
        try {
            return inputStream.readLine();
        } catch (IOException ioe) {
            return null;
        }
    }
}