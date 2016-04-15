package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

import eu.europa.ec.fisheries.uvms.spatial.entity.PortEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.PortLocationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi", uses = GeometryMapper.class)
public interface PortLocationMapper {

    PortLocationMapper INSTANCE = Mappers.getMapper(PortLocationMapper.class);

    @Mappings({
            @Mapping(source = "geometry", target = "geom"),
    })
    PortEntity portLocationDtoToPortsEntity(PortLocationDto portLocationDto);

}
