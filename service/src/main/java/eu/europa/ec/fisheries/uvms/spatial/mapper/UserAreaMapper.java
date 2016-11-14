/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.mapper;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.spatial.entity.UserAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.UserScopeEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.geojson.UserAreaGeoJsonDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

import static eu.europa.ec.fisheries.uvms.common.DateUtils.DATE_TIME_UI_FORMAT;

@Mapper
public abstract class UserAreaMapper {

    private static UserAreaMapper INSTANCE = Mappers.getMapper(UserAreaMapper.class);

    public static UserAreaMapper mapper() {
        return INSTANCE;
    }

    @Mappings({
            @Mapping(target = "type", source = "subType"),
            @Mapping(target = "areaDesc", source = "desc"),
            @Mapping(source = "geometry", target = "geom"),
            @Mapping(target = "startDate", dateFormat = DATE_TIME_UI_FORMAT),
            @Mapping(target = "endDate", dateFormat = DATE_TIME_UI_FORMAT),
            @Mapping(target = "scopeSelection", expression = "java(fromScopeArrayToEntity(userAreaDto.getScopeSelection()))"),
    })
    public abstract UserAreasEntity fromDtoToEntity(UserAreaGeoJsonDto userAreaDto);

    @Mappings({
            @Mapping(target = "type", source = "subType"),
            @Mapping(target = "areaDesc", source = "desc"),
            @Mapping(source = "geometry", target = "geom"),
            @Mapping(target = "startDate", dateFormat = DATE_TIME_UI_FORMAT),
            @Mapping(target = "endDate", dateFormat = DATE_TIME_UI_FORMAT),
    })
    public abstract void updateUserAreaEntity(UserAreaGeoJsonDto userAreaDto, @MappingTarget UserAreasEntity userAreasEntity);

    public static Set<UserScopeEntity> fromScopeArrayToEntity(List<String> scopeSelection) {
        Set<UserScopeEntity> userScopeEntities = Sets.newHashSet();

        if (scopeSelection != null) {
            for (String scope : scopeSelection) {
                UserScopeEntity userScopeEntity = new UserScopeEntity();
                userScopeEntity.setName(scope);
                userScopeEntities.add(userScopeEntity);
            }
        }

        return userScopeEntities;
    }

    public static Set<UserScopeEntity> fromScopeArrayToEntity(List<String> scopeSelection, UserAreasEntity userAreasEntity) {
        Set<UserScopeEntity> userScopeEntities = Sets.newHashSet();

        if (scopeSelection != null) {
            for (String scope : scopeSelection) {
                UserScopeEntity userScopeEntity = new UserScopeEntity();
                userScopeEntity.setName(scope);
                userScopeEntity.setUserAreas(userAreasEntity);
                userScopeEntities.add(userScopeEntity);
            }
        }

        return userScopeEntities;
    }

    public static List<String> fromEntityToScopeArray(Set<UserScopeEntity> scopeSelection) {
        List<String> returnArray = Lists.newArrayList();

        if (scopeSelection != null) {
            for (UserScopeEntity scope : scopeSelection) {
                returnArray.add(scope.getName());
            }
        }

        return returnArray;
    }

    public UserAreaGeoJsonDto fromEntityToDto(UserAreasEntity userAreaEntity, boolean isGeometryNeeded) {
        UserAreaGeoJsonDto dto = new UserAreaGeoJsonDto();
        dto.setName(userAreaEntity.getName());
        dto.setDatasetName(userAreaEntity.getDatasetName());
        dto.setDesc(userAreaEntity.getAreaDesc());
        dto.setId(userAreaEntity.getId());
        dto.setType(userAreaEntity.getType());
        dto.setScopeSelection(fromEntityToScopeArray(userAreaEntity.getScopeSelection()));
        Geometry geometry = userAreaEntity.getGeom();

        if (!isGeometryNeeded){
            //that removes the geometry and sets extent. Be aware that Jackson don't serialize automatically the geometry well
            dto.getProperties().put(UserAreaGeoJsonDto.EXTENT, dto.getExtend(geometry));
        } else {
            dto.setGeometry(geometry);
        }

        return dto;
    }

    public List<UserAreaGeoJsonDto> fromEntityListToDtoList(List<UserAreasEntity> userAreas, boolean isGeometryNeeded) {
        List<UserAreaGeoJsonDto> listAreas = Lists.newArrayList();
        for(UserAreasEntity entity : userAreas) {
            listAreas.add(fromEntityToDto(entity, isGeometryNeeded));
        }

        return listAreas;
    }

}