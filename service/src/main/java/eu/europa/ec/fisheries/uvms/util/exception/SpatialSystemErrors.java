package eu.europa.ec.fisheries.uvms.util.exception;

/**
 * Created by kopyczmi on 17-Aug-15.
 */
public enum SpatialSystemErrors {
    SOME_EXAMPLE_SYSTEM_ERROR("Example Sytem Error");

    private String message;

    SpatialSystemErrors(final String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }

    public String message(final String prefix) {
        return prefix + "::" + message;
    }
}
