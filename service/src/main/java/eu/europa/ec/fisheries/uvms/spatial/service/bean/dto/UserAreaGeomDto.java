package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto;

import static org.apache.commons.lang.StringUtils.isNumeric;

public class UserAreaGeomDto extends GeoJsonDto {

    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String GID = "gid";
    private static final String IS_SHARED = "isShared";
    private static final boolean DEFAULT_IS_SHARED_VALUE = false;

    public String getName() {
        return properties.get(NAME);
    }

    public void setName(String name) {
        if (name != null) {
            properties.put(NAME, name);
        }
    }

    public String getDesc() {
        return properties.get(DESCRIPTION);
    }

    public void setDesc(String desc) {
        if (desc != null) {
            properties.put(DESCRIPTION, desc);
        }
    }

    public Boolean isShared() {
        String isShared = properties.get(IS_SHARED);
        if (isShared != null) {
            return Boolean.valueOf(isShared);
        }
        return DEFAULT_IS_SHARED_VALUE;
    }

    public void setShared(Boolean isShared) {
        if (isShared != null) {
            properties.put(IS_SHARED, String.valueOf(isShared));
        }
    }

    public Long getGid() {
        String gid = properties.get(GID);
        if (gid != null && isNumeric(gid)) {
            return Long.valueOf(gid);
        }
        return null;
    }

    public void setId(Long gid) {
        properties.put(GID, String.valueOf(gid));
    }

}
