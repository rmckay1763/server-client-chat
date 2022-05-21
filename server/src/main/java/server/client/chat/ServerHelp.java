package server.client.chat;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import java.awt.Font;

/**
 * Displays help instructions for the client chat program.
 * 
 * @author Robert McKay
 */
public class ServerHelp extends JFrame
{
    private static final long serialVersionUID = 1L;
    private static final int WIDTH = 500;
    private static final int HEIGHT = 400;
    private static String helpText = 
        "\nUse the 'Start Server' button to activate the server.\n" +
        "Use the 'Disconnect' button to terminate a current connection.\n" +
        "Use the 'Kill Server' button to deactivate the server.\n\n" +
        "Port number: Server uses a default port of 5000. To update the port,\n" +
        "enter a valid port in the text box at the bottom and click 'Update Port.\n\n" +
        "-------------------- Sending/Receving Messages -----------------------\n" +
        "Enter a message in the text box at the bottom and click 'Send Message.'\n" +
        "Messages from the server will automatically appear in the chat area.\n" +
        "Use the 'Clear' button to remove all messages from the chat box";

    /**
     * Constructor.
     */
    public ServerHelp()
    {
        super("Server Help");
        setSize(WIDTH, HEIGHT);
        JTextArea text = new JTextArea();
        Font font = new Font("SansSerif", Font.PLAIN, 14);
        text.setFont(font);
        text.setText(helpText);
        add(text);
    }
}
