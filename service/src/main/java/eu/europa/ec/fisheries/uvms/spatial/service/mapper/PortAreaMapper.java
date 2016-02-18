package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

import eu.europa.ec.fisheries.uvms.spatial.entity.PortAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.PortAreaDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi", uses = GeometryMapper.class)
public interface PortAreaMapper {

    PortAreaMapper INSTANCE = Mappers.getMapper(PortAreaMapper.class);

    @Mappings({
            @Mapping(source = "geometry", target = "geom"),
    })
    PortAreasEntity portAreaDtoToPortAreasEntity(PortAreaDto portAreaDto);

}
