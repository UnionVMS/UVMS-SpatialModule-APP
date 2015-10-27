
package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "type",
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
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Layer() {
    }

    /**
     * 
     * @param isBaseLayer
     * @param title
     * @param styles
     * @param type
     * @param url
     * @param attribution
     * @param layerGeoName
     * @param serverType
     */
    public Layer(String type, String title, Boolean isBaseLayer, String attribution, String url, String serverType, String layerGeoName, Styles styles) {
        this.type = type;
        this.title = title;
        this.isBaseLayer = isBaseLayer;
        this.attribution = attribution;
        this.url = url;
        this.serverType = serverType;
        this.layerGeoName = layerGeoName;
        this.styles = styles;
    }

    /**
     * 
     * @return
     *     The type
     */
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    public Layer withType(String type) {
        this.type = type;
        return this;
    }

    /**
     * 
     * @return
     *     The title
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    public Layer withTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * 
     * @return
     *     The isBaseLayer
     */
    @JsonProperty("isBaseLayer")
    public Boolean getIsBaseLayer() {
        return isBaseLayer;
    }

    /**
     * 
     * @param isBaseLayer
     *     The isBaseLayer
     */
    @JsonProperty("isBaseLayer")
    public void setIsBaseLayer(Boolean isBaseLayer) {
        this.isBaseLayer = isBaseLayer;
    }

    public Layer withIsBaseLayer(Boolean isBaseLayer) {
        this.isBaseLayer = isBaseLayer;
        return this;
    }

    /**
     * 
     * @return
     *     The attribution
     */
    @JsonProperty("attribution")
    public String getAttribution() {
        return attribution;
    }

    /**
     * 
     * @param attribution
     *     The attribution
     */
    @JsonProperty("attribution")
    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }

    public Layer withAttribution(String attribution) {
        this.attribution = attribution;
        return this;
    }

    /**
     * 
     * @return
     *     The url
     */
    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    /**
     * 
     * @param url
     *     The url
     */
    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    public Layer withUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * 
     * @return
     *     The serverType
     */
    @JsonProperty("serverType")
    public String getServerType() {
        return serverType;
    }

    /**
     * 
     * @param serverType
     *     The serverType
     */
    @JsonProperty("serverType")
    public void setServerType(String serverType) {
        this.serverType = serverType;
    }

    public Layer withServerType(String serverType) {
        this.serverType = serverType;
        return this;
    }

    /**
     * 
     * @return
     *     The layerGeoName
     */
    @JsonProperty("layerGeoName")
    public String getLayerGeoName() {
        return layerGeoName;
    }

    /**
     * 
     * @param layerGeoName
     *     The layerGeoName
     */
    @JsonProperty("layerGeoName")
    public void setLayerGeoName(String layerGeoName) {
        this.layerGeoName = layerGeoName;
    }

    public Layer withLayerGeoName(String layerGeoName) {
        this.layerGeoName = layerGeoName;
        return this;
    }

    /**
     * 
     * @return
     *     The styles
     */
    @JsonProperty("styles")
    public Styles getStyles() {
        return styles;
    }

    /**
     * 
     * @param styles
     *     The styles
     */
    @JsonProperty("styles")
    public void setStyles(Styles styles) {
        this.styles = styles;
    }

    public Layer withStyles(Styles styles) {
        this.styles = styles;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Layer withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}
