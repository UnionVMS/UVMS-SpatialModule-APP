package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.io.ParseException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.PointType;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.jts.WKTWriter2;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.List;

@Slf4j
public class SpatialUtils {

    private static final String EPSG = "EPSG:";
    private static final int DEFAULT_CRS = 4326;

    private SpatialUtils() {
    }

    static Point convertToPointInWGS84(PointType schemaPoint) {
        return convertToPointInWGS84(schemaPoint.getLongitude(), schemaPoint.getLatitude(), defaultIfNull(schemaPoint.getCrs()));
    }

    static String calculateBuffer(final Double latitude, final Double longitude, final Double buffer) {
        Geometry geometry = SpatialUtils.getPoint(longitude, latitude).buffer(buffer);
        return new WKTWriter2().write(geometry);
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
            Point point = getPoint(lon, lat);
            if (!isDefaultCrs(crs)) {
                point = transform(crs, point);
            }
            point.setSRID(DEFAULT_CRS);
            return point;
        } catch (FactoryException ex) {
            log.error("Exception while conversion to point", ex);
            throw new SpatialServiceException(SpatialServiceErrors.NO_SUCH_CRS_CODE_ERROR, String.valueOf(crs), ex);
        } catch (MismatchedDimensionException | TransformException ex) {
            log.error("Exception while transformation to point", ex);
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR, ex);
        }
    }

    static Point getPoint(double lon, double lat) {

        try {
            GeometryFactory gf = new GeometryFactory();
            return gf.createPoint(new Coordinate(lon, lat));
        }
        catch (Exception ex){
            log.error("Exception while conversion to point", ex);
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR, ex);
        }
    }

    static Point transform(int crs, Point point) throws FactoryException, TransformException {

        CoordinateReferenceSystem inputCrs = CRS.decode(EPSG + crs);
        MathTransform mathTransform = CRS.findMathTransform(inputCrs, DefaultGeographicCRS.WGS84, false);
        point = (Point) JTS.transform(point, mathTransform);
        return point;
    }

    public static Integer defaultIfNull(Integer crs) {
        if (crs == null) {
            return DEFAULT_CRS;
        }
        return crs;
    }

    public static List<String> toUpperCase(List<String> textValue) {
        return Lists.transform(textValue, new Function<String, String>() {
            @Override
            public String apply(String value) {
                if (value != null) {
                    return value.toUpperCase();
                }
                return StringUtils.EMPTY;
            }
        });
    }

    public static boolean isDefaultCrs(int crs) {
        return crs == DEFAULT_CRS;
    }

}
