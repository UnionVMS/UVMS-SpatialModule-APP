package eu.europa.ec.fisheries.uvms.spatial.service.Service2.bean.utils;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.bean.TransactionalTests;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.utils.GeometryUtils;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class GeometryUtilsTest extends TransactionalTests {

    @Test
    public void createPointTest(){
        double lon = 17.969;
        double lat = 58.916666666666664;

        Point point = (Point) GeometryUtils.createPoint(lat, lon);

        assertEquals(lon, point.getX(), 0);
        assertEquals(lat, point.getY(), 0);
        assertEquals(4326, point.getSRID());

    }

    @Test
    public void geometryToWktTest(){
        double lon = 17.969;
        double lat = 58.916666666666664;

        Geometry geo = GeometryUtils.createPoint(lat, lon);
        String wkt = GeometryUtils.geometryToWkt(geo);

        assertEquals("POINT (17.969 58.916666666666664)", wkt);
    }

    @Test
    public void WktToGeometryTest() throws Exception{
        String wkt = "POINT (17.969 58.916666666666664)";

        Point point = (Point) GeometryUtils.wktToGeometry(wkt);

        assertEquals(17.969, point.getX(), 0);
        assertEquals(58.916666666666664, point.getY(), 0);
        assertEquals(4326, point.getSRID(), 0);
    }

    @Test
    public void pointToWktToPointTest() throws Exception{
        double lon = 17.969;
        double lat = 58.916666666666664;

        Point point = (Point) GeometryUtils.createPoint(lat, lon);
        String wkt = GeometryUtils.geometryToWkt(point);
        Point wktPoint = (Point) GeometryUtils.wktToGeometry(wkt);

        assertEquals(point.getX(), wktPoint.getX(), 0);
        assertEquals(point.getY(), wktPoint.getY(), 0);
        assertEquals(point.getSRID(), wktPoint.getSRID(), 0);
    }

    @Test
    public void WktToCentroidWktTest() throws Exception{

        double lon = -162.64657223720369;
        double lat = 2.0032231410359316;
        String wkt = "POLYGON((-164.366562638602 -29.867947809336705,-164.366562638602 30.132052190663302,-159.206591434407 5.745565041781205,-164.366562638602 -29.867947809336705))";

        String centroidWkt = GeometryUtils.wktToCentroidWkt(wkt);
        Point centroidPoint = (Point) GeometryUtils.wktToGeometry(centroidWkt);

        assertEquals(lon, centroidPoint.getX(), 0.0000001);
        assertEquals(lat, centroidPoint.getY(), 0.0000001);

    }



}
