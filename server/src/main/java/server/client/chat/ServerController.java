package server.client.chat;
import java.io.IOException;
import java.util.Date;

/**
 * Controller for the server view and the server model.
 * 
 * @author Robert McKay
 */
public class ServerController {
    // class constants
    private static final boolean CLOSED_BY_SERVER = true;
    private static final boolean CLOSED_BY_CLIENT = false;

    // class data members
    private ServerView view;
    private ServerModel model;
    private ClientListener clientListener;
    private MessageListener messageListener;

    /**
     * Inner class. Listens for clients until server is killed.
     */
    private class ClientListener extends Thread {
        @Override
        public void run() {
            while(model.isStarted()) {
                listen();
            }
        }
    }

    /**
     * Inner class. Listens for incomming messages until connection is closed.
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
    public ServerController(ServerView view, ServerModel model) {
        this.view = view;
        this.model = model;
    }

    /**
     * Adds a listener to each action component in the view.
     */
    public void addListeners() {
        view.addStartButtonListener(e -> start());
        view.addDisconnectButtonListener(e -> disconnect(CLOSED_BY_SERVER));
        view.addKillButtonListener(e -> kill());
        view.addPortButtonListener(e -> updatePort());
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
        if (message.equals("") || message.equals(ServerView.DEFAULT_MESSAGE)) {
            throw new IllegalArgumentException("No message entered in message field");
        } else if (message.length() > 1000) {
            throw new IllegalArgumentException("Message exceeds maximamum size (1000 characters)");
        }
        return message;
    }

    /**
     * Sends a message to the client.
     */
    private void sendMessage() {
        try {
            String message = getValidMessage();
            model.sendMessage(message);
            view.addMessage("Server sends - " + new Date() + ": " + message);
        } catch (IllegalArgumentException err) {
            view.addMessage(err.getMessage());
        } catch (ServerModelException err) {
            view.addMessage(err.getMessage());
        }
    }

    /**
     * Receives a message from the server.
     */
    private void receiveMessage() {
        try {
            String message = model.receiveMessage();
            view.addMessage("Client sends - " + new Date() + ": " + message);
            if (message.equals("connection terminated by client")) {
                disconnect(CLOSED_BY_CLIENT);
            }
        } catch (ServerModelException err) {
            return;
        }
    }

    /**
     * Starts the server
     */
    private void start() {
        try {
            model.start();
            clientListener = new ClientListener();
            clientListener.start();
            view.addMessage(
                "Server started successfully.\n" +
                "Address of server: " + model.getServerAddress() + "\n" +
                "Listening for clients on port " + model.getPort()
            );
        } catch (ServerModelException err) {
            view.addMessage(err.getMessage());
        }
    }

    /**
     * Listens for a client on the open connection.
     */
    private void listen() {
        try {
            model.connect();
            messageListener = new MessageListener();
            messageListener.start();
            view.addMessage("Connection established with client");
        } catch (ServerModelException err) {
            view.addMessage(err.getMessage());
        } catch (IOException err) {
            return;
        }
    }

    /**
     * Terminates the current connection with the client.
     */
    private void disconnect(boolean closedByServer) {
        try {
            if (closedByServer) {
                model.sendMessage("connection terminated by server");
            }
            model.disconnect();
            view.addMessage("Disconnected from client");
        } catch (ServerModelException err) {
            view.addMessage(err.getMessage());
        }
    }

    /**
     * Terminates the server.
     */
    private void kill() {
        try {
            disconnect(CLOSED_BY_SERVER);
            model.kill();
            view.addMessage("Server is now inactive");
        } catch (ServerModelException err) {
            view.addMessage(err.getMessage());
        }
    }

    /**
     * Update the port number to listen on
     */
    private void updatePort()
    {
        int port;
        String candidate;
        candidate = view.getMessage();
        try {
            port = Integer.parseInt(candidate);
            model.setPort(port);
            view.addMessage("Port number updated successfully");
        } catch (ServerModelException err) {
            view.addMessage(err.getMessage());
        } catch (NumberFormatException nfe) {
            view.addMessage("Port number not a valid integer");
        }
    }

    /**
     * Displays the help window
     */
    private void help() {
        ServerHelp helpWindow = new ServerHelp();
        helpWindow.setVisible(true);
    }
}
