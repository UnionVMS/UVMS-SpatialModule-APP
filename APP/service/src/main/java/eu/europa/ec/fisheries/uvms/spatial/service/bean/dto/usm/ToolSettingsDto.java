/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries © European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
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