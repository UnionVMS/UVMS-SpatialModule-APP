package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "type",
        "areaType",
        "title",
        "isBaseLayer",
        "attribution",
        "url",
        "serverType",
        "layerGeoName",
        "styles"
})
public class Layer {

    @JsonProperty("type")
    private String type;
    @JsonProperty("areaType")
    private String areaType;
    @JsonProperty("title")
    private String title;
    @JsonProperty("isBaseLayer")
    private Boolean isBaseLayer;
    @JsonProperty("attribution")
    private String attribution;
    @JsonProperty("url")
    private String url;
    @JsonProperty("serverType")
    private String serverType;
    @JsonProperty("layerGeoName")
    private String layerGeoName;
    @JsonProperty("styles")
    private Styles styles;

    /**
     * No args constructor for use in serialization
     */
    public Layer() {
    }

    public Layer(String type, String areaType, String title, Boolean isBaseLayer, String attribution, String url, String serverType, String layerGeoName, Styles styles) {
        this.type = type;
        this.areaType = areaType;
        this.title = title;
        this.isBaseLayer = isBaseLayer;
        this.attribution = attribution;
        this.url = url;
        this.serverType = serverType;
        this.layerGeoName = layerGeoName;
        this.styles = styles;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    public Layer withType(String type) {
        this.type = type;
        return this;
    }

    @JsonProperty("areaType")
    public String getAreaType() {
        return areaType;
    }

    @JsonProperty("areaType")
    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    public Layer withAreaType(String areaType) {
        this.areaType = areaType;
        return this;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    public Layer withTitle(String title) {
        this.title = title;
        return this;
    }

    @JsonProperty("isBaseLayer")
    public Boolean getIsBaseLayer() {
        return isBaseLayer;
    }

    @JsonProperty("isBaseLayer")
    public void setIsBaseLayer(Boolean isBaseLayer) {
        this.isBaseLayer = isBaseLayer;
    }

    public Layer withIsBaseLayer(Boolean isBaseLayer) {
        this.isBaseLayer = isBaseLayer;
        return this;
    }

    @JsonProperty("attribution")
    public String getAttribution() {
        return attribution;
    }

    @JsonProperty("attribution")
    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }

    public Layer withAttribution(String attribution) {
        this.attribution = attribution;
        return this;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    public Layer withUrl(String url) {
        this.url = url;
        return this;
    }

    @JsonProperty("serverType")
    public String getServerType() {
        return serverType;
    }

    @JsonProperty("serverType")
    public void setServerType(String serverType) {
        this.serverType = serverType;
    }

    public Layer withServerType(String serverType) {
        this.serverType = serverType;
        return this;
    }

    @JsonProperty("layerGeoName")
    public String getLayerGeoName() {
        return layerGeoName;
    }

    @JsonProperty("layerGeoName")
    public void setLayerGeoName(String layerGeoName) {
        this.layerGeoName = layerGeoName;
    }

    public Layer withLayerGeoName(String layerGeoName) {
        this.layerGeoName = layerGeoName;
        return this;
    }

    @JsonProperty("styles")
    public Styles getStyles() {
        return styles;
    }

    @JsonProperty("styles")
    public void setStyles(Styles styles) {
        this.styles = styles;
    }

    public Layer withStyles(Styles styles) {
        this.styles = styles;
        return this;
    }

}
