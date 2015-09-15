package eu.europa.ec.fisheries.uvms.spatial.model;

public enum FaultCode {

    SPATIAL_MESSAGE(1700);

    private final int code;

    private FaultCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
