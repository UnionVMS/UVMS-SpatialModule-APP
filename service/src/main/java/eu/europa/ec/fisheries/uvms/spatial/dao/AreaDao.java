package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.*;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import eu.europa.ec.fisheries.uvms.spatial.util.SqlPropertyHolder;
import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.hibernate.transform.Transformers;

import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static eu.europa.ec.fisheries.uvms.spatial.util.SpatialUtils.convertToWkt;
import static org.apache.commons.collections.CollectionUtils.isEmpty;
import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;

public class AreaDao {

    private static final String CRS = "crs";
    private static final String WKT = "wktPoint";
    private static final String UNIT = "unit";
    private static final String FIND_AREAS_ID_BY_LOCATION = "sql.findAreasIdByLocation";
    private static final String CLOSEST_AREA_QUERY = "sql.findClosestArea";
    private static final String CLOSEST_LOCATION_QUERY = "sql.findClosestLocation";
    private static final String TABLE_NAME_PLACEHOLDER = "{tableName}";
    private static final String SEARCH_AREA = "sql.searchArea";
    private static final String NAME_PLACEHOLDER = "{name}";
    private static final String CODE_PLACEHOLDER = "{code}";
    private static final String GID = "gid";

    private EntityManager em;

    private SqlPropertyHolder propertyHolder;

    public AreaDao(EntityManager em, SqlPropertyHolder propertyHolder) {
        this.em = em;
        this.propertyHolder = propertyHolder;
    }

    private static Function<AreaIdentifierDto, String> CONVERT_DTO_TO_TYPE = new Function<AreaIdentifierDto, String>() {
        @Override
        public String apply(AreaIdentifierDto area) {
            return area.getAreaType();
        }
    };

    private static Function<AreaIdentifierDto, String> CONVERT_DTO_TO_ID = new Function<AreaIdentifierDto, String>() {
        @Override
        public String apply(AreaIdentifierDto area) {
            return area.getId();
        }
    };

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

    public List findAreaOrLocationByCoordinates(Point point, String nativeQueryString) {
        String wktPoint = convertToWkt(point);
        int crs = point.getSRID();
        return createNamedNativeQuery(nativeQueryString, wktPoint, crs).list();
    }

    @SuppressWarnings("unchecked")
    public List<AreaLayerDto> findSystemAreaLayerMapping() {
        return createQuery(QueryNameConstants.FIND_SYSTEM_AREA_LAYER, AreaLayerDto.class).list();
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, String>> findSelectedAreaColumns(String namedQueryString, Number gid) {
        return createNamedQuery(namedQueryString, gid).list();
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, String>> findAreaByFilter(String areaType, String filter) {
        String queryString = propertyHolder.getProperty(SEARCH_AREA);
        queryString = replaceSearchStrings(queryString, areaType, filter);
        return createSQLQuery(queryString).list();
    }

    @SuppressWarnings("unchecked")
    public Geometry filterAreas(List<AreaIdentifierDto> userAreas, List<AreaIdentifierDto> scopeAreas) {
        String[] userAreaTypesArr = convertToTypeArray(userAreas, CONVERT_DTO_TO_TYPE);
        String[] userAreaIdsArr = convertToTypeArray(userAreas, CONVERT_DTO_TO_ID);
        validateInput(userAreaTypesArr, userAreaIdsArr);

        String userAreaTypesString = convertToTypesString(userAreaTypesArr);
        String userAreaIdsString = convertToIdsString(userAreaIdsArr);

        Query query = getSession().createSQLQuery(
                "CALL spatial.filter_geom(:userAreaTypes, :userAreaIds)")
                .addEntity(Geometry.class)
                .setParameter("userAreaTypes", userAreaTypesString)
                .setParameter("userAreaIds", userAreaIdsString);

        List<Geometry> result = query.list();
        validateResponse(result);
        return result.get(0);
    }

    private String convertToIdsString(String[] userAreaIdsArr) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (String id : userAreaIdsArr) {
            sb.append(id);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length());
        sb.append("}::integer[]");
        return sb.toString();
    }

    private String convertToTypesString(String[] userAreaTypesArr) {
        String userAreaTypesString = Arrays.toString(userAreaTypesArr);
        userAreaTypesString = userAreaTypesString.substring(1, userAreaTypesString.length());
        return userAreaTypesString;
    }

    private void validateResponse(List<Geometry> result) {
        if (isEmpty(result)) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR);
        }
    }

    private void validateInput(String[] userAreaTypesArr, String[] userAreaIdsArr) {
        if (ArrayUtils.isEmpty(userAreaTypesArr) || ArrayUtils.isEmpty(userAreaIdsArr) || userAreaTypesArr.length != userAreaIdsArr.length) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR);
        }
    }

    private String[] convertToTypeArray(List<AreaIdentifierDto> userAreas, Function<AreaIdentifierDto, String> func) {
        List<String> userAreaTypes = Lists.transform(userAreas, func);
        return userAreaTypes.toArray(new String[userAreaTypes.size()]);
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

    private SQLQuery createSQLQuery(String queryString) {
        SQLQuery sqlQuery = getSession().createSQLQuery(queryString);
        sqlQuery.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        return sqlQuery;
    }

    private Query createNamedNativeQuery(String nativeQueryString, String wktPoint, int crs) {
        Query query = getSession().getNamedQuery(nativeQueryString);
        query.setParameter(WKT, wktPoint);
        query.setParameter(CRS, crs);
        return query;
    }

    private Query createNamedQuery(String namedQueryString, Number gid) {
        Query query = getSession().getNamedQuery(namedQueryString);
        query.setParameter(GID, gid);
        query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        return query;
    }

    private SQLQuery createSQLQueryForClosestArea(String queryString, String wktPoint, int crs) {
        SQLQuery sqlQuery = getSession().createSQLQuery(queryString);
        sqlQuery.setString(WKT, wktPoint);
        sqlQuery.setInteger(CRS, crs);
        return sqlQuery;
    }

    private <T> Query createQuery(String nativeQuery, Class<T> dtoClass) {
        Query query = getSession().getNamedQuery(nativeQuery);
        query.setResultTransformer(Transformers.aliasToBean(dtoClass));
        return query;
    }

    private Session getSession() {
        return em.unwrap(Session.class);
    }

    private String replaceTableName(String queryString, String tableName) {
        return queryString.replace(TABLE_NAME_PLACEHOLDER, tableName);
    }

    private String replaceSearchStrings(String queryString, String tableName, String filter) {
        return queryString.replace(TABLE_NAME_PLACEHOLDER, tableName).replace(NAME_PLACEHOLDER, filter).replace(CODE_PLACEHOLDER, filter);
    }
}
