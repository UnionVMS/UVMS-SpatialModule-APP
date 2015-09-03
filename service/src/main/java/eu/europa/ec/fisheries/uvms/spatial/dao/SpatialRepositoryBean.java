package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.service.CrudService;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaEntry;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.ClosestAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.MeasurementUnit;
import eu.europa.ec.fisheries.uvms.util.SqlPropertyHolder;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

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
    private static final String UNIT = "unit";
    private static final String FIND_AREAS_ID_BY_LOCATION = "sql.findAreasIdByLocation";
    private static final String CLOSEST_AREA_QUERY = "sql.findClosestArea";
    private static final String TABLE_NAME_PLACEHOLDER = "{tableName}";

    @EJB
    private SqlPropertyHolder sqlPropertyHolder;

    @EJB
    private CrudService crudService;

    @Override
    public List<Integer> findAreasIdByLocation(Point point, String areaDbTable) {
        String queryString = sqlPropertyHolder.getProperty(FIND_AREAS_ID_BY_LOCATION);
        return executeAreasByLocation(queryString, point, areaDbTable);
    }

    @Override
    public List<ClosestAreaDto> findClosestAreas(Point point, MeasurementUnit unit, String areaDbTable) {
        String queryString = sqlPropertyHolder.getProperty(CLOSEST_AREA_QUERY);
        return executeClosestAreas(queryString, point, unit, areaDbTable);
    }

    private List<Integer> executeAreasByLocation(String queryString, Point point, String areaDbTable) {
        queryString = replaceTableName(queryString, areaDbTable);
        String wktPoint = convertToWkt(point);
        int crs = point.getSRID();

        return createSQLQuery(queryString, wktPoint, crs).list();
    }

    private List<ClosestAreaDto> executeClosestAreas(String queryString, Point point, MeasurementUnit unit, String areaDbTable) {
        queryString = replaceTableName(queryString, areaDbTable);
        String wktPoint = convertToWkt(point);
        int crs = point.getSRID();
        double unitRatio = unit.getRatio();

        return createSQLQuery(queryString, wktPoint, crs, unitRatio).list();
    }

    private SQLQuery createSQLQuery(String queryString, String wktPoint, int crs, double unit) {
        SQLQuery sqlQuery = getSession().createSQLQuery(queryString);
        sqlQuery.setResultTransformer(Transformers.aliasToBean(ClosestAreaDto.class));
        sqlQuery.setString(WKT, wktPoint);
        sqlQuery.setInteger(CRS, crs);
        sqlQuery.setDouble(UNIT, unit);
        return sqlQuery;
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
