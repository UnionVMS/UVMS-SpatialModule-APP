package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

/**
 * Created by padhyad on 11/26/2015.
 */

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.*;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = "cdi")
public abstract class MapConfigMapper {

    public static MapConfigMapper INSTANCE = Mappers.getMapper(MapConfigMapper.class);

    public abstract PositionDto getPositionDtos(PositionsDto positionsDto);

    public abstract SegmentDto getSegmentDtos(SegmentsDto segmentDto);

    @Mappings({
            @Mapping(source = "positions", target = "positionDto"),
            @Mapping(source = "segments", target = "segmentDto")
    })
    public abstract VectorStylesDto getStyleDtos(StyleSettingsDto styleSettingsDto);

    public abstract TbControlDto getTbControl(ControlsDto controlsDto);

    public abstract ControlDto getControl(ControlsDto controlsDto);

    public abstract List<TbControlDto> getTbControls(List<ControlsDto> controlsDto);

    public abstract List<ControlDto> getControls(List<ControlsDto> controlsDto);

    @Mappings({
            @Mapping(source = "order", target = "orders"),
            @Mapping(target = "isAttributeVisible", expression = "java(getAttributeValue(visibilityAttributesDto.isAttributeVisible()))")
    })
    public abstract VisibilityAttributeType getVisibilityAttributeType(VisibilityAttributesDto visibilityAttributesDto);

    @Mappings({
            @Mapping(source = "orders", target = "order"),
            @Mapping(target = "isAttributeVisible", expression = "java(getAttributeValue(visibilityAttributeType.isIsAttributeVisible()))")
    })
    public abstract VisibilityAttributesDto getVisibilityAttributeDto(VisibilityAttributeType visibilityAttributeType);

    public abstract VisibilityPositionsType getVisibilityPositionsType(VisibilityPositionsDto visibilityPositionsDto);

    public abstract VisibilityPositionsDto getVisibilityPositionsDto(VisibilityPositionsType visibilityPositionsType);

    public abstract VisibilitySegmentType getVisibilitySegmentsType(VisibilitySegmentDto visibilitySegmentDto);

    public abstract VisibilitySegmentDto getVisibilitySegmentsDto(VisibilitySegmentType visibilitySegmentType);

    public abstract VisibilityTracksType getVisibilityTracksType(VisibilityTracksDto visibilityTracksDto);

    public abstract VisibilityTracksDto getVisibilityTracksDto(VisibilityTracksType visibilityTracksType);

    @Mappings({
            @Mapping(source = "visibilityPositionsDto", target = "positions"),
            @Mapping(source = "visibilitySegmentDto", target = "segments"),
            @Mapping(source = "visibilityTracksDto", target = "tracks")
    })
    public abstract VisibilitySettingsType getVisibilitySettingsType(VisibilitySettingsDto visibilitySettingsDto);

    @Mappings({
            @Mapping(source = "positions", target = "visibilityPositionsDto"),
            @Mapping(source = "segments", target = "visibilitySegmentDto"),
            @Mapping(source = "tracks", target = "visibilityTracksDto")
    })
    public abstract VisibilitySettingsDto getVisibilitySettingsDto(VisibilitySettingsType visibilitySettingsType);

    @Mappings({
            @Mapping(source = "positions", target = "position"),
            @Mapping(source = "segments", target = "segment")
    })
    public abstract StyleSettingsType getStyleSettingsType(StyleSettingsDto styleSettingsDto);

    @Mappings({
            @Mapping(source = "position", target = "positions"),
            @Mapping(source = "segment", target = "segments")
    })
    public abstract StyleSettingsDto getStyleSettingsDto(StyleSettingsType styleSettingsType);

    @Mappings({
            @Mapping(target = "styles", expression = "java(convertToStyleType(positionsDto.getStyle()))")
    })
    public abstract PositionType getPositionType(PositionsDto positionsDto);

    @Mappings({
            @Mapping(target = "style", expression = "java(convertToStyleMap(positionType.getStyles()))")
    })
    public abstract PositionsDto getPositionsDto(PositionType positionType);

    @Mappings({
            @Mapping(target = "styles", expression = "java(convertToStyleType(segmentsDto.getStyle()))")
    })
    public abstract SegmentType getSegmentType(SegmentsDto segmentsDto);

    @Mappings({
            @Mapping(target = "style", expression = "java(convertToStyleMap(segmentType.getStyles()))")
    })
    public abstract SegmentsDto getSegmentDto(SegmentType segmentType);

    protected Boolean getAttributeValue(Boolean isAttrVisible) {
        if (isAttrVisible == null) {
            return false;
        }
        return isAttrVisible;
    }

    protected List<StyleDataType> convertToStyleType(Map<String, String> style) {
        if (style == null) {
            return null;
        }
        List<StyleDataType> styleDataTypes = new ArrayList<>();
        for (Map.Entry<String, String> entry : style.entrySet()) {
            StyleDataType styleDataType = new StyleDataType(entry.getKey(), entry.getValue());
            styleDataTypes.add(styleDataType);
        }
        return styleDataTypes;
    }

    protected Map<String, String> convertToStyleMap(List<StyleDataType> styleDataTypes) {
        if(styleDataTypes == null || styleDataTypes.isEmpty()) {
            return null;
        }
        Map<String, String> styleMap = new HashMap<>();
        for (StyleDataType styleDataType : styleDataTypes) {
            styleMap.put(styleDataType.getKey(), styleDataType.getValue());
        }
        return styleMap;
    }
}
