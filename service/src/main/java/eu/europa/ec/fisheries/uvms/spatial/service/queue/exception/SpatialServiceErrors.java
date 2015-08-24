package eu.europa.ec.fisheries.uvms.spatial.service.queue.exception;

import java.text.MessageFormat;

/**
 * Created by kopyczmi on 13-Aug-15.
 */
public enum SpatialServiceErrors {
    //@formatter:off

    INTERNAL_APPLICATION_ERROR(500, "An internal application error has occurred.", "An internal application error has occurred.");

    //@formatter:on

    private final Integer errorCode;
    private final String messagePattern;
    private final String description;

    SpatialServiceErrors(int code, String message) {
        this(code, message, message);
    }

    SpatialServiceErrors(int code, String message, String description) {
        this.errorCode = Integer.valueOf(code);
        this.messagePattern = message;
        this.description = description;
    }

    public static String retrieveErrorCode(SpatialServiceException e) {
        if (e.getError() != null) {
            try {
                return e.getError().getErrorCode().toString();
            } catch (Exception var2) {
                return "Error code cannot be resolved (Reason:" + var2.getMessage() + ")";
            }
        } else {
            return null;
        }
    }

    public static SpatialServiceErrors getErrorByErrorCode(String errorCode) {
        SpatialServiceErrors[] arr$ = values();
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            SpatialServiceErrors error = arr$[i$];
            String code = error.errorCode.toString();
            if (code.equals(errorCode)) {
                return error;
            }
        }

        return null;
    }

    public Integer getErrorCode() {
        return this.errorCode;
    }

    public String getDescription() {
        return this.description;
    }

    public String getMessagePattern() {
        return this.messagePattern;
    }

    public String formatMessage(Object... arguments) {
        try {
            return MessageFormat.format(this.getMessagePattern().replaceAll("\'", "\'\'"), arguments);
        } catch (Exception var3) {
            return "Error message (" + this + ") cannot be resolved correctly (Reason: " + var3.getMessage() + ")";
        }
    }
}
