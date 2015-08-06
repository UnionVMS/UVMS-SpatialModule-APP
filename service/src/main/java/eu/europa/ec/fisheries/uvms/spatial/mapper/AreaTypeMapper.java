package eu.europa.ec.fisheries.uvms.spatial.mapper;

import eu.europa.ec.fisheries.uvms.spatial.dto.AreaDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * //TODO create test
 */
@Mapper
public interface AreaTypeMapper {

    AreaTypeMapper INSTANCE = Mappers.getMapper(AreaTypeMapper.class);

    AreaDto areaTypeToAreaDto(AreaTypeMapper areaTypeMapper);
}
