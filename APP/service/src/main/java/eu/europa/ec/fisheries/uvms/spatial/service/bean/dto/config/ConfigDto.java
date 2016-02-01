package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm.VisibilitySettingsDto;

/**
 * Created by padhyad on 12/23/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfigDto {

    @JsonProperty("visibilitySettings")
    private VisibilitySettingsDto visibilitySettings;

    public ConfigDto(){}

    public ConfigDto(VisibilitySettingsDto visibilitySettings) {
        this.visibilitySettings = visibilitySettings;
    }

    @JsonProperty("visibilitySettings")
    public VisibilitySettingsDto getVisibilitySettings() {
        return visibilitySettings;
    }

    @JsonProperty("visibilitySettings")
    public void setVisibilitySettings(VisibilitySettingsDto visibilitySettings) {
        this.visibilitySettings = visibilitySettings;
    }
}
