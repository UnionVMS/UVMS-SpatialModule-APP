package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.google.common.collect.Maps;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.WKTWriter;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    //@SneakyThrows(CommonGenericDAOException.class)
    public List<Integer> findAreasIdByLocation(double lat, double lon, int crs, String areaDbTable) {
//        String nativeQuery = SELECT_GID_FROM + getTableName(areaDbTable) + getWhereCondition(lat, lon, crs);
//        return findEntityByNativeQuery(nativeQuery);

        HashMap<String, String> parameters = Maps.newHashMap();
        parameters.put("tableName", areaDbTable);
        parameters.put("lat", String.valueOf(lat));
        parameters.put("lon", String.valueOf(lon));
        parameters.put("crs", String.valueOf(crs));
        return findEntityByNamedQuery2("findAreasIdByLocation", parameters);
    }

    private List<Integer> findEntityByNamedQuery2(String queryName, HashMap<String, String> parameters) {
        Query e = getEntityManager().createNamedQuery(queryName);
        Iterator i$ = parameters.entrySet().iterator();

        while (i$.hasNext()) {
            Map.Entry entry = (Map.Entry) i$.next();
            e.setParameter((String) entry.getKey(), entry.getValue());
        }

        List objectList = e.getResultList();
        return objectList;
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
