package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

import eu.europa.ec.fisheries.schema.spatial.types.EezType;
import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * Created by kopyczmi on 06-Aug-15.
 */
@Mapper(uses = GeometryMapper.class)
public interface EezMapper {

    EezMapper INSTANCE = Mappers.getMapper(EezMapper.class);

    @Mappings({
            @Mapping(source = "eez", target = "name"),
            @Mapping(source = "geom", target = "geometry")
    })
    EezType eezEntityToSchema(EezEntity eezEntity);

}