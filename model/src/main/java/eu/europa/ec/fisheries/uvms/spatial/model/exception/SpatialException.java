package eu.europa.ec.fisheries.uvms.spatial.model.exception;

public class SpatialException extends Exception {
    private static final long serialVersionUID = 1L;

    public SpatialException(String message) {
        super(message);
    }

    public SpatialException(String message, Throwable cause) {
        super(message, cause);
    }

}
