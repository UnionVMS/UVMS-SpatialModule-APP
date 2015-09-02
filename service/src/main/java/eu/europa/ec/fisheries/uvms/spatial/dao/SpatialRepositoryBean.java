package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.WKTWriter;
import eu.europa.ec.fisheries.uvms.service.CrudService;
import eu.europa.ec.fisheries.uvms.util.SpatialUtils;
import eu.europa.ec.fisheries.uvms.util.SqlPropertyHolder;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import javax.ejb.*;
import java.util.List;

import static eu.europa.ec.fisheries.uvms.util.SpatialUtils.convertToWkt;

/**
 * Created by Michal Kopyczok on 21-Aug-15.
 */

@Stateless
@Local(value = SpatialRepository.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class SpatialRepositoryBean implements SpatialRepository {

    private static final String CRS = "crs";
    private static final String WKT = "wktPoint";
    private static final String FIND_AREAS_ID_BY_LOCATION = "sql.findAreasIdByLocation";
    private static final String TABLE_NAME_PLACEHOLDER = "{tableName}";

    @EJB
    private SqlPropertyHolder sqlPropertyHolder;

    @EJB
    private CrudService crudService;

    @Override
    public List<Integer> findAreasIdByLocation(Point point, String areaDbTable) {
        String queryString = sqlPropertyHolder.getProperty(FIND_AREAS_ID_BY_LOCATION);

        queryString = replaceTableName(queryString, areaDbTable);
        String wktPoint = convertToWkt(point);
        int sRid = point.getSRID();

        List<Integer> result = createSQLQuery(queryString, wktPoint, sRid).list();
        return result;
    }

    private SQLQuery createSQLQuery(String queryString, String wktPoint, int crs) {
        SQLQuery sqlQuery = getSession().createSQLQuery(queryString);
        sqlQuery.setString(WKT, wktPoint);
        sqlQuery.setInteger(CRS, crs);
        return sqlQuery;
    }

    private String replaceTableName(String queryString, String tableName) {
        return queryString.replace(TABLE_NAME_PLACEHOLDER, tableName);
    }

    private Session getSession() {
        return crudService.getEntityManager().unwrap(Session.class);
    }

}
