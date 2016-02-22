package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

import eu.europa.ec.fisheries.uvms.spatial.entity.UserAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.UserScopeEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.geojson.UserAreaGeoJsonDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static eu.europa.ec.fisheries.uvms.common.DateUtils.*;
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
            @Mapping(target = "datasetName", expression = "java(userAreaDto.getDatasetName())")
    })
    public abstract UserAreasEntity fromDtoToEntity(UserAreaGeoJsonDto userAreaDto);

    protected Date stringToDate(String date) {
        if (isEmpty(date)) {
            return null;
        }
        return UI_FORMATTER.parseDateTime(date).toDate();
    }

    protected Set<UserScopeEntity> fromScopeArrayToEntity(List<String> scopeSelection) {
        Set<UserScopeEntity> userScopeEntities = null;

        if (scopeSelection != null) {
            userScopeEntities = new HashSet<>(scopeSelection.size());
            for (String scope : scopeSelection) {
                UserScopeEntity userScopeEntity = new UserScopeEntity();
                userScopeEntity.setName(scope);
                userScopeEntities.add(userScopeEntity);
            }
        }

        return userScopeEntities;
    }

}
