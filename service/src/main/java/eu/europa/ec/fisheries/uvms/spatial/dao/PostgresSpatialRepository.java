package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.WKTWriter;
import eu.europa.ec.fisheries.uvms.service.exception.CommonGenericDAOException;
import lombok.SneakyThrows;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.List;

/**
 * Created by Michal Kopyczok on 21-Aug-15.
 */

@Stateless(name = "postgresRepository")
@Local(value = SpatialRepository.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PostgresSpatialRepository extends CommonGenericDAOBean implements SpatialRepository {

    private static final String SELECT_GID_FROM = "SELECT gid FROM ";
    private static final String SCHEMA_NAME = "spatial";
    private static final String SEPARATOR = ".";
    private static final String SRID = "SRID=";
    private static final String ST_GEOM_FROM_EWKT = "st_geomfromewkt";

    @Override
    @SneakyThrows(CommonGenericDAOException.class)
    public List<Integer> findAreaIdByLocation(double lat, double lon, int crs, String areaDbTable) {
        String nativeQuery = SELECT_GID_FROM + getTableName(areaDbTable) + getWhereCondition(lat, lon, crs);
        return findEntityByNativeQuery(nativeQuery);
    }

    private String getWhereCondition(double lat, double lon, int crs) {
        return " where " + "st_intersects" + "(geom, " + getStGeomFromWkt(lat, lon, crs) + ")";
    }

    private String getStGeomFromWkt(double lat, double lon, int crs) {
        return ST_GEOM_FROM_EWKT + createEWKTPoint(lat, lon, crs);
    }

    private String getTableName(String areaDbTable) {
        return SCHEMA_NAME + SEPARATOR + areaDbTable;
    }

    private String createEWKTPoint(double lat, double lon, int crs) {
        return "('" + createSrid(crs) + ";" + createWKTPoint(lat, lon) + "')";
    }

    private String createSrid(int crs) {
        return SRID + crs;
    }

    private String createWKTPoint(double lat, double lon) {
        GeometryFactory gf = new GeometryFactory();
        Coordinate coord = new Coordinate(lon, lat);
        Point point = gf.createPoint(coord);
        point.setSRID(3245);
        return new WKTWriter(2).write(point);
    }

}
