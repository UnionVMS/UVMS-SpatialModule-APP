package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi", uses = GeometryTypeMapper.class)
public interface EezTypeMapper {

    EezTypeMapper INSTANCE = Mappers.getMapper(EezTypeMapper.class);

    @Mappings({
            @Mapping(source = "geom", target = "geometry"),
    })
    EezType eezEntityToEezType(EezEntity eezEntity);

}
