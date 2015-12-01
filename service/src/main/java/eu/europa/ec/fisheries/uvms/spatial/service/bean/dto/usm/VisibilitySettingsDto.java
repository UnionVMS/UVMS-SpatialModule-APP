package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by padhyad on 11/30/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VisibilitySettingsDto {

    @JsonProperty("positions")
    private VisibilityPositionsDto visibilityPositionsDto;

    @JsonProperty("segments")
    private VisibilitySegmentDto visibilitySegmentDto;

    public VisibilitySettingsDto() {}

    public VisibilitySettingsDto(VisibilityPositionsDto visibilityPositionsDto, VisibilitySegmentDto visibilitySegmentDto) {
        this.visibilityPositionsDto = visibilityPositionsDto;
        this.visibilitySegmentDto = visibilitySegmentDto;
    }

    @JsonProperty("positions")
    public VisibilityPositionsDto getVisibilityPositionsDto() {
        return visibilityPositionsDto;
    }

    @JsonProperty("positions")
    public void setVisibilityPositionsDto(VisibilityPositionsDto visibilityPositionsDto) {
        this.visibilityPositionsDto = visibilityPositionsDto;
    }

    @JsonProperty("segments")
    public VisibilitySegmentDto getVisibilitySegmentDto() {
        return visibilitySegmentDto;
    }

    @JsonProperty("segments")
    public void setVisibilitySegmentDto(VisibilitySegmentDto visibilitySegmentDto) {
        this.visibilitySegmentDto = visibilitySegmentDto;
    }
}
