package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers;

/**
 * Created by padhyad on 1/7/2016.
 */
public enum AreaSubTypeEnum {

    SYSAREA("sysarea"),
    PORT("port"),
    BACKGROUND("background"),
    ADDITIONAL("additional"),
    OTHERS("others"),
    USERAREA("userarea"),
    PORTAREA("portarea");

    private String areaSubType;

    AreaSubTypeEnum(String areaSubType) {
        this.areaSubType = areaSubType;
    }

    public String getAreaSubType() {
        return areaSubType;
    }
}
