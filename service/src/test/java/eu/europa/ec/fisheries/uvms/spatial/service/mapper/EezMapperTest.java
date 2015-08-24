package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezType;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;

public class EezMapperTest {

    private EezTypeMapper mapper;
    private EezEntity eez;

    @Before
    public void beforeTest() {
        mapper = new EezTypeMapperImpl();

        try {
            Field field = EezTypeMapperImpl.class.getDeclaredField("geometryTypeMapper");
            field.setAccessible(true);
            GeometryTypeMapper geometryMapper = new GeometryTypeMapperImpl();
            field.set(mapper, geometryMapper);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to initialize areaResultTypeMapper");
        }
    }

    @Test
    public void shouldReturnEezType() {
        // given
        eez = createEez();

        // when
        EezType eezType = mapper.eezEntityToEezType(eez);

        // then
        assertEquals(eezType.getRemarks(), eez.getRemarks());
        assertEquals(eezType.getEez(), eez.getEez());
        assertEquals(eezType.getCountry(), eez.getCountry());
        assertEquals(eezType.getGeometry().getGeometry(), "POINT (1 11)");

    }

    private EezEntity createEez() {
        eez = new EezEntity();
        eez.setRemarks("remarks");
        eez.setEez("eez");
        eez.setCountry("country");
        eez.setGeom(new GeometryFactory().createPoint(new Coordinate(1, 11)));
        return eez;
    }
}
