package eu.europa.ec.fisheries.uvms.spatial.model;

public enum FaultCode {

    SPATIAL_DOMAIN(1800),
    SPATIAL_DOMAIN_MODEL(1810),
    SPATIAL_DOMAIN_MODEL_GROUP(1811),
    SPATIAL_DOMAIN_MESSAGE(1820),
    SPATIAL_DOMAIN_MAPPING_ERROR(1830),
    SPATIAL_DOMAIN_MAPPING_SEARCH(1831),
    SPATIAL_DOMAIN_DAO(1840),
    SPATIAL_DOMAIN_DAO_NO_ENTITY_FOUND(1841),
    SPATIAL_DOMAIN_DAO_GROUP(1842),
    SPATIAL_MESSAGE(1700),
    SPATIAL_DOMAIN_INPUT_ERROR(1801);

    //VesselModelException (parameterDao)

    private final int code;

    private FaultCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
