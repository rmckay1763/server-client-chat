package server.client.chat;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Date;

/**
 * Controller for the server view and the server model.
 * 
 * @author Robert McKay
 */
public class ServerController
{
    // class constants
    private static final boolean CLOSED_BY_SERVER = true;
    private static final boolean CLOSED_BY_CLIENT = false;
    private static final boolean SHOW_FEEDBACK = true;
    private static final boolean SHOW_NO_FEEDBACK = false;

    // class data members
    private boolean isListening = false;
    private ServerView view;
    private ServerModel model;
    private ViewListener viewListener;
    private ClientListener clientListener;
    private MessageListener messageListener;

    /**
     * Inner class. Listens for clients until server is killed.
     */
    private class ClientListener extends Thread
    {
        @Override
        public void run()
        {
            while(model.isStarted())
            {
                listen();
            }
        }
    }

    /**
     * Inner class. Listens for incomming messages until connection is closed.
     */
    private class MessageListener extends Thread
    {
        @Override
        public void run()
        {
            while (model.isConnected())
            {
                receiveMessage();
            }
        }
    }

    /**
     * Inner class. Listens for the events from the view.
     */
    private class ViewListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent event)
        {
            String command = event.getActionCommand();
            if (command.equals(ServerView.START))
            {
                start();
            }
            else if (command.equals(ServerView.DISCONNECT))
            {
                disconnect(CLOSED_BY_SERVER, SHOW_FEEDBACK);
            }
            else if (command.equals(ServerView.KILL))
            {
                kill();
            }
            else if (command.equals(ServerView.UPDATE_PORT))
            {
                updatePort();
            }
            else if (command.equals(ServerView.HELP))
            {
                help();
            }
            else if (command.equals(ServerView.SEND))
            {
                send();
            }
            else if (command.equals(ServerView.CLEAR))
            {
                view.clear();
            }
        }
    }

    /**
     * Constructor.
     * @param view The view associated with this controller.
     * @param model The model associated with this controller.
     */
    public ServerController(ServerView view, ServerModel model)
    {
        this.view = view;
        this.model = model;
        viewListener = new ViewListener();
        view.addListener(viewListener);
    }

    /**
     * Sends a message to the client.
     */
    private void send()
    {
        String message = view.getMessage();
        if (!model.isConnected())
        {
            message = "Not connected with a client";
        }
        else if (message.equals("") || message.equals(ServerView.DEFAULT_MESSAGE))
        {
            message = "No message entered in message field";
        }
        else if (message.length() > 1000)
        {
            message = "Message exceeds maximamum size (1000 characters)";
        }
        else if (model.sendMessage(message))
        {
            message = "Server sends - " + new Date() + ": " + message;
        }
        else
        {
            message = "Failed to send message";
        }
        view.addMessage(message);
    }

    /**
     * Receives a message from the server.
     */
    private void receiveMessage()
    {
        boolean terminate = false;
        String message = model.receiveMessage();
        if (message != null)
        {
            if (message.equals("connection terminated by client"))
            {
                terminate = true;
            }
            message = "Client sends - " + new Date() + ": " + message;
            view.addMessage(message);
        }
        if(terminate)
        {
            disconnect(CLOSED_BY_CLIENT, SHOW_FEEDBACK);
        }
    }

    /**
     * Starts the server
     */
    private void start()
    {
        String feedback;
        if (model.isStarted())
        {
            feedback = "Server already started";
        }
        else if (model.start())
        {
            clientListener = new ClientListener();
            clientListener.start();
            isListening = true;
            feedback = "Server started successfully.\n" +
                       "Address of server: " + model.getServerAddress() + "\n" +
                       "Listening for clients on port " + model.getPort();
        } else
        {
            feedback = "Failed to start server";
        }
        view.addMessage(feedback);
    }

    /**
     * Listens for a client on the open connection.
     */
    private void listen()
    {
        if (model.connect())
        {
            messageListener = new MessageListener();
            messageListener.start();
            view.addMessage("Connected established with client");
        }
    }

    /**
     * Terminates the current connection with the client.
     */
    private void disconnect(boolean closedByServer, boolean showFeedback)
    {
        if (model.isConnected())
        {
            if (closedByServer)
            {
                model.sendMessage("connection terminated by server");
            }
            model.disconnect();
            if (showFeedback)
            {
                view.addMessage("Disconnected from client");
            }
        }
        else if(showFeedback)
        {
            view.addMessage("Already disconnected");
        }
        if (isListening && showFeedback)
        {
            view.addMessage("Listening for clients on port " + model.getPort());
        }
    }

    /**
     * Terminates the server.
     */
    private void kill()
    {
        String feedback;
        if (model.isStarted())
        {
            disconnect(CLOSED_BY_SERVER, SHOW_NO_FEEDBACK);
            model.kill();
            isListening = false;
            feedback = "Server is now inactive";
        }
        else
        {
            feedback = "Server already stopped";
        }
        view.addMessage(feedback);
    }

    /**
     * Update the port number to listen on
     */
    private void updatePort()
    {
        int port;
        String candidate, feedback;
        candidate = view.getMessage();
        if (model.isStarted())
        {
            feedback = "Kill server before updating port";
        }
        else
        {
            try
            {
                port = Integer.parseInt(candidate);
                if (model.setPort(port))
                {
                    feedback = "Port updated successfully";
                }
                else // new port must be out of range
                {
                    feedback = "Port number is out of range [0 - 65535]";
                }
            }
            catch (NumberFormatException nfe)
            {
                feedback = "Invalid input. Enter a number [0 - 65536]";
            }
        }
        view.addMessage(feedback);
    }

    /**
     * Displays the help window
     */
    private void help()
    {
        ServerHelp helpWindow = new ServerHelp();
        helpWindow.setVisible(true);
    }
}
