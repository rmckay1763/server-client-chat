package server.client.chat;

import javax.swing.SwingUtilities;

/**
 * Driver class for the Server Chat program.
 * 
 * @author Robert McKay
 */

public class ServerDriver
{
    /**
     * Entry point
     * @param args unused.
     */
    public static void main(String[] args)
    {
        int port = 5000;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run()
            {
                ServerView view = new ServerView();
                ServerModel model = new ServerModel(port);
                ServerController controller = new ServerController(view, model);
            }
        });
    }
}
