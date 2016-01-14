package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config;

import com.fasterxml.jackson.annotation.*;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.AreaDto;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "type",
        "groupType",
        "title",
        "isBaseLayer",
        "shortCopyright",
        "longCopyright",
        "url",
        "serverType",
        "layerGeoName",
        "styles",
        "areas",
        "apiKey"
})
public class LayerDto {

    @JsonProperty("type")
    private String type;

    @JsonProperty("groupType")
    private String groupType;

    @JsonProperty("title")
    private String title;

    @JsonProperty("isBaseLayer")
    private Boolean isBaseLayer;

    @JsonProperty("shortCopyright")
    private String shortCopyright;

    @JsonProperty("longCopyright")
    private String longCopyright;

    @JsonProperty("url")
    private String url;

    @JsonProperty("serverType")
    private String serverType;

    @JsonProperty("layerGeoName")
    private String layerGeoName;

    @JsonProperty("styles")
    private StylesDto styles;

    @JsonProperty("areas")
    private List<AreaDto> areaDto;

    @JsonProperty("apiKey")
    private String apiKey;

    /**
     * No args constructor for use in serialization
     */
    public LayerDto() {}

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("groupType")
    public String getGroupType() {
        return groupType;
    }

    @JsonProperty("groupType")
    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("isBaseLayer")
    public Boolean getIsBaseLayer() {
        return isBaseLayer;
    }

    @JsonProperty("isBaseLayer")
    public void setIsBaseLayer(Boolean isBaseLayer) {
        this.isBaseLayer = isBaseLayer;
    }

    @JsonProperty("shortCopyright")
    public String getShortCopyright() {
        return shortCopyright;
    }

    @JsonProperty("shortCopyright")
    public void setShortCopyright(String shortCopyright) {
        this.shortCopyright = shortCopyright;
    }

    @JsonProperty("longCopyright")
    public String getLongCopyright() {
        return longCopyright;
    }

    @JsonProperty("longCopyright")
    public void setLongCopyright(String longCopyright) {
        this.longCopyright = longCopyright;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty("serverType")
    public String getServerType() {
        return serverType;
    }

    @JsonProperty("serverType")
    public void setServerType(String serverType) {
        this.serverType = serverType;
    }

    @JsonProperty("layerGeoName")
    public String getLayerGeoName() {
        return layerGeoName;
    }

    @JsonProperty("layerGeoName")
    public void setLayerGeoName(String layerGeoName) {
        this.layerGeoName = layerGeoName;
    }

    @JsonProperty("styles")
    public StylesDto getStyles() {
        return styles;
    }

    @JsonProperty("styles")
    public void setStyles(StylesDto styles) {
        this.styles = styles;
    }

    @JsonProperty("apiKey")
    public String getApiKey() {
        return apiKey;
    }

    @JsonProperty("apiKey")
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @JsonProperty("areas")
    public List<AreaDto> getAreaDto() {
        return areaDto;
    }

    @JsonProperty("areas")
    public void setAreaDto(List<AreaDto> areaDto) {
        this.areaDto = areaDto;
    }
}
