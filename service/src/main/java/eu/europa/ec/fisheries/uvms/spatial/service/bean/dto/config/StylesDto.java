package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "geom",
        "label",
        "labelGeom"
})
public class StylesDto {

    @JsonProperty("geom")
    private String geom;

    @JsonProperty("label")
    private String label;

    @JsonProperty("labelGeom")
    private String labelGeom;

    /**
     * No args constructor for use in serialization
     */
    public StylesDto() {}

    public StylesDto(String geom, String label, String labelGeom) {
        this.geom = geom;
        this.label = label;
        this.labelGeom = labelGeom;
    }

    public StylesDto(String geom) {
        this.geom = geom;
    }

    @JsonProperty("geom")
    public String getGeom() {
        return geom;
    }

    @JsonProperty("geom")
    public void setGeom(String geom) {
        this.geom = geom;
    }

    @JsonProperty("label")
    public String getLabel() {
        return label;
    }

    @JsonProperty("label")
    public void setLabel(String label) {
        this.label = label;
    }

    @JsonProperty("labelGeom")
    public String getLabelGeom() {
        return labelGeom;
    }

    @JsonProperty("labelGeom")
    public void setLabelGeom(String labelGeom) {
        this.labelGeom = labelGeom;
    }
}
