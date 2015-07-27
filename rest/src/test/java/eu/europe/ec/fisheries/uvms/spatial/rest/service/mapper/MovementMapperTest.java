package eu.europe.ec.fisheries.uvms.spatial.rest.service.mapper;

import eu.europa.ec.fisheries.schema.movement.v1.MovementBaseType;
import eu.europa.ec.fisheries.uvms.movement.model.mock.MockData;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MovementMapperTest {

    private MovementBaseType movementBaseType;

    @Before
    public void setup(){
        movementBaseType = MockData.getDtoList(1).get(0);
    }

    @Test
    public void testMovementBaseTypeToMovementDto(){

        MovementDto movementDto = MovementMapper.INSTANCE.movementBaseTypeToMovementDto(movementBaseType);

        assertEquals("ENT", movementDto.getMessageType().value());
        assertNotNull(movementDto.getConnectId());
        assertEquals("0", movementDto.getId());
        assertEquals("God like", movementDto.getStatus());
        assertEquals("3.2", movementDto.getCalculatedSpeed().toString());
        assertEquals(12, movementDto.getCourse());
        assertEquals(0, movementDto.getMeasuredSpeed().intValue());
        assertEquals(12.0, movementDto.getPosition().getLatitude());
        assertEquals(69.0, movementDto.getPosition().getLongitude());
        assertEquals("ABC-80+", movementDto.getMobileTerminal().getId());
        assertEquals(null, movementDto.getPositionTime());

    }
}
