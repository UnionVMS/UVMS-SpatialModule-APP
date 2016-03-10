package eu.europa.ec.fisheries.uvms.spatial.util;

/**
 * Created by padhyad on 3/7/2016.
 */
public enum LayerTypeEnum {

    BASE("base"),
    ADDITIONAL("additional"),
    AREA("area"),
    PORT("port");

    private String type;

    LayerTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static LayerTypeEnum getLayerType(String type) {
        for (LayerTypeEnum layer : LayerTypeEnum.values()) {
            if (layer.getType().equals(type)) {
                return layer;
            }
        }
        return null;
    }

}
