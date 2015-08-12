package eu.europa.ec.fisheries.uvms.spatial.rest.mapper;

import eu.europa.ec.fisheries.schema.spatial.types.EezType;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.EezDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * Created by kopyczmi on 11-Aug-15.
 */
@Mapper(componentModel = "cdi")
public interface EezDtoMapper {

    @Mappings({
            @Mapping(target = "geometryType", source = "geom.geometryType"),
            @Mapping(target = "geometryJson", source = "geom.geometryJson")
    })
    EezDto eezSchemaToDto(EezType eez);

}

