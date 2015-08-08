package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static junit.framework.TestCase.fail;

public class AreaResultTypeMapperTest {

    AreaResultTypeMapper areaResultTypeMapper;

    @Before
    public void beforeTest(){
        areaResultTypeMapper = new AreaResultTypeMapperImpl();
        try {
            Field field = AreaResultTypeMapperImpl.class.getDeclaredField("geometryMapper");
            field.setAccessible(true);
            GeometryMapperImpl geometryMapper = new GeometryMapperImpl();
            field.set(areaResultTypeMapper, geometryMapper);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to initialize areaResultTypeMapper");
        }

    }

    @Test
    public void test() {

        // TODO test more
        EezEntity eezEntity = new EezEntity();
        eezEntity.setEez("dkljdlkjd");
        eezEntity.setGeom(new GeometryFactory().createPoint(new Coordinate(1,1)));

        areaResultTypeMapper.eezEntityToAreaResultType(eezEntity);

    }
}
