package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm.AlarmsDto;

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

    /**
     * No args constructor for use in serialization
     */
    public VectorStylesDto() {
    }

    public VectorStylesDto(PositionDto positionDto, SegmentDto segmentDto, AlarmsDto alarmsDto) {
        this.positionDto = positionDto;
        this.segmentDto = segmentDto;
        this.alarmsDto = alarmsDto;
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
}
