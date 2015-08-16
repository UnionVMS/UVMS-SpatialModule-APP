package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezType;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi", uses = GeometryMapper.class)
public interface EezMapper {

    EezType eezEntityToEezType(EezEntity eezEntity);

}
