package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;

import java.util.List;
import java.util.Map;

/**
 * Created by padhyad on 11/25/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PositionsDto {

    @JsonProperty("attribute")
    private String attribute;

    @JsonProperty("style")
    private Map<String, String> style;

    public PositionsDto() {}

    public PositionsDto(String attribute, Map<String, String> style) {
        this.style = style;
        this.attribute = attribute;
    }

    @JsonProperty("attribute")
    public String getAttribute() {
        return attribute;
    }

    @JsonProperty("attribute")
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    @JsonProperty("style")
    public Map<String, String> getStyle() {
        return style;
    }

    @JsonProperty("style")
    public void setStyle(Map<String, String> style) {
        this.style = style;
    }

    @Override
    public String toString() {
        return "ClassPojo [style = " + style + ", attribute = " + attribute + "]";
    }
}

