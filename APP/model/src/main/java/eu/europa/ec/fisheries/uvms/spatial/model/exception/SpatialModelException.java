package eu.europa.ec.fisheries.uvms.spatial.model.exception;

public class SpatialModelException extends SpatialException {
    private static final long serialVersionUID = 1L;

    public SpatialModelException(String message) {
        super(message);
    }

    public SpatialModelException(String message, Throwable cause) {
        super(message, cause);
    }
}
