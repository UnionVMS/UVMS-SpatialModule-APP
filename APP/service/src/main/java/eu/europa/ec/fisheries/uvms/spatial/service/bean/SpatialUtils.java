package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.PointType;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import lombok.extern.slf4j.Slf4j;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class SpatialUtils {

    private static final String EPSG = "EPSG:";
    private static final int DEFAULT_CRS = 4326;

    private SpatialUtils() {
    }

    static Point convertToPointInWGS84(PointType schemaPoint) {

        Integer crs = DEFAULT_CRS;

        if (schemaPoint.getCrs() != null){
            crs = schemaPoint.getCrs();
        }

        return convertToPointInWGS84(schemaPoint.getLongitude(), schemaPoint.getLatitude(), crs);
    }

    static Geometry translate(Double tx, Double ty, Geometry geometry) {

        AffineTransform translate= AffineTransform.getTranslateInstance(tx, ty);

        Coordinate[] source = geometry.getCoordinates();
        Coordinate[] target = new Coordinate[source.length];

        for (int i= 0; i < source.length; i++){
            Coordinate sourceCoordinate = source[i];
            Point2D p = new Point2D.Double(sourceCoordinate.x,sourceCoordinate.y);
            Point2D transform = translate.transform(p, null);
            target[i] = new Coordinate(transform.getX(), transform.getY());
        }

        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

        Geometry targetGeometry;

        if (geometry instanceof Point){
            targetGeometry = geometryFactory.createPoint(target[0]);
        }

        else if (geometry instanceof Polygon){
           targetGeometry = geometryFactory.createPolygon(target);
        }

        else {
            throw new UnsupportedOperationException("Geometry type not supported");
        }

        return targetGeometry;

    }

    static Point convertToPointInWGS84(double lon, double lat, int crs) {
        try {

            GeometryFactory gf = new GeometryFactory();
            Point point = gf.createPoint(new Coordinate(lon, lat));

            if (!isDefaultCrs(crs)) {

                CoordinateReferenceSystem inputCrs = CRS.decode(EPSG + crs);
                MathTransform mathTransform = CRS.findMathTransform(inputCrs, DefaultGeographicCRS.WGS84, false);
                point = (Point) JTS.transform(point, mathTransform);

            }

            point.setSRID(DEFAULT_CRS);
            return point;
        } catch (FactoryException ex) {
            throw new SpatialServiceException(SpatialServiceErrors.NO_SUCH_CRS_CODE_ERROR, String.valueOf(crs), ex);
        } catch (MismatchedDimensionException | TransformException ex) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR, ex);
        }
    }

    public static boolean isDefaultCrs(int crs) {
        return crs == DEFAULT_CRS;
    }

}
