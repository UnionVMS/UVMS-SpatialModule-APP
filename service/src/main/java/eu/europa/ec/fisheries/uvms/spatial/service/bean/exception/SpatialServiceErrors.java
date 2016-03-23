package eu.europa.ec.fisheries.uvms.spatial.service.bean.exception;

import java.text.MessageFormat;

public enum SpatialServiceErrors {

    INTERNAL_APPLICATION_ERROR("INTERNAL_APPLICATION_ERROR", 500, "An internal application error has occurred.", "An internal application error has occurred."),
    WRONG_NATIVE_SQL_CONFIGURATION_ERROR("WRONG_NATIVE_SQL_CONFIGURATION_ERROR", 5001, "Sql Native query with name {0} does not exist.", "Wrong configuration."),
    NO_SUCH_CRS_CODE_ERROR("NO_SUCH_CRS_CODE_ERROR", 5002, "CRS code {0} does not exist.", "Wrong argument."),
    WRONG_MEASUREMENT_UNIT("WRONG_MEASUREMENT_UNIT", 5003, "Invalid parameter. Wrong measurement unit: '{0}'.", "Wrong argument."),
    WRONG_AREA_TYPE("WRONG_AREA_TYPE", 5004, "Invalid parameter. Wrong Area Type: '{0}'.", "Wrong argument."),
    MISSING_LATITUDE("MISSING_LATITUDE", 5005, "Invalid parameter. Missing latitude: 'lat='.", "Wrong argument."),
    MISSING_LONGITUDE("MISSING_LONGITUDE", 5006, "Invalid parameter. Missing longitude: 'lon='.", "Wrong argument."),
    MISSING_AREA_TYPE("MISSING_AREA_TYPE", 5007, "Invalid parameter. Please specify at least one Area Type: 'type='.", "Wrong argument."),
    MISSING_LOCATION_TYPE("MISSING_LOCATION_TYPE", 5008, "Invalid parameter. Please specify at least one Location Type: 'type='.", "Wrong argument."),
    ENTITY_NOT_FOUND("ENTITY_NOT_FOUND", 5010, "Entity Type not found : {0}"),
    WRONG_LOCATION_TYPE("WRONG_LOCATION_TYPE", 5011, "Invalid parameter. Wrong Location Type: '{0}'.", "Wrong argument."),
    INVALID_ID_TYPE("INVALID_ID_TYPE", 5012, "Invalid id in the request : {0}"),
    INVALID_AREA_TYPE("INVALID_AREA_TYPE", 5013, "Invalid area type: {0}", "Wrong argument."),
    USER_AREA_DOES_NOT_EXIST("USER_AREA_DOES_NOT_EXIST_FOR_USER_AND_SCOPE", 5014, "User area with id: {0} does not exist for that user and scope.", "Wrong argument."),
    MISSING_USER_AREA_ID("MISSING_USER_AREA_ID", 5015, "Please specify user area id.", "Wrong argument."),
    PORT_AREA_DOES_NOT_EXIST("PORT_AREA_DOES_NOT_EXIST", 5016, "Port area with id: {0} does not exist.", "Wrong argument."),
    MISSING_PORT_AREA_ID("MISSING_PORT_AREA_ID", 5017, "Please specify port area id.", "Wrong argument."),
    INVALID_UPLOAD_AREA_DATA("INVALID_UPLOAD_AREA_DATA", 5018, "Invalid upload area data.", "Wrong argument."),
    INVALID_USER_AREA_ID("INVALID_USER_AREA_ID", 5019, "Invalid user area id.", "Wrong argument."),
    USER_AREA_ALREADY_EXISTING("USER_AREA_ALREADY_EXISTING", 5020, "Invalid parameter");


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
