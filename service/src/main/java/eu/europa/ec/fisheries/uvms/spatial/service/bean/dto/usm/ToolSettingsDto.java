package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by padhyad on 11/25/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ToolSettingsDto {

    @NotNull
    @JsonProperty("control")
    private List<ControlsDto> control;

    @NotNull
    @JsonProperty("tbControl")
    private List<ControlsDto> tbControl;

    public ToolSettingsDto() {}

    public ToolSettingsDto(List<ControlsDto> control, List<ControlsDto> tbControl) {
        this.control = control;
        this.tbControl = tbControl;
    }

    @JsonProperty("control")
    public List<ControlsDto> getControl() {
        return control;
    }

    @JsonProperty("control")
    public void setControl(List<ControlsDto> control) {
        this.control = control;
    }

    @JsonProperty("tbControl")
    public List<ControlsDto> getTbControl() {
        return tbControl;
    }

    @JsonProperty("tbControl")
    public void setTbControl(List<ControlsDto> tbControl) {
        this.tbControl = tbControl;
    }

    @Override
    public String toString() {
        return "ClassPojo [control = " + control + ", tbControl = " + tbControl + "]";
    }
}
