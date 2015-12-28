package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto;

public class UserAreaGeomDto extends GeoJsonDto {

    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String IS_SHARED = "isShared";
    private static final boolean DEFAULT_IS_SHARED_VALUE = false;

    public String getName() {
        return properties.get(NAME);
    }

    public String getAreaDesc() {
        return properties.get(DESCRIPTION);
    }

    public Boolean isShared() {
        String isShared = properties.get(IS_SHARED);
        if (isShared != null) {
            return Boolean.valueOf(isShared);
        }
        return DEFAULT_IS_SHARED_VALUE;
    }

}
