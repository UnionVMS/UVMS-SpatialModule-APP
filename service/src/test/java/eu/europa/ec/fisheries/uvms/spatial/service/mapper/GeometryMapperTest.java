package eu.europa.ec.fisheries.uvms.spatial.service.mapper;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.GeometryType;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class GeometryMapperTest {

    private GeometryMapper mapper;
    private Geometry geometry;

    @Before
    public void beforeTest() {
        mapper = new GeometryMapperImpl();
    }

    @Test
    public void shouldReturnGeometryTypeWithTypePoint() {
        geometry = new GeometryFactory().createPoint(new Coordinate(1, 11));
        GeometryType geometryType = mapper.geometryToWKT(geometry);
        assertEquals("POINT (1 11)", geometryType.getGeometry());
    }

    @Test
    public void shouldReturnGeometryTypeWithTypePolygon() {
        List<Coordinate> coordinates = Arrays.asList(new Coordinate(1, 11), new Coordinate(1, 11), new Coordinate(1, 11), new Coordinate(1, 11));
        Polygon polygon = new GeometryFactory().createPolygon((Coordinate[]) coordinates.toArray());
        GeometryType geometryType = mapper.geometryToWKT(polygon);
        assertEquals("POLYGON ((1 11, 1 11, 1 11, 1 11))", geometryType.getGeometry());
    }
}