package eu.europa.ec.fisheries.uvms.spatial.client;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialModuleMethod;
import eu.europa.ec.fisheries.uvms.spatial.service.utils.GeometryUtils;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;
import org.locationtech.jts.io.ParseException;

import javax.inject.Inject;
import javax.naming.InitialContext;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class SpatialRestClientTest extends BuildSpatialRestClientDeployment {

    @Inject
    private SpatialRestClient client;

    @Before
    public void setUp() throws Exception {
        InitialContext initialContext = new InitialContext();
        initialContext.rebind("java:global/spatial_endpoint", "http://localhost:8080/spatial");
    }

    @Test
    public void getGeometryByPortCode() throws ParseException {
        // When
        String geometry = client.getGeometryByPortCode("ESVGO", SpatialModuleMethod.GET_GEOMETRY_BY_PORT_CODE);

        // Then
        Coordinate coordinate = new Coordinate(-8.718, 42.242);
        CoordinateArraySequence sequence = new CoordinateArraySequence(new Coordinate[]{coordinate});
        GeometryFactory geometryFactory = new GeometryFactory();
        Point point = new Point(sequence, geometryFactory);
        MultiPoint multiPoint = new MultiPoint(new Point[]{point}, geometryFactory);

        MultiPoint responseMultipoint = (MultiPoint) GeometryUtils.wktToGeometry(geometry);
        assertEquals(multiPoint, responseMultipoint);
    }
}
