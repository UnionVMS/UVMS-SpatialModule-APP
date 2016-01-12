package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.AreaDto;

import java.util.List;

/**
 * Created by padhyad on 1/11/2016.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LayerAreaDto extends LayersDto {

    @JsonProperty("areas")
    private List<AreaDto> areaDtos;

    public LayerAreaDto() {}

    public LayerAreaDto(List<AreaDto> areaDtos, String name, String serviceLayerId) {
        super(name, serviceLayerId);
        this.areaDtos = areaDtos;
    }

    @JsonProperty("areas")
    public List<AreaDto> getAreaDtos() {
        return areaDtos;
    }

    @JsonProperty("areas")
    public void setAreaDtos(List<AreaDto> areaDtos) {
        this.areaDtos = areaDtos;
    }
}
