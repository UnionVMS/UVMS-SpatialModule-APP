package eu.europa.ec.fisheries.uvms.spatial.rest.dto;

public enum ResponseCode {

    OK(200),
    ERROR(500),

    SPATIAL_ERROR(501),

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
