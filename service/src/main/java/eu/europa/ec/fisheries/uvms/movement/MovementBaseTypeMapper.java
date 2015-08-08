package eu.europa.ec.fisheries.uvms.movement;

import eu.europa.ec.fisheries.schema.movement.v1.MovementBaseType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * //TODO add test
 */
@Mapper(componentModel="cdi")
public interface MovementBaseTypeMapper {

    MovementBaseTypeMapper INSTANCE = Mappers.getMapper(MovementBaseTypeMapper.class);

    MovementDto movementBaseTypeToMovementDto(MovementBaseType movementBaseType);
}

