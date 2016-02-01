package eu.europa.ec.fisheries.uvms.spatial.model.exception;

public class SpatialModelMapperException extends SpatialModelException {
    private static final long serialVersionUID = 1L;

    public SpatialModelMapperException(String message) {
        super(message);
    }

    public SpatialModelMapperException(String message, Throwable cause) {
        super(message, cause);
    }

}
