package eu.europa.ec.fisheries.uvms.util;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
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

import static eu.europa.ec.fisheries.uvms.common.SpatialUtils.DEFAULT_CRS;

/**
 * Created by Michal Kopyczok on 02-Sep-15.
 */
public class SpatialUtils {

    private static final String EPSG = "EPSG:";

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

    public static  Point transform(int crs, Point point) throws FactoryException, TransformException {
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
}
