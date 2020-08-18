/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.config.*;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.usm.*;
import eu.europa.ec.fisheries.uvms.spatial.service.enums.AreaTypeEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.*;

@Mapper
public abstract class MapConfigMapper {

    public static final MapConfigMapper INSTANCE = Mappers.getMapper(MapConfigMapper.class);

    public abstract PositionDto getPositionDtos(PositionsDto positionsDto);

    public abstract SegmentDto getSegmentDtos(SegmentsDto segmentDto);

    @Mappings({
            @Mapping(source = "positions", target = "positionDto"),
            @Mapping(source = "segments", target = "segmentDto"),
            @Mapping(source = "alarms", target = "alarmsDto")
    })
    public abstract VectorStylesDto getStyleDtos(StyleSettingsDto styleSettingsDto);

    public abstract TbControlDto getTbControl(ControlsDto controlsDto);

    public abstract ControlDto getControl(ControlsDto controlsDto);

    public abstract List<TbControlDto> getTbControls(List<ControlsDto> controlsDto);

    public abstract List<ControlDto> getControls(List<ControlsDto> controlsDto);

    @Mappings({
            @Mapping(source = "order", target = "orders"),
    })
    public abstract VisibilityAttributeType getVisibilityAttributeType(VisibilityAttributesDto visibilityAttributesDto);

    @Mappings({
            @Mapping(source = "orders", target = "order"),
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

    public abstract LayerSettingsType getLayerSettingsType(LayerSettingsDto layerSettingsDto);

    public abstract LayerSettingsDto getLayerSettingsDto(LayerSettingsType layerSettingsType);

    public abstract LayersType getLayersType(LayersDto layersDto);

    public abstract LayersDto getLayersDto(LayersType layersType);

    @Mappings({
            @Mapping(target = "areaType", expression = "java(getAreaType(layerAreaDto.getAreaType()))")
    })
    public abstract LayerAreaType getLayerAreaType(LayerAreaDto layerAreaDto);

    @Mappings({
            @Mapping(target = "areaType", expression = "java(getAreaTypeEnum(layerAreaType.getAreaType()))")
    })
    public abstract LayerAreaDto getLayerAreaDto(LayerAreaType layerAreaType);


    @Mappings({
            @Mapping(source = "positions", target = "position"),
            @Mapping(source = "segments", target = "segment"),
            @Mapping(source = "alarms", target = "alarm")
    })
    public abstract StyleSettingsType getStyleSettingsType(StyleSettingsDto styleSettingsDto);

    @Mappings({
            @Mapping(source = "position", target = "positions"),
            @Mapping(source = "segment", target = "segments"),
            @Mapping(source = "alarm", target = "alarms")
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

    public List<ReferenceDataType> getReferenceDataType(Map<String, ReferenceDataPropertiesDto> referenceData) {
        if (referenceData == null || referenceData.isEmpty()) {
            return Collections.emptyList();
        }
        List<ReferenceDataType> referenceDataTypes = new ArrayList<>();
        for (Map.Entry<String, ReferenceDataPropertiesDto> entry : referenceData.entrySet()) {
            referenceDataTypes.add(new ReferenceDataType(entry.getKey(), entry.getValue().getSelection(), entry.getValue().getCodes()));
        }
        return referenceDataTypes;
    }

    public Map<String, ReferenceDataPropertiesDto> getReferenceData(List<ReferenceDataType> referenceDataTypes) {
        if (referenceDataTypes == null || referenceDataTypes.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, ReferenceDataPropertiesDto> referenceData = new HashMap<>();
        for (ReferenceDataType referenceDataType : referenceDataTypes) {
            referenceData.put(referenceDataType.getType(),
                    new ReferenceDataPropertiesDto(referenceDataType.getSelection(), referenceDataType.getCodes()));
        }
        return referenceData;
    }

    protected String getAreaType(AreaTypeEnum areaTypeEnum) {
        return areaTypeEnum.getType();
    }

    protected AreaTypeEnum getAreaTypeEnum(String areaType) {
        return AreaTypeEnum.valueOf(areaType);
    }

    protected AlarmType getAlarmType(AlarmsDto alarmsDto) {
        if (alarmsDto == null) {
            return null;
        }
        AlarmType alarmType = new AlarmType();
        alarmType.setSize(alarmsDto.getSize());
        alarmType.setOpen(alarmsDto.getOpen());
        alarmType.setClosed(alarmsDto.getClosed());
        alarmType.setPending(alarmsDto.getPending());
        alarmType.setNone(alarmsDto.getNone());
        return alarmType;
    }

    protected AlarmsDto getAlarmDto(AlarmType alarmType) {
        if (alarmType == null) {
            return null;
        }
        AlarmsDto alarmsDto = new AlarmsDto();
        alarmsDto.setSize(alarmType.getSize());
        alarmsDto.setOpen(alarmType.getOpen());
        alarmsDto.setClosed(alarmType.getClosed());
        alarmsDto.setPending(alarmType.getPending());
        alarmsDto.setNone(alarmType.getNone());
        return alarmsDto;
    }

    protected List<StyleDataType> convertToStyleType(Map<String, String> style) {
        if (style == null) {
            return Collections.emptyList();
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
            return Collections.emptyMap();
        }
        Map<String, String> styleMap = new HashMap<>();
        for (StyleDataType styleDataType : styleDataTypes) {
            styleMap.put(styleDataType.getKey(), styleDataType.getValue());
        }
        return styleMap;
    }
}