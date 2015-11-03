package eu.europa.ec.fisheries.uvms.spatial.rest.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.AreaDetailsDto;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.AreaTypeDto;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.GeoCoordinateDto;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.LocationDetailsDto;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.LocationTypeDto;

@Mapper
public abstract class AreaLocationDtoMapper {
	
	private static String AREA_TYPE = "areaType";
	
	private static String LOCATION_TYPE = "locationType";
	
	public static AreaLocationDtoMapper INSTANCE = Mappers.getMapper(AreaLocationDtoMapper.class);
	
	public abstract AreaTypeEntry getAreaTypeEntry(AreaTypeDto areaDto);

	public abstract LocationTypeEntry getLocationTypeEntry(LocationTypeDto locationDto);
	
	@Mappings( {
		@Mapping(target = "properties", expression = "java(extractProperties(locationDetails))"),
		@Mapping(target = "type", expression = "java(extractType(locationDetails))")
	})
	public abstract LocationDetailsDto getLocationDetailsDto(LocationDetails locationDetails);
	
	@Mappings( {
		@Mapping(target = "properties", expression = "java(extractProperties(areaDetails))"),
		@Mapping(target = "type", expression = "java(extractType(areaDetails))")
	})
	public abstract AreaDetailsDto getAreaDetailsDto(AreaDetails areaDetails);
	
	public abstract List<AreaTypeEntry> getAreaTypeEntryList(List<AreaTypeDto> areaDtoList);
	
	public AreaDetailsDto getAreaDetailsDtoForAllAreas(List<AreaDetails> areaDetailsList, AreaTypeDto areaDto) {
		AreaDetailsDto areaDetailsDto = new AreaDetailsDto();
		areaDetailsDto.setAllAreaProperties(extractProperties(areaDetailsList));
		areaDetailsDto.setType(extractType(areaDto));
		return areaDetailsDto;
	}
	
	public abstract Coordinate getCoordinateFromDto(GeoCoordinateDto geoCoordinateDto);
	
	protected Map<String, String> extractProperties(AreaDetails areaDetails) {
		Map<String, String> propertyMap = new HashMap<String, String>();
		for (AreaProperty property : areaDetails.getAreaProperties()) {
			propertyMap.put(property.getPropertyName(), property.getPropertyValue());
		}
		if (!propertyMap.isEmpty()) {
			propertyMap.put(AREA_TYPE, String.valueOf(areaDetails.getAreaType().getAreaType()).toUpperCase());
		}
		return propertyMap;
	}
	
	protected List<Map<String, String>> extractProperties(List<AreaDetails> areaDetailsList) {
		List<Map<String, String>> allPropertyMap = new ArrayList<Map<String, String>>();
		for (AreaDetails areaDetails : areaDetailsList) {
			Map<String, String> propertyMap = extractProperties(areaDetails);
			allPropertyMap.add(propertyMap);
		}
		return allPropertyMap;
	}
	
	protected String extractType(AreaDetails areaDetails) {
		return String.valueOf(areaDetails.getAreaType().getAreaType());
	}
	
	protected String extractType(AreaTypeDto areaDto) {
		return String.valueOf(areaDto.getAreaType());
	}
	
	protected Map<String, String> extractProperties(LocationDetails locationDetails) {
		Map<String, String> propertyMap = new HashMap<String, String>();
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
