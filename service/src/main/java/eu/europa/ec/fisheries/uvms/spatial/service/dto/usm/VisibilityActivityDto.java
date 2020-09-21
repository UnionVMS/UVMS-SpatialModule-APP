package eu.europa.ec.fisheries.uvms.spatial.service.dto.usm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VisibilityActivityDto {


    @JsonProperty("popup")
    private VisibilityAttributesDto popup;

    @JsonProperty("labels")
    private VisibilityAttributesDto labels;

    @NotNull
    @JsonProperty("table")
    private VisibilityAttributesDto table;

    public VisibilityActivityDto() {}

    public VisibilityActivityDto(VisibilityAttributesDto popup, VisibilityAttributesDto labels, VisibilityAttributesDto table) {
        this.popup = popup;
        this.labels = labels;
        this.table = table;
    }

    @JsonProperty("popup")
    public VisibilityAttributesDto getPopup() {
        return popup;
    }

    @JsonProperty("popup")
    public void setPopup(VisibilityAttributesDto popup) {
        this.popup = popup;
    }

    @JsonProperty("labels")
    public VisibilityAttributesDto getLabels() {
        return labels;
    }

    @JsonProperty("labels")
    public void setLabels(VisibilityAttributesDto labels) {
        this.labels = labels;
    }

    @JsonProperty("table")
    public VisibilityAttributesDto getTable() {
        return table;
    }

    @JsonProperty("table")
    public void setTable(VisibilityAttributesDto table) {
        this.table = table;
    }
}
