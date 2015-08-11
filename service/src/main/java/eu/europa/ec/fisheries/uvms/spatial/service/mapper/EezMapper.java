package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

import eu.europa.ec.fisheries.schema.spatial.types.EezType;
import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import org.mapstruct.Mapper;

/**
 * Created by kopyczmi on 11-Aug-15.
 */
@Mapper(componentModel = "cdi", uses = GeometryMapper.class)
public interface EezMapper {
    EezType eezEntityToSchema(EezEntity eezEntity);
}
