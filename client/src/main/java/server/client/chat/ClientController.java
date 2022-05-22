package server.client.chat;

import java.net.UnknownHostException;
import java.util.Date;
import java.net.InetAddress;

/**
 * Controller for the client view and the client model.
 * 
 * @author Robert McKay
 */
public class ClientController {
    // class constants
    private static final boolean CLOSED_BY_CLIENT = true;
    private static final boolean CLOSED_BY_SERVER = false;

    // class attributes
    private ClientView view;
    private ClientModel model;
    private MessageListener messageListener;

    /**
     * Inner class. Listens for incoming messages. Runs in a separate thread.
     */
    private class MessageListener extends Thread {
        @Override
        public void run() {
            while (model.isConnected()) {
                receiveMessage();
            }
        }
    }

    /**
     * Constructor.
     * @param view The view associated with this controller.
     * @param model The model associated with this controller.
     */
    public ClientController(ClientView view, ClientModel model) {
        this.view = view;
        this.model = model;
    }

    /**
     * Adds a listener to each action component in the view.
     */
    public void addListeners() {
        view.addConnectButtonListener(e -> connect());
        view.addDisconnectButtonListener(e -> disconnect(CLOSED_BY_CLIENT));
        view.addPortButtonListener(e -> updatePort());
        view.addAddressButtonListener(e -> updateAddress());
        view.addHelpButtonListener(e -> help());
        view.addSendButtonListener(e -> sendMessage());
        view.addClearButtonListener(e -> view.clear());
    }

    /**
     * Gets and validates the message from the view input field.
     * @return Valid message from the view.
     * @throws IllegalArgumentException If the message is empty or exceeds 1000 characters.
     */
    private String getValidMessage() throws IllegalArgumentException {
        String message = view.getMessage();
        if (message.equals("") || message.equals(ClientView.DEFAULT_MESSAGE)) {
            throw new IllegalArgumentException("No message entered in message field");
        } else if (message.length() > 1000) {
            throw new IllegalArgumentException("Message exceeds maximamum size (1000 characters)");
        }
        return message;
    }

    /**
     * Sends a message to the server.
     */
    private void sendMessage() {
        try {
            String message = getValidMessage();
            model.sendMessage(message);
            view.addMessage("Client sends - " + new Date() + ": " + message);
        } catch (IllegalArgumentException err) {
            view.addMessage(err.getMessage());
        } catch (ClientModelException err) {
            view.addMessage(err.getMessage());
        }
    }

    /**
     * Receives a message from the server.
     */
    private void receiveMessage() {
        try {
            String message = model.receiveMessage();
            view.addMessage("Server sends - " + new Date() + ": " + message);
            if (message.equals("connection terminated by server")) {
                disconnect(CLOSED_BY_SERVER);
            }
        } catch (ClientModelException err) {
            return;
        }
    }

    /**
     * Connects the client to the server.
     */
    private void connect() {
        try {
            model.connect();
            view.addMessage("Addres of client: " + model.getClientAddress());
            view.addMessage("Connected with server!");
            messageListener = new MessageListener();
            messageListener.start();
        } catch (ClientModelException err) {
            view.addMessage(err.getMessage());
        }
    }

     /**
      * Terminates the current connection with the server.
      * @param closedByClient True if the client closed the connection.
      */
    private void disconnect(boolean closedByClient) {
        try {
            if (closedByClient) {
                model.sendMessage("connection terminated by client");
            }
            model.disconnect();
            view.addMessage("Disconnected from Server");
        } catch (ClientModelException err) {
            view.addMessage(err.getMessage());
        }
    }

    /**
     * Update the port number used for connections.
     */
    private void updatePort() {
        int port;
        String candidate;
        candidate = view.getMessage();
        try {
            port = Integer.parseInt(candidate);
            model.setPort(port);
            view.addMessage("Port updated successfully");
        } catch (ClientModelException err) {
            view.addMessage(err.getMessage());
        } catch (NumberFormatException err) {
            view.addMessage("Port number not a valid integer");
        }
    }

    /**
     * Update the target server address to connect to.
     */
    private void updateAddress() {
        String candidate = view.getMessage();
        try {
            InetAddress address = InetAddress.getByName(candidate);
            model.setServerAddress(address);
            view.addMessage("Target server address updated successfully!");
        } catch (UnknownHostException err) {
            view.addMessage("Failed to verify address");
        } catch (ClientModelException err) {
            view.addMessage(err.getMessage());
        }
    }

    /**
     * Displays the help window.
     */
    private void help() {
        ClientHelp helpWindow = new ClientHelp();
        helpWindow.setVisible(true);
    }
}
