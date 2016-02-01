package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers;

/**
 * Created by padhyad on 1/7/2016.
 */
public enum LayerTypeEnum {

    BACKGROUND("background"),
    ADDITIONAL("additional"),
    PORT("port"),
    USERAREA("userarea"),
    SYSAREA("sysarea");

    private String layerType;

    LayerTypeEnum(String layerType) {
        this.layerType = layerType;
    }

    public String getLayerType() {
        return layerType;
    }

    public static LayerTypeEnum value(String type) {
        for (LayerTypeEnum layer: LayerTypeEnum.values()) {
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
