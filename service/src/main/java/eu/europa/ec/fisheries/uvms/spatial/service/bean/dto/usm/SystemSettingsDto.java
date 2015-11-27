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

    public SystemSettingsDto() {}

    public SystemSettingsDto(String geoserverUrl) {
        this.geoserverUrl = geoserverUrl;
    }

    @JsonProperty("geoserverUrl")
    public String getGeoserverUrl() {
        return geoserverUrl;
    }

    @JsonProperty("geoserverUrl")
    public void setGeoserverUrl(String geoserverUrl) {
        this.geoserverUrl = geoserverUrl;
    }

    @Override
    public String toString() {
        return "ClassPojo [geoserverUrl = " + geoserverUrl + "]";
    }
}

