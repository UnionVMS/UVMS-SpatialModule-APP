/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.dto.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.usm.AlarmsDto;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "positions",
        "segments"
})
public class VectorStylesDto {

    @JsonProperty("positions")
    private PositionDto positionDto;

    @JsonProperty("segments")
    private SegmentDto segmentDto;

    @JsonProperty("alarms")
    private AlarmsDto alarmsDto;

    @JsonProperty("activities")
    private ActivityDto activityDto;

    /**
     * No args constructor for use in serialization
     */
    public VectorStylesDto() {
    }

    public VectorStylesDto(PositionDto positionDto, SegmentDto segmentDto, AlarmsDto alarmsDto,ActivityDto activitiesDto) {
        this.positionDto = positionDto;
        this.segmentDto = segmentDto;
        this.alarmsDto = alarmsDto;
        this.activityDto = activitiesDto;
    }

    @JsonProperty("positions")
    public PositionDto getPositionDto() {
        return positionDto;
    }

    @JsonProperty("positions")
    public void setPositionDto(PositionDto positionDto) {
        this.positionDto = positionDto;
    }

    @JsonProperty("segments")
    public SegmentDto getSegmentDto() {
        return segmentDto;
    }

    @JsonProperty("segments")
    public void setSegmentDto(SegmentDto segmentDto) {
        this.segmentDto = segmentDto;
    }

    @JsonProperty("alarms")
    public AlarmsDto getAlarmsDto() {
        return alarmsDto;
    }

    @JsonProperty("alarms")
    public void setAlarmsDto(AlarmsDto alarmsDto) {
        this.alarmsDto = alarmsDto;
    }

    @JsonProperty("activities")
    public void setActivityDto(ActivityDto activitiesDto) {
        this.activityDto = activitiesDto;
    }
}