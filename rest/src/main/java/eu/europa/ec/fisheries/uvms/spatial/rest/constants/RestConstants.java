package eu.europa.ec.fisheries.uvms.spatial.rest.constants;

public final class RestConstants {

    private RestConstants(){}

    public static final String REST_URL = "/rest";
    public static final String MODULE_NAME = "spatial";

    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-ControlDto-Allow-Origin";
    public static final String ACCESS_CONTROL_ALLOW_METHODS_ALL = "*";
    
    public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-ControlDto-Allow-Methods";
    public static final String ACCESS_CONTROL_ALLOWED_METHODS = "GET, POST, DELETE, PUT, OPTIONS";
    
    public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-ControlDto-Allow-Headers";
    public static final String ACCESS_CONTROL_ALLOW_HEADERS_ALL = "Content-Type";
}
