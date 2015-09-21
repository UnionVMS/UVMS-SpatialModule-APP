package eu.europa.ec.fisheries.uvms.spatial.dao;

import static eu.europa.ec.fisheries.uvms.spatial.util.SpatialUtils.convertToWkt;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import com.vividsolutions.jts.geom.Point;

import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.ClosestAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.ClosestLocationDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.MeasurementUnit;
import eu.europa.ec.fisheries.uvms.spatial.util.SqlPropertyHolder;

public class AreaDao {

    private static final String CRS = "crs";
    private static final String WKT = "wktPoint";
    private static final String UNIT = "unit";
    private static final String FIND_AREAS_ID_BY_LOCATION = "sql.findAreasIdByLocation";
    private static final String CLOSEST_AREA_QUERY = "sql.findClosestArea";
    private static final String CLOSEST_LOCATION_QUERY = "sql.findClosestLocation";
    private static final String FIND_AREA_BY_COORDINATE = "sql.findAreaByCoordinate";
    private static final String TABLE_NAME_PLACEHOLDER = "{tableName}";

    private SqlPropertyHolder propertyHolder;
    private EntityManager em;

    public AreaDao(EntityManager em, SqlPropertyHolder propertyHolder) {
        this.em = em;
        this.propertyHolder = propertyHolder;
    }

    public List<Integer> findAreasIdByLocation(Point point, String areaDbTable) {
        String queryString = propertyHolder.getProperty(FIND_AREAS_ID_BY_LOCATION);
        return executeAreasByLocation(queryString, point, areaDbTable);
    }

    @SuppressWarnings("unchecked")
    public List<ClosestAreaDto> findClosestArea(Point point, MeasurementUnit unit, String areaDbTable) {
        String queryString = propertyHolder.getProperty(CLOSEST_AREA_QUERY);
        return executeClosest(queryString, point, unit, areaDbTable, ClosestAreaDto.class);
    }
    
    @SuppressWarnings("unchecked")
    public List<ClosestLocationDto> findClosestlocation(Point point, MeasurementUnit unit, String areaDbTable) {
        String queryString = propertyHolder.getProperty(CLOSEST_LOCATION_QUERY);
        return executeClosest(queryString, point, unit, areaDbTable, ClosestLocationDto.class);
    }
    
    public List findAreaByCoordinates(Point point, String nativeQueryString) {
    	String wktPoint = convertToWkt(point);
		int crs = point.getSRID();
		return createNamedNativeQuery(nativeQueryString, wktPoint, crs).list();
    }

    @SuppressWarnings("unchecked")
    private List<Integer> executeAreasByLocation(String queryString, Point point, String areaDbTable) {
        queryString = replaceTableName(queryString, areaDbTable);
        String wktPoint = convertToWkt(point);
        int crs = point.getSRID();

        return createSQLQueryForClosestArea(queryString, wktPoint, crs).list();
    }

    private List executeClosest(String queryString, Point point, MeasurementUnit unit, String areaDbTable, Class resultClass) {
        queryString = replaceTableName(queryString, areaDbTable);
        String wktPoint = convertToWkt(point);
        int crs = point.getSRID();
        double unitRatio = unit.getRatio();
        return createSQLQuery(queryString, wktPoint, crs, unitRatio, resultClass).list();
    }

    private SQLQuery createSQLQuery(String queryString, String wktPoint, int crs, double unit, Class resultClass) {
        SQLQuery sqlQuery = getSession().createSQLQuery(queryString);
        sqlQuery.setResultTransformer(Transformers.aliasToBean(resultClass));
        sqlQuery.setString(WKT, wktPoint);
        sqlQuery.setInteger(CRS, crs);
        sqlQuery.setDouble(UNIT, unit);
        return sqlQuery;
    }
    
    private Query createNamedNativeQuery(String nativeQueryString, String wktPoint, int crs) {
        Query query = getSession().getNamedQuery(nativeQueryString);
        query.setParameter(WKT, wktPoint);
        query.setParameter(CRS, crs);
        return query;
    }

    private SQLQuery createSQLQueryForClosestArea(String queryString, String wktPoint, int crs) {
        SQLQuery sqlQuery = getSession().createSQLQuery(queryString);
        sqlQuery.setString(WKT, wktPoint);
        sqlQuery.setInteger(CRS, crs);
        return sqlQuery;
    }

    private Session getSession() {
        return em.unwrap(Session.class);
    }

    private String replaceTableName(String queryString, String tableName) {
        return queryString.replace(TABLE_NAME_PLACEHOLDER, tableName);
    }
}
