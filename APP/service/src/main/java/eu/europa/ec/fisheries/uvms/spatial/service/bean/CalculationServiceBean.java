package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import eu.europa.ec.fisheries.uvms.spatial.service.CalculateService;
import lombok.extern.slf4j.Slf4j;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.jts.WKTReader2;
import org.geotools.geometry.jts.WKTWriter2;

import javax.ejb.Local;
import javax.ejb.Stateless;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

@Slf4j
@Stateless
@Local(CalculateService.class)
public class CalculationServiceBean implements CalculateService {

    @Override
    public String calculateBuffer(final Double latitude, final Double longitude, final Double buffer) {

        return SpatialUtils.calculateBuffer(latitude, longitude, buffer);

    }

    @Override
    public String transform(final Double tx, final Double ty, final String wkt, Boolean nativeQuery) {

        try {

            Geometry geometry = new WKTReader2().read(wkt);
            com.vividsolutions.jts.geom.Coordinate[] sourceCoordinates = geometry.getCoordinates();
            com.vividsolutions.jts.geom.Coordinate[] targetCoordinates = new com.vividsolutions.jts.geom.Coordinate[]{};
            AffineTransform translate= AffineTransform.getTranslateInstance(tx, ty);

            for (int i= 0; i < sourceCoordinates.length; i++){
                com.vividsolutions.jts.geom.Coordinate sourceCoordinate = sourceCoordinates[i];
                Point2D p = new Point2D.Double(sourceCoordinate.x,sourceCoordinate.y);
                Point2D transform = translate.transform(p, null);
                targetCoordinates[i] = new com.vividsolutions.jts.geom.Coordinate(transform.getX(), transform.getY());
            }

            if (nativeQuery){

            }
            GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

            LinearRing ring = geometryFactory.createLinearRing( targetCoordinates );
            Polygon polygon = geometryFactory.createPolygon(ring, null );
            return new WKTWriter2().write(polygon);


        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;

    }
}
