package eu.europa.ec.fisheries.uvms.spatial.message.exception;

// TODO put this class in common lib to all modules
public class MovementMessageException extends Exception {

    public MovementMessageException() {
    }

    public MovementMessageException(String message) {
        super(message);
    }

    public MovementMessageException(String message, Throwable cause) {
        super(message, cause);
    }

}
