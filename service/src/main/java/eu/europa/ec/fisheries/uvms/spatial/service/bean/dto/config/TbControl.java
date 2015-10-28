package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "type"
})
public class TbControl {

    @JsonProperty("type")
    private String type;

    /**
     * No args constructor for use in serialization
     */
    public TbControl() {
    }

    public TbControl(String type) {
        this.type = type;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    public TbControl withType(String type) {
        this.type = type;
        return this;
    }

}
