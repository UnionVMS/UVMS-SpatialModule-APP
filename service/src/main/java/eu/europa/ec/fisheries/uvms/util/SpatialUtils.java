package eu.europa.ec.fisheries.uvms.util;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.WKTWriter;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.PointType;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import java.util.List;

/**
 * Created by Michal Kopyczok on 02-Sep-15.
 */
public class SpatialUtils {

    private static final String EPSG = "EPSG:";
    private static final int DEFAULT_CRS = 4326;

    private SpatialUtils() {
    }

    public static Point convertToPointInWGS84(PointType schemaPoint) {
        return convertToPointInWGS84(schemaPoint.getLongitude(), schemaPoint.getLatitude(), defaultIfNull(schemaPoint.getCrs()));
    }

    public static Point convertToPointInWGS84(double lon, double lat, int crs) {
        try {
            GeometryFactory gf = new GeometryFactory();
            Point point = gf.createPoint(new Coordinate(lon, lat));
            if (crs != DEFAULT_CRS) {
                point = transform(crs, point);
            }
            point.setSRID(DEFAULT_CRS);
            return point;
        } catch (FactoryException ex) {
            throw new SpatialServiceException(SpatialServiceErrors.NO_SUCH_CRS_CODE_ERROR, String.valueOf(crs));
        } catch (MismatchedDimensionException | TransformException ex) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR);
        }
    }

    public static Point transform(int crs, Point point) throws FactoryException, TransformException {
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
                return value.toUpperCase();
            }
        });
    }

    public static String convertToWkt(Point point) {
        return new WKTWriter().write(point);
    }

    public static boolean isDefaultCrs(int crs) {
        return crs == DEFAULT_CRS;
    }

}
