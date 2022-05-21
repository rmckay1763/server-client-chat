package server.client.chat;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import java.awt.Font;

/**
 * Displays help instructions for the client chat program.
 * 
 * @author Robert McKay
 */
public class ClientHelp extends JFrame
{
    private static final long serialVersionUID = 1L;
    private static final int WIDTH = 500;
    private static final int HEIGHT = 400;
    private static String helpText = 
        "\nUse the 'Connect' button to establish a connection with the server.\n" +
        "Use the 'Disconnect' button to terminate a current connection.\n\n" +
        "Port number: client uses a default port of 5000. To update the port,\n" +
        "enter a valid port in the text box at the bottom and click 'Update Port.\n\n" +
        "Server Address: client uses the loopback address as the default. To update\n" +
        "the target address, enter a valid host in the text box at the bottom and\n" +
        "click the 'Update Address' button.\n\n" +
        "-------------------- Sending/Receving Messages -----------------------\n" +
        "Enter a message in the text box at the bottom and click 'Send Message.'\n" +
        "Messages from the server will automatically appear in the chat area.\n" +
        "Use the 'Clear' button to remove all messages from the chat box";

    /**
     * Constructor.
     */
    public ClientHelp()
    {
        super("Client Help");
        setSize(WIDTH, HEIGHT);
        JTextArea text = new JTextArea();
        Font font = new Font("SansSerif", Font.PLAIN, 14);
        text.setFont(font);
        text.setText(helpText);
        add(text);
    }
}
