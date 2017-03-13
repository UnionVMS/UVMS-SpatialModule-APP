/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.rest.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaProperty;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Coordinate;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationProperty;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.AreaCoordinateType;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.GeoCoordinateType;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.LocationCoordinateType;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.geojson.AreaDetailsGeoJsonDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.geojson.LocationDetailsGeoJsonDto;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.UserScopeEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.UserAreaMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class AreaLocationMapper {

	private static final String AREA_TYPE = "areaType";
	private static final String LOCATION_TYPE = "locationType";
	public static final String AREA_ID = "id";
	
	private static final AreaLocationMapper INSTANCE = Mappers.getMapper(AreaLocationMapper.class);


	public static AreaLocationMapper mapper() {
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
			Object propertyValue;

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
		List<Map<String, Object>> allPropertyMap = new ArrayList<>();
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