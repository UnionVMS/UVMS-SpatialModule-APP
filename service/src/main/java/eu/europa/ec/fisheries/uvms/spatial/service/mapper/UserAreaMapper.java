package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

import eu.europa.ec.fisheries.uvms.spatial.entity.UserAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UserAreaGeomDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class UserAreaMapper {

    private static UserAreaMapper INSTANCE = Mappers.getMapper(UserAreaMapper.class);

    public static UserAreaMapper mapper() {
        return INSTANCE;
    }

    @Mappings({
            @Mapping(source = "geometry", target = "geom"),
            @Mapping(target = "isShared", expression = "java(userAreaDto.isShared())"),
            @Mapping(target = "name", expression = "java(userAreaDto.getName())"),
            @Mapping(target = "areaDesc", expression = "java(userAreaDto.getDesc())")
    })
    public abstract UserAreasEntity fromDtoToEntity(UserAreaGeomDto userAreaDto);

}
