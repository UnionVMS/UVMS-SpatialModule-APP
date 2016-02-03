package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by padhyad on 1/28/2016.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class VisibilityAttributesDto {

    @JsonProperty("order")
    private List<String> order;

    @JsonProperty("values")
    private List<String> values;

    public VisibilityAttributesDto(){}

    public VisibilityAttributesDto(List<String> order, List<String> values) {
        this.order = order;
        this.values = values;
    }

    @JsonProperty("order")
    public List<String> getOrder() {
        return order;
    }

    @JsonProperty("order")
    public void setOrder(List<String> order) {
        this.order = order;
    }

    @JsonProperty("values")
    public List<String> getValues() {
        return values;
    }

    @JsonProperty("values")
    public void setValues(List<String> values) {
        this.values = values;
    }
}
