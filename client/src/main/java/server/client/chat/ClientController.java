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
        view.addSendButtonListener(e -> send());
        view.addClearButtonListener(e -> view.clear());
    }

    /**
     * Sends a message to the server.
     */
    private void send() {
        String message = view.getMessage();
        if (!model.isConnected()) {
            message = "Not connected to the server";
        } else if (message.equals("") || message.equals(ClientView.DEFAULT_MESSAGE)) {
            message = "No message entered in message field";
        } else if (message.length() > 1000) {
            message = "Message exceeds maximamum size (1000 characters)";
        } else if (model.sendMessage(message)) {
            message = "Client sends - " + new Date() + ": " + message;
        } else {
            message = "Failed to send message";
        }
        view.addMessage(message);
    }

    /**
     * Receives a message from the server.
     */
    private void receiveMessage() {
        boolean terminate = false;
        String message = model.receiveMessage();
        if (message != null) {
            if (message.equals("connection terminated by server")) {
                terminate = true;
            }
            message = "Server sends - " + new Date() + ": " + message;
            view.addMessage(message);
        }
        if(terminate) {
            disconnect(CLOSED_BY_SERVER);
        }
    }

    /**
     * Connects the client to the server.
     */
    private void connect() {
        if (model.isConnected()) {
            view.addMessage("Already connected");
            return;
        }
        if (model.connect()) {
            view.addMessage("Addres of client: " + model.getClientAddress());
            view.addMessage("Connected with server!");
            messageListener = new MessageListener();
            messageListener.start();
        } else {
            view.addMessage("Failed to connect.");
        }
    }

     /**
      * Terminates the current connection with the server.
      * @param closedByClient True if the client closed the connection.
      */
    private void disconnect(boolean closedByClient) {
        if (model.isConnected()) {
            if (closedByClient) {
                model.sendMessage("connection terminated by client");
            }
            model.disconnect();
            view.addMessage("Disconnected from Server");
        } else {
            view.addMessage("Already disconnected");
        }
    }

    /**
     * Update the port number use for connections.
     */
    private void updatePort() {
        int port;
        String candidate, feedback;
        candidate = view.getMessage();
        if (model.isConnected()) {
            feedback = "Disconnect client before updating port";
        } else {
            try {
                port = Integer.parseInt(candidate);
                if (model.setPort(port)) {
                    feedback = "Port updated successfully";
                } else {
                    feedback = "Port number is out of range [0 - 65535]";
                }
            } catch (NumberFormatException nfe) {
                feedback = "Invalid input. Enter a number [0 - 65536]";
            }
        }
        view.addMessage(feedback);
    }

    /**
     * Update the target server address to connect to.
     */
    private void updateAddress() {
        InetAddress address;
        String candidate, feedback;
        candidate = view.getMessage();
        if (model.isConnected()) {
            view.addMessage("Disconnect before updating server address");
        } else {
            try {
                address = InetAddress.getByName(candidate);
                model.setServerAddress(address);
                feedback = "Target server address updated successfully!";
            } catch (UnknownHostException uhe) {
                feedback = "Failed to verify address";
            }
            view.addMessage(feedback);
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
