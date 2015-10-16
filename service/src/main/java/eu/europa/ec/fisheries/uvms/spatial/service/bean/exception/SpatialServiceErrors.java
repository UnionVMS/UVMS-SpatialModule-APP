package eu.europa.ec.fisheries.uvms.spatial.service.bean.exception;

import java.text.MessageFormat;

/**
 * Created by kopyczmi on 13-Aug-15.
 */
public enum SpatialServiceErrors {
    //@formatter:off

    INTERNAL_APPLICATION_ERROR("INTERNAL_APPLICATION_ERROR", 500, "An internal application error has occurred.", "An internal application error has occurred."),
    WRONG_NATIVE_SQL_CONFIGURATION_ERROR("WRONG_NATIVE_SQL_CONFIGURATION_ERROR", 5001, "Sql Native query with name {0} does not exist.", "Wrong configuration."),
    NO_SUCH_CRS_CODE_ERROR("NO_SUCH_CRS_CODE_ERROR", 5002, "CRS code {0} does not exist.", "Wrong argument."),
    WRONG_MEASUREMENT_UNIT("WRONG_MEASUREMENT_UNIT", 5003, "Invalid parameter. Wrong measurement unit: '{0}'.", "Wrong argument."),
    WRONG_AREA_TYPE("WRONG_AREA_TYPE", 5004, "Invalid parameter. Wrong Area Type: '{0}'.", "Wrong argument."),
    MISSING_LATITUDE("MISSING_LATITUDE", 5005, "Invalid parameter. Missing latitude: 'lat='.", "Wrong argument."),
    MISSING_LONGITUDE("MISSING_LONGITUDE", 5006, "Invalid parameter. Missing longitude: 'lon='.", "Wrong argument."),
    MISSING_AREA_TYPE("MISSING_AREA_TYPE", 5007, "Invalid parameter. Please specify at least one Area Type: 'type='.", "Wrong argument."),
    MISSING_LOCATION_TYPE("MISSING_LOCATION_TYPE", 5008, "Invalid parameter. Please specify at least one Location Type: 'type='.", "Wrong argument."),
	INVALID_AREA_LOCATION_TYPE("INVALID_AREA_LOCATION_TYPE", 5009, "Invalid Area or Location Type in Request : {0}"),
	ENTITY_NOT_FOUND("ENTITY_NOT_FOUND", 5010, "Entity Type not found : {0}"),
    WRONG_LOCATION_TYPE("WRONG_LOCATION_TYPE", 5011, "Invalid parameter. Wrong Location Type: '{0}'.", "Wrong argument."),
    INVALID_ID_TYPE("INVALID_ID_TYPE", 5012, "Invalid id in the request : {0}"),
    INVALID_AREA_TYPE("INVALID_AREA_TYPE", 5013, "Invalid area type: {0}", "Wrong argument.");

    //@formatter:on

    private final Integer errorCode;
    private final String messagePattern;
    private final String description;
    private final String errorMessageCode;

    SpatialServiceErrors(String errorMessageCode, int code, String message) {
        this(errorMessageCode, code, message, message);
    }

    SpatialServiceErrors(String errorMessageCode, int code, String message, String description) {
        this.errorCode = Integer.valueOf(code);
        this.messagePattern = message;
        this.description = description;
        this.errorMessageCode = errorMessageCode;
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
    
    public String getErrorMessageCode() {
    	return this.errorMessageCode;
    }

    public String formatMessage(Object... arguments) {
        try {
            return MessageFormat.format(this.getMessagePattern().replaceAll("\'", "\'\'"), arguments);
        } catch (Exception var3) {
            return "Error message (" + this + ") cannot be resolved correctly (Reason: " + var3.getMessage() + ")";
        }
    }
}
