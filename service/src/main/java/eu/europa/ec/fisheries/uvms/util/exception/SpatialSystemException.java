package eu.europa.ec.fisheries.uvms.util.exception;

/**
 * Represents a system exception, that should not be presented to the client in details. (User gets internal error
 * message). Details should be written to logs.
 * <p/>
 * Created by kopyczmi on 17-Aug-15.
 */
public class SpatialSystemException extends RuntimeException {

    private SpatialSystemErrors error;

    // Following are any parameters you want to associate with this exception.
    // Specifically, it will be used to generate parameterized error message
    // based on code.
    private Object[] params = null;

    public SpatialSystemException(final String msg) {
        super(msg);
    }

    public SpatialSystemException(final SpatialSystemErrors pError) {
        super(pError.message());
        error = pError;
    }

    public SpatialSystemException(final SpatialSystemErrors pError, final Object... pParams) {
        super(String.format(pError.message(), pParams));
        params = pParams;
        error = pError;
    }

    public SpatialSystemException(final SpatialSystemErrors pError, Exception ex, final Object... pParams) {
        super(String.format(pError.message(), pParams), ex);
        params = pParams;
        error = pError;
    }

    /**
     * Constructs a ApplicationException object and stores the stack trace from the Exception object.
     *
     * @param ex Exception object
     */

    public SpatialSystemException(final Exception ex) {
        super(ex);
    }

    public SpatialSystemException(final SpatialSystemErrors pError, final Exception ex) {
        super(pError.message(), ex);
        error = pError;
    }

    /**
     * Constructs a ApplicationException object and stores the stack trace from the Exception object. The message is
     * stored as an error message.
     *
     * @param msg the error message
     * @param ex  Exception object
     */

    public SpatialSystemException(final String msg, final Exception ex) {
        super(msg, ex);
    }

    /**
     * Get all the parameters associated with this exception.
     */
    public final Object[] getParameters() {
        return params;
    }

    public final SpatialSystemErrors getError() {
        return error;
    }

}
