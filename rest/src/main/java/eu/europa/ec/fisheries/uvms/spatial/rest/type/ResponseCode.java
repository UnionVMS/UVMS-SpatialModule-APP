package eu.europa.ec.fisheries.uvms.spatial.rest.type;

public enum ResponseCode {

    OK(200),
    ERROR(500),

    SPATIAL_ERROR(501),

    INPUT_ERROR(511),
    MAPPING_ERROR(512),

    SERVICE_ERROR(521),
    MODEL_ERROR(522),
    DOMAIN_ERROR(523),

    UNDEFINED_ERROR(500);

    private final int code;

    private ResponseCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
