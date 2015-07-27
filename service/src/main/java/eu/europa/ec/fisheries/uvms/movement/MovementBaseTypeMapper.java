package eu.europa.ec.fisheries.uvms.movement;

import eu.europa.ec.fisheries.schema.movement.v1.MovementBaseType;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.MovementDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * //TODO add test
 */
@Mapper
public interface MovementBaseTypeMapper {

    MovementBaseTypeMapper INSTANCE = Mappers.getMapper(MovementBaseTypeMapper.class);

    MovementDto movementBaseTypeToMovementDto(MovementBaseType movementBaseType);
}

