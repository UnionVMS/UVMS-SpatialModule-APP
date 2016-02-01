package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

/**
 * Created by padhyad on 11/26/2015.
 */

import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.*;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface MapConfigMapper {

    MapConfigMapper INSTANCE = Mappers.getMapper(MapConfigMapper.class);

    PositionDto getPositionDtos(PositionsDto positionsDto);

    SegmentDto getSegmentDtos(SegmentsDto segmentDto);

    @Mappings({
            @Mapping(source = "positions", target = "positionDto"),
            @Mapping(source = "segments", target = "segmentDto")
    })
    VectorStylesDto getStyleDtos(StyleSettingsDto styleSettingsDto);

    TbControlDto getTbControl(ControlsDto controlsDto);

    ControlDto getControl(ControlsDto controlsDto);

    List<TbControlDto> getTbControls(List<ControlsDto> controlsDto);

    List<ControlDto> getControls(List<ControlsDto> controlsDto);
}
