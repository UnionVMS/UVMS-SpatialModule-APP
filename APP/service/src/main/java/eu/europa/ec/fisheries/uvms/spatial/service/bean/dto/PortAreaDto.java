package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto;

import static org.apache.commons.lang.StringUtils.isNumeric;

public class PortAreaDto extends GeoJsonDto {

    public Long getId() {
        String gid = properties.get(ID);
        if (gid != null && isNumeric(gid)) {
            return Long.valueOf(gid);
        }
        return null;
    }

    public void setId(Long gid) {
        properties.put(ID, String.valueOf(gid));
    }

}
