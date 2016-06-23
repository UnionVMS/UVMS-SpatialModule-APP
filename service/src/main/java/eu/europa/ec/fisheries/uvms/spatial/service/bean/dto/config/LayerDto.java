package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config;

import com.fasterxml.jackson.annotation.*;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.AreaDto;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "type",
        "areaType",
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

    @JsonProperty("areaType")
    private String areaType;

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

    @JsonProperty("gid")
    private Long gid;

    @JsonProperty("name")
    private String name;

    @JsonProperty("apiKey")
    private String apiKey;

    @JsonProperty("cql_all")
    private String cqlAll;

    @JsonProperty("cql_active")
    private String cqlActive;

    @JsonProperty("cql")
    private String cql;

    @JsonProperty("warning")
    private Boolean isWarning;

    @JsonIgnore
    private String areaLocationTypeName;

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

    @JsonProperty("gid")
    public Long getGid() {
        return gid;
    }

    @JsonProperty("gid")
    public void setGid(Long gid) {
        this.gid = gid;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("cql_all")
    public String getCqlAll() {
        return cqlAll;
    }

    @JsonProperty("cql_all")
    public void setCqlAll(String cqlAll) {
        this.cqlAll = cqlAll;
    }

    @JsonProperty("areaType")
    public String getAreaType() {
        return areaType;
    }

    @JsonProperty("areaType")
    public void setAreaType(String areaType) {
        this.areaType = areaType;
    }

    @JsonProperty("cql_active")
    public String getCqlActive() {
        return cqlActive;
    }

    @JsonProperty("cql_active")
    public void setCqlActive(String cqlActive) {
        this.cqlActive = cqlActive;
    }

    @JsonProperty("cql")
    public String getCql() {
        return cql;
    }

    @JsonProperty("cql")
    public void setCql(String cql) {
        this.cql = cql;
    }

    @JsonProperty("warning")
    public Boolean isWarning() {
        return isWarning;
    }

    @JsonProperty("warning")
    public void setIsWarning(Boolean isWarning) {
        this.isWarning = isWarning;
    }

    public String getAreaLocationTypeName() {
        return areaLocationTypeName;
    }

    public void setAreaLocationTypeName(String areaLocationTypeName) {
        this.areaLocationTypeName = areaLocationTypeName;
    }
}
