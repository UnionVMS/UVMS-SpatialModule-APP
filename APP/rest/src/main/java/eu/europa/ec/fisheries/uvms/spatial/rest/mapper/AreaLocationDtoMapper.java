package eu.europa.ec.fisheries.uvms.spatial.rest.mapper;

import java.util.*;

import eu.europa.ec.fisheries.uvms.spatial.entity.UserScopeEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.rest.type.geocoordinate.AreaCoordinateType;
import eu.europa.ec.fisheries.uvms.spatial.rest.type.geocoordinate.GeoCoordinateType;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.geojson.AreaDetailsGeoJsonDto;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.UserAreaMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.geojson.LocationDetailsGeoJsonDto;
import eu.europa.ec.fisheries.uvms.spatial.rest.type.geocoordinate.LocationCoordinateType;

@Mapper
public abstract class AreaLocationDtoMapper {

	private static final String AREA_TYPE = "areaType";
	private static final String LOCATION_TYPE = "locationType";
	public static final String AREA_ID = "id";
	
	private static final AreaLocationDtoMapper INSTANCE = Mappers.getMapper(AreaLocationDtoMapper.class);


	public static AreaLocationDtoMapper mapper() {
		return INSTANCE;
	}

	@Mappings( {
			@Mapping(target = "areaType", expression = "java(getAreaTypeEnum(areaDto.getAreaType()))")
	})
	public abstract AreaTypeEntry getAreaTypeEntry(AreaCoordinateType areaDto);

	public abstract LocationTypeEntry getLocationTypeEntry(LocationCoordinateType locationDto);
	
	@Mappings( {
		@Mapping(target = "properties", expression = "java(extractProperties(locationDetails))"),
		@Mapping(target = "type", expression = "java(extractType(locationDetails))")
	})
	public abstract LocationDetailsGeoJsonDto getLocationDetailsDto(LocationDetails locationDetails);
	
	@Mappings( {
		@Mapping(target = "properties", expression = "java(extractProperties(areaDetails))"),
		@Mapping(target = "type", expression = "java(extractType(areaDetails))")
	})
	public abstract AreaDetailsGeoJsonDto getAreaDetailsDto(AreaDetails areaDetails);
	
	public abstract List<AreaTypeEntry> getAreaTypeEntryList(List<AreaCoordinateType> areaDtoList);
	
	public AreaDetailsGeoJsonDto getAreaDetailsDtoForAllAreas(List<AreaDetails> areaDetailsList, AreaCoordinateType areaDto) {
		AreaDetailsGeoJsonDto areaDetailsGeoJsonDto = new AreaDetailsGeoJsonDto();
		areaDetailsGeoJsonDto.setAllAreaProperties(extractProperties(areaDetailsList));
		areaDetailsGeoJsonDto.setType(extractType(areaDto));
		return areaDetailsGeoJsonDto;
	}
	
	public abstract Coordinate getCoordinateFromDto(GeoCoordinateType geoCoordinateType);

	protected AreaType getAreaTypeEnum(String areaType) {
		if ( areaType == null ) {
			return null;
		}
		return Enum.valueOf( AreaType.class, areaType.toUpperCase());
	}
	
	protected Map<String, Object> extractProperties(AreaDetails areaDetails) {
		Map<String, Object> propertyMap = new HashMap<>();
		for (AreaProperty property : areaDetails.getAreaProperties()) {
			Object propertyValue = null;

			if ("scopeSelection".equalsIgnoreCase(property.getPropertyName()) ) {
				propertyValue = UserAreaMapper.fromEntityToScopeArray((Set<UserScopeEntity>) property.getPropertyValue());
			} else {
				propertyValue = property.getPropertyValue();
			}

			propertyMap.put(property.getPropertyName(), propertyValue);
		}
		if (!propertyMap.isEmpty()) {
			propertyMap.put(AREA_TYPE, String.valueOf(areaDetails.getAreaType().getAreaType()).toUpperCase());
			propertyMap.put(AREA_ID, areaDetails.getAreaType().getId());
		}
		return propertyMap;
	}
	
	protected List<Map<String, Object>> extractProperties(List<AreaDetails> areaDetailsList) {
		List<Map<String, Object>> allPropertyMap = new ArrayList<Map<String, Object>>();
		for (AreaDetails areaDetails : areaDetailsList) {
			Map<String, Object> propertyMap = extractProperties(areaDetails);
			allPropertyMap.add(propertyMap);
		}
		return allPropertyMap;
	}
	
	protected String extractType(AreaDetails areaDetails) {
		return String.valueOf(areaDetails.getAreaType().getAreaType());
	}
	
	protected String extractType(AreaCoordinateType areaDto) {
		return areaDto.getAreaType();
	}
	
	protected Map<String, Object> extractProperties(LocationDetails locationDetails) {
		Map<String, Object> propertyMap = new HashMap<>();
		for (LocationProperty property : locationDetails.getLocationProperties()) {
			propertyMap.put(property.getPropertyName(), property.getPropertyValue());
		}
		if (!propertyMap.isEmpty()) {
			propertyMap.put(LOCATION_TYPE, String.valueOf(locationDetails.getLocationType().getLocationType()).toUpperCase());
		}
		return propertyMap;
	}
	
	protected String extractType(LocationDetails locationDetails) {
		return String.valueOf(locationDetails.getLocationType().getLocationType());
	}
}
