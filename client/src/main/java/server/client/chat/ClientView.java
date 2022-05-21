package server.client.chat;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JScrollPane;

/**
 * The view for the client. Follows MVC design.
 * 
 * @author Robert McKay
 */
public class ClientView extends JFrame
{
    // public class constants
    public static final String CONNECT = "connect";
    public static final String DISCONNECT = "disconnect";
    public static final String UPDATE_PORT = "updatePort";
    public static final String UPDATE_ADDRESS = "updateAddress";
    public static final String HELP = "help";
    public static final String SEND = "send";
    public static final String CLEAR = "clear";
    public static final String DEFAULT_MESSAGE = "<Enter message here>";
    
    // private class constants
    private static final long serialVersionUID = 1L;
	private static final int WIDTH = 850;
    private static final int HEIGHT = 500;

    // components for the chat area
    private JPanel chatPanel;
    private JTextArea chatText;
    private JScrollPane chatPane;

    // compenents to send a message
    private JPanel messagePanel;
    private JTextField messageField;
    private JButton send;
    private JButton clear;

    // components for options
    private JPanel optionsPanel;
    private JButton connect;
    private JButton disconnect;
    private JButton updatePort;
    private JButton updateAddress;
    private JButton help;

    /**
     * Constructor.
     */
    public ClientView()
    {
        super("Chat: Client Side");
        setDefaultCloseOperation((JFrame.EXIT_ON_CLOSE));
        setSize(WIDTH, HEIGHT);
        addChatPanel();
        addMessagePanel();
        addOptionsPanel();
        setVisible(true);
    }

    /**
     * Initializes and adds the chat area components to the view.
     */
    private void addChatPanel()
    {
        // chat box
        chatText = new JTextArea(20, 50);
        chatText.setEditable(false);
        chatPane = new JScrollPane(chatText);
        chatPanel = new JPanel();
        chatPanel.add(chatPane);
        add(chatPanel, BorderLayout.CENTER);
    }
    
    /**
     * Initializes and adds the send message components to the view.
     */
    private void addMessagePanel()
    {
        
        // message input field
        messageField = new JTextField(50);
        messageField.setActionCommand(SEND);
        messageField.setText(DEFAULT_MESSAGE);
        
        // send button
        send = new JButton("Send Message");
        send.setActionCommand(SEND);
        send.setToolTipText("Click to send message)");

        // clear button
        clear = new JButton("Clear");
        clear.setActionCommand(CLEAR);
        clear.setToolTipText("clear all messages in the chat box");

        // add message components
        messagePanel = new JPanel();
        messagePanel.add(messageField);
        messagePanel.add(send);
        messagePanel.add(clear);
        add(messagePanel, BorderLayout.SOUTH);
    }

    /**
     * Initializes and adds the options components to the view.
     * Options: connect, disconnect, update port, update address, help.
     */
    private void addOptionsPanel()
    {
        // connect button
        connect = new JButton("Connect");
        connect.setActionCommand(CONNECT);
        connect.setToolTipText("Connect to the server");

        // disconnect button
        disconnect = new JButton("Disconnect");
        disconnect.setActionCommand(DISCONNECT);
        disconnect.setToolTipText("Terminate a current connection");

        // update port button
        updatePort = new JButton("Update Port");
        updatePort.setActionCommand(UPDATE_PORT);
        updatePort.setToolTipText("Update the port for connections");

        // update address button
        updateAddress = new JButton("Update Address");
        updateAddress.setActionCommand(UPDATE_ADDRESS);
        updateAddress.setToolTipText("Update the target server address");

        // help button
        help = new JButton("Help");
        help.setActionCommand(HELP);
        help.setToolTipText("Dispplay the help window");

        // add options to view
        optionsPanel = new JPanel();
        optionsPanel.add(connect);
        optionsPanel.add(disconnect);
        optionsPanel.add(updatePort);
        optionsPanel.add(updateAddress);
        optionsPanel.add(help);
        add(optionsPanel, BorderLayout.NORTH);
    }

    /**
     * Acessor method for the text in the send message text field
     * @return The message in the send message text field
     */
    public String getMessage()
    {
        return messageField.getText();
    }

    /**
     * Appends a message to the next line of the chat area.
     * @param message The message to append.
     */
    public void addMessage(String message)
    {
        chatText.append("\n" + message);
    }

    /**
     * Clear all text in the chat box.
     */
    public void clear()
    {
        chatText.setText(null);
    }

    /**
     * Adds an event listener to the view buttons.
     * @param listener The listener to add.
     */
    public void addListener(ActionListener listener)
    {
        messageField.addActionListener(listener);
        send.addActionListener(listener);
        clear.addActionListener(listener);
        connect.addActionListener(listener);
        disconnect.addActionListener(listener);
        updatePort.addActionListener(listener);
        updateAddress.addActionListener(listener);
        help.addActionListener(listener);
    }

}
    

