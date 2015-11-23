package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "positions",
        "segments"
})
public class VectorStylesDto {

    @JsonProperty("positions")
    private PositionsDto positionsDto;

    @JsonProperty("segments")
    private SegmentDto segmentDto;

    /**
     * No args constructor for use in serialization
     */
    public VectorStylesDto() {
    }

    public VectorStylesDto(PositionsDto positionsDto, SegmentDto segmentDto) {
        this.positionsDto = positionsDto;
        this.segmentDto = segmentDto;
    }

    @JsonProperty("positions")
    public PositionsDto getPositionsDto() {
        return positionsDto;
    }

    @JsonProperty("positions")
    public void setPositionsDto(PositionsDto positionsDto) {
        this.positionsDto = positionsDto;
    }

    @JsonProperty("segments")
    public SegmentDto getSegmentDto() {
        return segmentDto;
    }

    @JsonProperty("segments")
    public void setSegmentDto(SegmentDto segmentDto) {
        this.segmentDto = segmentDto;
    }
}
