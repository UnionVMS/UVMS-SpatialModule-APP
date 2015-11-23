package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;
import java.util.Map;

/**
 * Created by padhyad on 11/23/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "attribute",
        "style"
})
public class PositionsDto {

    @JsonProperty("attribute")
    private String attribute;

    @JsonProperty("style")
    private List<Map<String, String>> style;

    @JsonProperty("attribute")
    public String getAttribute() {
        return attribute;
    }

    @JsonProperty("attribute")
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    @JsonProperty("style")
    public List<Map<String, String>> getStyle() {
        return style;
    }

    @JsonProperty("style")
    public void setStyle(List<Map<String, String>> style) {
        this.style = style;
    }
}
