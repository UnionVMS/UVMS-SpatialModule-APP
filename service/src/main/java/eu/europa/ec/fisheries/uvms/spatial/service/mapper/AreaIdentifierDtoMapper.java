package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaIdentifierType;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.AreaIdentifierDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AreaIdentifierDtoMapper {

    AreaIdentifierDtoMapper INSTANCE = Mappers.getMapper(AreaIdentifierDtoMapper.class);

    AreaIdentifierDto areaIdentifierTypeToAreaDto(AreaIdentifierType areaIdentifierType);

}
