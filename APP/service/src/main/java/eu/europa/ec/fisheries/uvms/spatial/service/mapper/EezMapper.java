package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezType;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.EezDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi", uses = GeometryMapper.class)
public interface EezMapper {

    EezMapper INSTANCE = Mappers.getMapper(EezMapper.class);

    @Mappings({
            @Mapping(source = "geom", target = "geometry"),
    })
    EezDto eezEntityToEezDto(EezEntity eezEntity);

    @Mappings({
            @Mapping(source = "geometry", target = "geom"),
    })
    EezEntity eezDtoToEezEntity(EezDto eezDto);

    @Mappings({
            @Mapping(source = "geom", target = "geometry"),
    })
    EezType eezEntityToEezType(EezEntity eezEntity);

}
