package eu.europa.ec.fisheries.uvms.spatial.rest.mapper;

import java.util.HashMap;
import java.util.Map;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaProperty;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationProperty;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.AreaDetailsDto;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.AreaTypeDto;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.LocationDetailsDto;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.LocationTypeDto;

@Mapper
public abstract class AreaLocationDtoMapper {
	
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
		
	protected Map<String, String> extractProperties(AreaDetails areaDetails) {
		Map<String, String> propertyMap = new HashMap<String, String>();
		for (AreaProperty property : areaDetails.getAreaProperty()) {
			propertyMap.put(property.getPropertyName(), property.getPropertyValue());
		}
		return propertyMap;
	}
	
	protected String extractType(AreaDetails areaDetails) {
		return String.valueOf(areaDetails.getAreaType().getAreaType());
	}
	
	protected Map<String, String> extractProperties(LocationDetails locationDetails) {
		Map<String, String> propertyMap = new HashMap<String, String>();
		for (LocationProperty property : locationDetails.getLocationProperty()) {
			propertyMap.put(property.getPropertyName(), property.getPropertyValue());
		}
		return propertyMap;
	}
	
	protected String extractType(LocationDetails locationDetails) {
		return String.valueOf(locationDetails.getLocationType().getLocationType());
	}
}
