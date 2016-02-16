package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

import eu.europa.ec.fisheries.uvms.spatial.entity.RfmoEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.RfmoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi", uses = GeometryMapper.class)
public interface RfmoMapper {

    RfmoMapper INSTANCE = Mappers.getMapper(RfmoMapper.class);

    @Mappings({
            @Mapping(source = "geometry", target = "geom"),
    })
    RfmoEntity rfmoDtoToRfmoEntity(RfmoDto rfmoDto);
    
}
