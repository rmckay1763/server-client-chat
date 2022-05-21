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
public class ServerModel
{
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
    public ServerModel(int port)
    {
        isStarted = false;
        isConnected = false;
        this.port = port;
        serverAddress = InetAddress.getLoopbackAddress();
    }

    /**
     * Accessor method for isStarted.
     * @return True if the ServerSocket is initialized, false otherwise.
     */
    public boolean isStarted()
    {
        return isStarted;
    }

    /**
     * Accessor method for isConnected.
     * @return True if an open I/O stream exits with a client, false otherwise.
     */
    public boolean isConnected()
    {
        return isConnected;
    }

    /**
     * Accessor method for the server address.
     * @return The local address of the this server.
     */
    public InetAddress getServerAddress()
    {
        return serverAddress;
    }

    /**
     * Accessor method for the port.
     * @return The listening port associated with this server.
     */
    public int getPort()
    {
        return port;
    }

    /**
     * Mutator method for the port.
     * @param port The new port to use for connections.
     * @return True if the port is in range (0 - 65535), false otherwise.
     */
    public boolean setPort(int port)
    {
        if (port < 0 || port > 65535)
        {
            return false;
        }
        else
        {
            this.port = port;
            return true;
        }
    }

    /**
     * Initializes the ServerSocket with the stored port value.
     * @return True if ServerSocket successfully initializes, false otherwise.
     */
    public boolean start()
    {
        try
        {
            server = new ServerSocket(port);
            isStarted = true;
            return true;
        }
        catch (IOException ioe)
        {
            return false;
        }
    }

    /**
     * Waits for a client to connect and establishes I/O stream.
     * @return True if I/O stream establishes successfully, false otherwise.
     */
    public boolean connect()
    {
        if (!isStarted)
        {
            return false;
        }
        try
        {
            connection = server.accept();
            inputStream = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            outputStream = new DataOutputStream(connection.getOutputStream());
            isConnected = true;
            return true;
        }
        catch (IOException ioe)
        {
            isConnected = false;
            return false;
        }
    }

    /**
     * Terminates the current I/O stream and current connection.
     * @return True if I/O stream closes successfully, false otherwise.
     */
    public boolean disconnect()
    {
        if (!isConnected())
        {
            return true;
        }
        try
        {
            outputStream.close();
            inputStream.close();
            connection.close();
            isConnected = false;
            return true;
        }
        catch(IOException ioe)
        {
            return false;
        }
    }

    /**
     * Terminates the ServerSocket associated with this Server.
     * @return True if all open connections close successfully.
     */
    public boolean kill()
    {
        if (!isStarted())
        {
            return true;
        }
        disconnect();
        try
        {
            server.close();
            isStarted = false;
            return true;
        }
        catch(IOException ioe)
        {
            return false;
        }
    }

    /**
     * Pushes a message into the output stream.
     * @param message The message to push.
     * @return True if the messages sends successfully, false otherwise.
     */
    public boolean sendMessage(String message)
    {
        if (!isConnected)
        {
            return false;
        }
        try
        {
            outputStream.writeBytes(message + "\n");
            return true;
        }
        catch (IOException ioe)
        {
            return false;
        }
    }

    /**
     * Pulls a message form the input stream.
     * @return The message from the input stream. Null if not successful.
     */
    public String receiveMessage()
    {
        if (!isConnected)
        {
            return null;
        }
        try
        {
            return inputStream.readLine();
        }
        catch (IOException ioe)
        {
            return null;
        }
    }
}
