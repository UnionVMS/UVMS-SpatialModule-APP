package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by padhyad on 1/11/2016.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AreaLayersDto {

    @JsonProperty("sysAreas")
    private List<LayerAreaDto> sysAreas;

    @JsonProperty("userAreas")
    private LayerAreaDto userAreas;

    public AreaLayersDto() {
    }

    public AreaLayersDto(List<LayerAreaDto> portLayers, LayerAreaDto userAreas) {
        this.sysAreas = portLayers;
        this.userAreas = userAreas;
    }

    @JsonProperty("sysAreas")
    public List<LayerAreaDto> getSysAreas() {
        return sysAreas;
    }

    @JsonProperty("sysAreas")
    public void setSysAreas(List<LayerAreaDto> sysAreas) {
        this.sysAreas = sysAreas;
    }

    @JsonProperty("userAreas")
    public LayerAreaDto getUserAreas() {
        return userAreas;
    }

    @JsonProperty("userAreas")
    public void setUserAreas(LayerAreaDto userAreas) {
        this.userAreas = userAreas;
    }
}
