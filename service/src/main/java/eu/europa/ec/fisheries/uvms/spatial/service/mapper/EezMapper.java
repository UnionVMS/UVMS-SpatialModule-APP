package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import eu.schemas.EezType;
import org.mapstruct.Mapper;

/**
 * Created by kopyczmi on 11-Aug-15.
 */
@Mapper(componentModel = "cdi", uses = GeometryMapper.class)
public interface EezMapper {
    EezType eezEntityToSchema(EezEntity eezEntity);
}
