package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto;

public class PortAreaDto extends GeoJsonDto {

    public Long getId() {
        Object gid = properties.get(ID);
        if (gid != null) {
            try {
                return Long.valueOf((String)gid);
            } catch (NumberFormatException nfe ) {
                //do nothing, later we return null anyway
            }
        }

        return null;
    }

    public void setId(Long gid) {
        properties.put(ID, String.valueOf(gid));
    }

}
