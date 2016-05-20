package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.spatial.entity.UserAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.UserScopeEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.geojson.UserAreaGeoJsonDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static eu.europa.ec.fisheries.uvms.common.DateUtils.UI_FORMATTER;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@Mapper
public abstract class UserAreaMapper {

    private static UserAreaMapper INSTANCE = Mappers.getMapper(UserAreaMapper.class);

    public static UserAreaMapper mapper() {
        return INSTANCE;
    }

    @Mappings({
            @Mapping(target = "name", expression = "java(userAreaDto.getName())"),
            @Mapping(target = "type", expression = "java(userAreaDto.getSubType())"),
            @Mapping(target = "areaDesc", expression = "java(userAreaDto.getDesc())"),
            @Mapping(source = "geometry", target = "geom"),
            @Mapping(target = "startDate", expression = "java(stringToDate(userAreaDto.getStartDate()))"),
            @Mapping(target = "endDate", expression = "java(stringToDate(userAreaDto.getEndDate()))"),
            @Mapping(target = "scopeSelection", expression = "java(fromScopeArrayToEntity(userAreaDto.getScopeSelection()))"),
            @Mapping(target = "datasetName", expression = "java(userAreaDto.getDatasetName())"),
    })
    public abstract UserAreasEntity fromDtoToEntity(UserAreaGeoJsonDto userAreaDto);

    protected Date stringToDate(String date) {
        if (isEmpty(date)) {
            return null;
        }
        return UI_FORMATTER.parseDateTime(date).toDate();
    }

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
