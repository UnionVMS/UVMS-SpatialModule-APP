package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by padhyad on 11/30/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VisibilitySegmentDto {

    @JsonProperty("popup")
    private List<String> popup;

    @JsonProperty("labels")
    private List<String> labels;

    @NotNull
    @JsonProperty("table")
    private List<String> table;

    public VisibilitySegmentDto() {}

    public VisibilitySegmentDto(List<String> popup, List<String> labels, List<String> table) {
        this.popup = popup;
        this.labels = labels;
        this.table = table;
    }

    @JsonProperty("popup")
    public List<String> getPopup() {
        return popup;
    }

    @JsonProperty("popup")
    public void setPopup(List<String> popup) {
        this.popup = popup;
    }

    @JsonProperty("labels")
    public List<String> getLabels() {
        return labels;
    }

    @JsonProperty("labels")
    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    @JsonProperty("table")
    public List<String> getTable() {
        return table;
    }

    @JsonProperty("table")
    public void setTable(List<String> table) {
        this.table = table;
    }
}
