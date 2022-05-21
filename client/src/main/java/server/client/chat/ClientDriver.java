package server.client.chat;

import java.net.InetAddress;
import javax.swing.SwingUtilities;

/**
 * Driver class for the Client Chat program
 *
 * @author Robert McKay
 */
public class ClientDriver
{
    /**
     * Entry point. 
     * @param args unused.
     */
    public static void main(String[] args)
    {
        int port = 5000;
        InetAddress serverAddress = InetAddress.getLoopbackAddress();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run()
            {
                ClientView view = new ClientView();
                ClientModel model = new ClientModel(serverAddress, port);
                ClientController controller = new ClientController(view, model);
            }
        });
    }
}