package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import eu.europa.ec.fisheries.schema.spatial.types.GeometryType;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class GeometryTypeMapperTest {

    private GeometryTypeMapper geometryMapper;
    private Geometry geometry;

    @Before
    public void beforeTest() {
        geometryMapper = new GeometryTypeMapperImpl();
    }

    @Test
    public void shouldReturnGeometryTypeWithTypePoint() {

        // given
        geometry = new GeometryFactory().createPoint(new Coordinate(1,11));

        // when
        GeometryType geometryType = geometryMapper.geometryToGeometryType(geometry);

        // then
        assertEquals("Point", geometryType.getType());
        assertEquals("POINT (1 11)", geometryType.getCoordinates());
    }

    @Test
    public void shouldReturnGeometryTypeWithTypePolygon() {

        // given
        List<Coordinate> coordinates = Arrays.asList(

                new Coordinate(1, 11), new Coordinate(1, 11), new Coordinate(1, 11), new Coordinate(1, 11));
        Polygon polygon = new GeometryFactory().createPolygon((Coordinate[]) coordinates.toArray());

        // when
        GeometryType geometryType = geometryMapper.geometryToGeometryType(polygon);

        // then
        assertEquals("Polygon", geometryType.getType());
        assertEquals("POLYGON ((1 11, 1 11, 1 11, 1 11))", geometryType.getCoordinates());
    }
}