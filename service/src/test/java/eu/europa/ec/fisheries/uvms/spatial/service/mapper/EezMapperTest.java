package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

import eu.europa.ec.fisheries.schema.spatial.types.EezType;
import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by kopyczmi on 06-Aug-15.
 */
public class EezMapperTest {

    private static final String EEZ = "Test EEZ";
    private static final String COUNTRY = "Spain";

    @Test
    public void shouldReturnMappedEez() throws Exception {
        // given
        EezEntity eezEntity = createEezEntity();

        // when
        EezType eezType = EezMapper.INSTANCE.eezEntityToSchema(eezEntity);

        // then
        assertEquals(EEZ, eezType.getName());
        assertEquals(COUNTRY, eezType.getCountry());
    }

    private EezEntity createEezEntity() {
        EezEntity eezEntity = new EezEntity();
        eezEntity.setEez(EEZ);
        eezEntity.setCountry(COUNTRY);
        return eezEntity;
    }

}