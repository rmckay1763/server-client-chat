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
 * The view for the Server. Follows MVC design.
 * 
 * @author Robert McKay
 */
public class ServerView extends JFrame {
    // public class constants
    public static final String DEFAULT_MESSAGE = "<Enter message here>";

    // private class constants
    private static final long serialVersionUID = 1L;
	private static final int WIDTH = 850;
    private static final int HEIGHT = 500;
    private static final int PADDING = 10;

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
    private JButton startButton;
    private JButton disconnectButton;
    private JButton killButton;
    private JButton portButton;
    private JButton helpButton;

    /**
     * Constructor.
     */
    public ServerView() {
        super("Chat: Server Side");
        setDefaultCloseOperation((JFrame.EXIT_ON_CLOSE));
        setSize(WIDTH, HEIGHT);
        addChatPanel();
        addMessagePanel();
        addOptionsPanel();
        setLocation(PADDING, PADDING);
        setVisible(true);
    }

    /**
     * Initializes and adds the chat area components to the view.
     */
    private void addChatPanel() {
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
    private void addMessagePanel() {
        // message input field
        messageField = new JTextField(50);
        messageField.setText(DEFAULT_MESSAGE);

        // send button
        sendButton = new JButton("Send Message");
        sendButton.setToolTipText("Click to send message");

        // clear button
        clearButton = new JButton("Clear");
        clearButton.setToolTipText("Clear all messages in the chat box");

        // add message components
        messagePanel = new JPanel();
        messagePanel.add(messageField);
        messagePanel.add(sendButton);
        messagePanel.add(clearButton);
        add(messagePanel, BorderLayout.SOUTH);
    }

    /**
     * Initializes and adds the options components to the view.
     * Options: start, disconnect, kill, update port, help.
     */
    private void addOptionsPanel() {
        // start button
        startButton = new JButton("Start Server");
        startButton.setToolTipText("Make the server active");

        // disconnect button
        disconnectButton = new JButton("Disconnect");
        disconnectButton.setToolTipText("Terminate a current connection");

        // kill button
        killButton = new JButton("Kill Server");
        killButton.setToolTipText("Make the server inactive");

        // update port button
        portButton = new JButton("Update Port");
        portButton.setToolTipText("Update the listening port");

        // help button
        helpButton = new JButton("Help");
        helpButton.setToolTipText("Display the help window");

        // add options to view
        optionsPanel = new JPanel();
        optionsPanel.add(startButton);
        optionsPanel.add(disconnectButton);
        optionsPanel.add(killButton);
        optionsPanel.add(portButton);
        optionsPanel.add(helpButton);
        add(optionsPanel, BorderLayout.NORTH);
    }

    /**
     * Gets the text in the send message text field
     * @return The message in the send message text field
     */
    public String getMessage() {
        return messageField.getText();
    }

    /**
     * Appends a message to the next line of the chat area.
     * @param message The message to append.
     */
    public void addMessage(String message) {
        chatText.append("\n" + message);
    }

    /**
     * Clear all text in the chat box.
     */
    public void clear() {
        chatText.setText(null);
    }

    /**
     * Adds a listener to the send message button in the view.
     * @param listener ActionListener to add to the send button.
     */
    public void addSendButtonListener(ActionListener listener) {
        sendButton.addActionListener(listener);
        messageField.addActionListener(listener);
    }

    /**
     * Adds a listener to the clear button in the view.
     * @param listener ActionListener to add to the clear button.
     */
    public void addClearButtonListener(ActionListener listener) {
        clearButton.addActionListener(listener);
    }

    /**
     * Adds a listener to the start server button in the view.
     * @param listener ActionListener to add to the start server button.
     */
    public void addStartButtonListener(ActionListener listener) {
        startButton.addActionListener(listener);
    }

    /**
     * Adds a listener to the disconnect button in the view.
     * @param listener ActionListener to add to the disconnect button.
     */
    public void addDisconnectButtonListener(ActionListener listener) {
        disconnectButton.addActionListener(listener);
    }

    /**
     * Adds a listener to the kill server button in the view.
     * @param listener ActionListener to add to the kill server button.
     */
    public void addKillButtonListener(ActionListener listener) {
        killButton.addActionListener(listener);
    }

    /**
     * Adds a listener to the update port button in the view.
     * @param listener ActionListener to add to the update port view.
     */
    public void addPortButtonListener(ActionListener listener) {
        portButton.addActionListener(listener);
    }

    /**
     * Adds a listener to the help button in the view.
     * @param listener ActionListener to add to the help button.
     */
    public void addHelpButtonListener(ActionListener listener) {
        helpButton.addActionListener(listener);
    }
}
