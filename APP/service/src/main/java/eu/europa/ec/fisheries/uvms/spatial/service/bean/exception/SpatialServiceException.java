package eu.europa.ec.fisheries.uvms.spatial.service.bean.exception;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class SpatialServiceException extends RuntimeException {

    private final SpatialServiceErrors error;
    private Object[] params;

    public SpatialServiceException(SpatialServiceErrors error, Object... params) {
        super(error.formatMessage(params));
        this.error = error;
        this.params = params;
    }

    public SpatialServiceException(SpatialServiceErrors error, Throwable cause, Object... params) {
        super(error.formatMessage(params), cause);
        this.params = params;
        this.error = error;
    }

    public SpatialServiceException(SpatialServiceErrors error, SpatialServiceException cause) {
        super(join(getMessage(error), cause.getMessage()), cause);
        this.error = cause.getError();
    }

    public SpatialServiceException(SpatialServiceErrors error, SpatialServiceException cause, Object... arguments) {
        super(join(error.formatMessage(arguments), cause.getMessage()), cause);
        this.error = cause.getError();
    }

    private static String join(String... params) {
        return StringUtils.join(params, ": ");
    }

    private static String getMessage(SpatialServiceErrors error) {
        return error.getMessagePattern();
    }

    public Integer getErrorCode() {
        return this.error.getErrorCode();
    }
    
    public String getErrorMessageCode() {
        return this.error.getErrorMessageCode();
    }

    public SpatialServiceErrors getError() {
        return this.error;
    }

    public Object[] getParameters() {
        return this.params;
    }

    public int hashCode() {
        return (new HashCodeBuilder()).append(this.error).toHashCode();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            SpatialServiceException that = (SpatialServiceException) o;
            return (new EqualsBuilder()).append(this.error, that.error).isEquals();
        } else {
            return false;
        }
    }
}