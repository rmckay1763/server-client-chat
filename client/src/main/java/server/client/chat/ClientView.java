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
    private JButton sendButton;
    private JButton clearButton;

    // components for options
    private JPanel optionsPanel;
    private JButton connectButton;
    private JButton disconnectButton;
    private JButton portButton;
    private JButton addressButton;
    private JButton helpButton;

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
        messageField.setText(DEFAULT_MESSAGE);
        
        // send button
        sendButton = new JButton("Send Message");
        sendButton.setToolTipText("Click to send message)");

        // clear button
        clearButton = new JButton("Clear");
        clearButton.setToolTipText("clear all messages in the chat box");

        // add message components
        messagePanel = new JPanel();
        messagePanel.add(messageField);
        messagePanel.add(sendButton);
        messagePanel.add(clearButton);
        add(messagePanel, BorderLayout.SOUTH);
    }

    /**
     * Initializes and adds the options components to the view.
     * Options: connect, disconnect, update port, update address, help.
     */
    private void addOptionsPanel()
    {
        // connect button
        connectButton = new JButton("Connect");
        connectButton.setToolTipText("Connect to the server");

        // disconnect button
        disconnectButton = new JButton("Disconnect");
        disconnectButton.setToolTipText("Terminate a current connection");

        // update port button
        portButton = new JButton("Update Port");
        portButton.setToolTipText("Update the port for connections");

        // update address button
        addressButton = new JButton("Update Address");
        addressButton.setToolTipText("Update the target server address");

        // help button
        helpButton = new JButton("Help");
        helpButton.setToolTipText("Dispplay the help window");

        // add options to view
        optionsPanel = new JPanel();
        optionsPanel.add(connectButton);
        optionsPanel.add(disconnectButton);
        optionsPanel.add(portButton);
        optionsPanel.add(addressButton);
        optionsPanel.add(helpButton);
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

    public void addSendButtonListener(ActionListener listener) {
        sendButton.addActionListener(listener);
        messageField.addActionListener(listener);
    }

    public void addClearButtonListener(ActionListener listener) {
        clearButton.addActionListener(listener);
    }

    public void addConnectButtonListener(ActionListener listener) {
        connectButton.addActionListener(listener);
    }

    public void addDisconnectButtonListener(ActionListener listener) {
        disconnectButton.addActionListener(listener);
    }

    public void addPortButtonListener(ActionListener listener) {
        portButton.addActionListener(listener);
    }

    public void addAddressButtonListener(ActionListener listener) {
        addressButton.addActionListener(listener);
    }

    public void addHelpButtonListener(ActionListener listener) {
        helpButton.addActionListener(listener);
    }
}
    

