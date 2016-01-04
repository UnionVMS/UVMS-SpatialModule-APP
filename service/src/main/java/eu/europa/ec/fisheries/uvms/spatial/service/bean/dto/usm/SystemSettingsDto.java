package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by padhyad on 11/25/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SystemSettingsDto {

    @JsonProperty("geoserverUrl")
    private String geoserverUrl;

    @JsonProperty("bingApiKey")
    private String bingApiKey;

    public SystemSettingsDto() {}

    public SystemSettingsDto(String geoserverUrl, String bingApiKey) {
        this.geoserverUrl = geoserverUrl;
        this.bingApiKey = bingApiKey;
    }

    @JsonProperty("geoserverUrl")
    public String getGeoserverUrl() {
        return geoserverUrl;
    }

    @JsonProperty("geoserverUrl")
    public void setGeoserverUrl(String geoserverUrl) {
        this.geoserverUrl = geoserverUrl;
    }

    @JsonProperty("bingApiKey")
    public String getBingApiKey() {
        return bingApiKey;
    }

    @JsonProperty("bingApiKey")
    public void setBingApiKey(String bingApiKey) {
        this.bingApiKey = bingApiKey;
    }

    @Override
    public String toString() {
        return "ClassPojo [geoserverUrl = " + geoserverUrl + "]";
    }
}

