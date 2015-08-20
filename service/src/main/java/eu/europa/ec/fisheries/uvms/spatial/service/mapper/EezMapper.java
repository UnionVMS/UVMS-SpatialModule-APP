package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "cdi", uses = GeometryMapper.class)
public interface EezMapper {

    @Mappings({
            @Mapping(source = "geom", target = "geometry"),
    })
    EezType eezEntityToEezType(EezEntity eezEntity);

}
