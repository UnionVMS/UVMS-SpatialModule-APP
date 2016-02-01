package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by padhyad on 1/8/2016.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AreaServiceLayerDto extends ServiceLayerDto {

    @JsonProperty("data")
    private List<AreaDto> data;

    public AreaServiceLayerDto() {}

    public AreaServiceLayerDto(ServiceLayerDto serviceLayerDto, List<AreaDto> data) {
        super(serviceLayerDto.getId(), serviceLayerDto.getName(), serviceLayerDto.getLayerDesc(), serviceLayerDto.getSubType());
        this.data = data;
    }

    @JsonProperty("data")
    public List<AreaDto> getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(List<AreaDto> data) {
        this.data = data;
    }
}
