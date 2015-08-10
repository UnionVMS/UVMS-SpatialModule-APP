package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import eu.europa.ec.fisheries.schema.spatial.types.AreaResultType;
import eu.europa.ec.fisheries.schema.spatial.types.PropertyType;
import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.TestCase.fail;

public class AreaResultTypeMapperTest {

    private AreaResultTypeMapper areaResultTypeMapper;

    private EezEntity entity;

    @Before
    public void beforeTest() {
        areaResultTypeMapper = new AreaResultTypeMapperImpl();
        entity = buildEntity();
        try {
            Field field = AreaResultTypeMapperImpl.class.getDeclaredField("geometryTypeMapper");
            field.setAccessible(true);
            GeometryTypeMapper geometryMapper = new GeometryTypeMapperImpl();
            field.set(areaResultTypeMapper, geometryMapper);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to initialize areaResultTypeMapper");
        }
    }

    @Test
    public void shouldReturnAreaResultType() {

        // when
        AreaResultType areaResultType = areaResultTypeMapper.eezEntityToAreaResultType(entity);

        // then
        assertEquals("Feature", areaResultType.getType());
        assertEquals("POINT (1 11)", areaResultType.getGeometry().getCoordinates());
        List<PropertyType.Entry> entry = areaResultType.getProperties().getEntry();
        assertEquals("longitude", entry.get(0).getKey());
        assertEquals("1.0", entry.get(0).getValue());
        assertEquals("latitude", entry.get(1).getKey());
        assertEquals("2.0", entry.get(1).getValue());
        assertEquals("country", entry.get(2).getKey());
        assertEquals("lalaland", entry.get(2).getValue());
        assertEquals("sovereign", entry.get(3).getKey());
        assertEquals("english", entry.get(3).getValue());
        assertEquals("remarks", entry.get(4).getKey());
        assertEquals("none", entry.get(4).getValue());
        assertEquals("iso3Digit", entry.get(5).getKey());
        assertEquals("AFG", entry.get(5).getValue());
        assertEquals("dateChang", entry.get(6).getKey());
        assertEquals("yesterday", entry.get(6).getValue());
        assertEquals("areaM2", entry.get(7).getKey());
        assertEquals("1.0", entry.get(7).getValue());
    }

    @Test
    public void shouldReturnNull() {
        // given
        entity = null;

        // when
        AreaResultType areaResultType = areaResultTypeMapper.eezEntityToAreaResultType(entity);

        // then
        assertNull(areaResultType);
    }

    @Test
    public void shouldReturnEmptyAreaResultType() {
        // given
        entity = new EezEntity();

        // when
        AreaResultType areaResultType = areaResultTypeMapper.eezEntityToAreaResultType(entity);

        // then
        assertEquals("Feature", areaResultType.getType());
        assertEquals(null, areaResultType.getGeometry());
        assertEquals(0, areaResultType.getProperties().getEntry().size());
    }

    private EezEntity buildEntity() {
        EezEntity entity = new EezEntity();
        entity.setEez("eez");
        entity.setSovereign("english");
        entity.setAreaM2((double) 1);
        entity.setCountry("lalaland");
        entity.setDateChang("yesterday");
        entity.setEezId(1);
        entity.setGid(1);
        entity.setIso3Digit("AFG");
        entity.setLatitude((double) 2);
        entity.setLongitude((double) 1);
        entity.setMrgid(BigInteger.valueOf(1));
        entity.setMrgidEez(1);
        entity.setRemarks("none");
        entity.setSovId(1);
        entity.setGeom(new GeometryFactory().createPoint(new Coordinate(1, 11)));
        return entity;
    }

}
