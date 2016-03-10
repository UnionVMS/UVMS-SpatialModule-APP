package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers;

/**
 * Created by padhyad on 1/7/2016.
 */
public enum LayerSubTypeEnum {

    BACKGROUND("background"),
    ADDITIONAL("additional"),
    PORT("port"),
    USERAREA("userarea"),
    SYSAREA("sysarea");

    private String layerType;

    LayerSubTypeEnum(String layerType) {
        this.layerType = layerType;
    }

    public String getLayerType() {
        return layerType;
    }

    public static LayerSubTypeEnum value(String type) {
        for (LayerSubTypeEnum layer: LayerSubTypeEnum.values()) {
            if (layer.layerType.equalsIgnoreCase(type)) {
                return layer;
            }
        }
        throw new IllegalArgumentException(type);
    }

    public void setLayerType(String layerType) {
        this.layerType = layerType;
    }
}
