package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.CrudServiceBean;
import eu.europa.ec.fisheries.uvms.util.SqlPropertyHolder;

import javax.ejb.*;
import java.util.HashMap;
import java.util.List;

import static com.google.common.collect.Maps.newHashMap;

/**
 * Created by Michal Kopyczok on 21-Aug-15.
 */

@Stateless(name = "postgresRepository")
@Local(value = SpatialRepository.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class SpatialRepositoryBean extends CrudServiceBean implements SpatialRepository {

    private static final String TABLE_NAME = "{tableName}";
    private static final String LAT = "{lat}";
    private static final String LON = "{lon}";
    private static final String CRS = "{crs}";
    private static final String FIND_AREAS_ID_BY_LOCATION = "sql.findAreasId.ByLocation";
    @EJB
    private SqlPropertyHolder sqlPropertyHolder;

    @Override
    public List<Integer> findAreasIdByLocation(Point point, String areaDbTable) {
        String sql = sqlPropertyHolder.getProperty(FIND_AREAS_ID_BY_LOCATION);
        sql = replaceAndEscapeParameters(sql, createParameters(point.getX(), point.getY(), point.getSRID(), areaDbTable));
        return findEntityByNativeQuery(sql);
    }

    private HashMap<String, String> createParameters(double lon, double lat, int crs, String areaDbTable) {
        HashMap<String, String> parameters = newHashMap();
        parameters.put(TABLE_NAME, areaDbTable);
        parameters.put(LON, String.valueOf(lon));
        parameters.put(LAT, String.valueOf(lat));
        parameters.put(CRS, String.valueOf(crs));
        return parameters;
    }

}
