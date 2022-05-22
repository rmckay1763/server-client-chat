package server.client.chat;

/**
 * Thrown to indicate a runtime exception in the client model.
 */
public class ClientModelException extends RuntimeException {
    
    /**
     * Default constructor.
     */
    public ClientModelException() {
        super();
    }

    /**
     * Overloaded constructor to set a error message.
     * @param message Description of the exception that occurred.
     */
    public ClientModelException(String message) {
        super(message);
    }
}
