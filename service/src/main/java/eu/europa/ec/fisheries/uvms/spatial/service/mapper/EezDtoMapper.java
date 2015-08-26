package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.EezDto;
import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * //TODO create test
 */
@Mapper(componentModel = "cdi", uses = GeometryTypeMapper.class)
public interface EezDtoMapper {

    EezDtoMapper INSTANCE = Mappers.getMapper(EezDtoMapper.class);

    @Mappings({
            @Mapping(source = "geom", target = "geometry"),
    })
    EezDto eezEntityToEezDto(EezEntity eezEntity);

}
