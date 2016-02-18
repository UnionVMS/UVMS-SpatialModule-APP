package eu.europa.ec.fisheries.uvms.spatial.rest.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.geojson.AreaDetailsGeoJsonDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaProperty;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Coordinate;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationProperty;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.geocoordinate.AreaTypeDto;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.geocoordinate.GeoCoordinateDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.geojson.LocationDetailsGeoJsonDto;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.geocoordinate.LocationTypeDto;

@Mapper
public abstract class AreaLocationDtoMapper {
	
	private static final String AREA_TYPE = "areaType";
	
	private static final String LOCATION_TYPE = "locationType";
	
	private static final AreaLocationDtoMapper INSTANCE = Mappers.getMapper(AreaLocationDtoMapper.class);

	public static AreaLocationDtoMapper mapper() {
		return INSTANCE;
	}
	
	public abstract AreaTypeEntry getAreaTypeEntry(AreaTypeDto areaDto);

	public abstract LocationTypeEntry getLocationTypeEntry(LocationTypeDto locationDto);
	
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
	
	public abstract List<AreaTypeEntry> getAreaTypeEntryList(List<AreaTypeDto> areaDtoList);
	
	public AreaDetailsGeoJsonDto getAreaDetailsDtoForAllAreas(List<AreaDetails> areaDetailsList, AreaTypeDto areaDto) {
		AreaDetailsGeoJsonDto areaDetailsGeoJsonDto = new AreaDetailsGeoJsonDto();
		areaDetailsGeoJsonDto.setAllAreaProperties(extractProperties(areaDetailsList));
		areaDetailsGeoJsonDto.setType(extractType(areaDto));
		return areaDetailsGeoJsonDto;
	}
	
	public abstract Coordinate getCoordinateFromDto(GeoCoordinateDto geoCoordinateDto);
	
	protected Map<String, Object> extractProperties(AreaDetails areaDetails) {
		Map<String, Object> propertyMap = new HashMap<>();
		for (AreaProperty property : areaDetails.getAreaProperties()) {
			propertyMap.put(property.getPropertyName(), property.getPropertyValue());
		}
		if (!propertyMap.isEmpty()) {
			propertyMap.put(AREA_TYPE, String.valueOf(areaDetails.getAreaType().getAreaType()).toUpperCase());
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
	
	protected String extractType(AreaTypeDto areaDto) {
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
