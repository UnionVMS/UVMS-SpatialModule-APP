package eu.europa.ec.fisheries.uvms.spatial.util;

/**
 * Created by padhyad on 3/2/2016.
 */
public enum AreaTypeEnum {

    sysarea("sysarea"),
    userarea("userarea"),
    areagroup("areagroup");

    private AreaTypeEnum(String type) {
        this.type = type;
    }

    private String type;

    public String getType() {
        return type;
    }

    public static AreaTypeEnum getEnumFromValue(String value) {
        for (AreaTypeEnum areaTypeEnum : AreaTypeEnum.values()) {
            if (areaTypeEnum.getType().equalsIgnoreCase(value)) {
                return areaTypeEnum;
            }
        }
        return null;
    }
}
