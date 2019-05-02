/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.Service2.dto.config;

import com.fasterxml.jackson.annotation.*;

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