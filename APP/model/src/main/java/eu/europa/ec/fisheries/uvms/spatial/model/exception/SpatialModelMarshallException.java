package eu.europa.ec.fisheries.uvms.spatial.model.exception;

public class SpatialModelMarshallException extends SpatialModelMapperException  {
    private static final long serialVersionUID = 7582161942682172612L;

    public SpatialModelMarshallException(String message) {
        super(message);
    }

    public SpatialModelMarshallException(String message, Throwable cause) {
        super(message, cause);
    }

}
