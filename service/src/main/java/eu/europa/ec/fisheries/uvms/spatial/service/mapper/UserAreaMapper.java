package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

import eu.europa.ec.fisheries.uvms.spatial.entity.UserAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UserAreaGeomDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserAreaMapper {

    UserAreaMapper INSTANCE = Mappers.getMapper(UserAreaMapper.class);

    @Mappings({
            @Mapping(source = "geometry", target = "geom"),
            @Mapping(target = "isShared", expression = "java(userAreaDto.isShared())"),
            @Mapping(target = "name", expression = "java(userAreaDto.getName())"),
            @Mapping(target = "areaDesc", expression = "java(userAreaDto.getAreaDesc())")
    })
    UserAreasEntity fromDtoToEntity(UserAreaGeomDto userAreaDto);
}
