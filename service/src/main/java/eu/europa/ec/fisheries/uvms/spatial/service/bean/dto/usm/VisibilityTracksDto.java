package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by padhyad on 12/22/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VisibilityTracksDto {

    @NotNull
    @JsonProperty("table")
    private List<String> table;

    public VisibilityTracksDto(){}

    public VisibilityTracksDto(List<String> table) {
        this.table = table;
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
