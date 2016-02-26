package eu.europa.ec.fisheries.uvms.spatial.dao;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.GeometryType;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.ServiceLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.GeometryMapper;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.AreaExtendedIdentifierDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.AreaLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.ClosestAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.ClosestLocationDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.FilterAreasDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.util.MeasurementUnit;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import eu.europa.ec.fisheries.uvms.spatial.util.SqlPropertyHolder;

public class AreaDao extends CommonDao {

    private static final String FIND_AREAS_ID_BY_LOCATION = "sql.findAreasIdByLocation";
    private static final String CLOSEST_AREA_QUERY = "sql.findClosestArea";
    private static final String CLOSEST_LOCATION_QUERY = "sql.findClosestLocation";
    private static final String FILTER_AREAS_QUERY = "sql.filterAreas";
    private static final String TABLE_NAME_PLACEHOLDER = "{tableName}";
    private static final String SEARCH_AREA = "sql.searchArea";
    private static final String NAME_PLACEHOLDER = "{name}";
    private static final String CODE_PLACEHOLDER = "{code}";
    private static final String USER_AREA_TABLES = "userAreaTypes";
    private static final String USER_AREA_IDS = "userAreaIds";
    private static final String SCOPE_AREA_TABLES = "scopeAreaTypes";
    private static final String SCOPE_AREA_IDS = "scopeAreaIds";
    private static final String SUB_TYPE = "subTypes";

    private SqlPropertyHolder propertyHolder;

    public AreaDao(EntityManager em, SqlPropertyHolder propertyHolder) {
    	super(em);
        this.propertyHolder = propertyHolder;
    }

    public List<AreaExtendedIdentifierDto> findAreasIdByLocation(Point point, String areaDbTable) {
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
        GeometryType geometryType = GeometryMapper.INSTANCE.geometryToWKT(point);
        int crs = point.getSRID();
        return createNamedNativeQuery(nativeQueryString, geometryType.getGeometry(), crs).list();
    }

    @SuppressWarnings("unchecked")
    public List<AreaLayerDto> findSystemAreaLayerMapping() {
        return createNamedQuery(QueryNameConstants.FIND_SYSTEM_AREA_LAYER, AreaLayerDto.class).list();
    }

    @SuppressWarnings("unchecked")
    public List<AreaLayerDto> findSystemAreaAndLocationLayerMapping() {
        return createNamedQuery(QueryNameConstants.FIND_SYSTEM_AREA_AND_LOCATION_LAYER, AreaLayerDto.class).list();
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
    public FilterAreasDto filterAreas(List<String> userAreaTables, List<String> userAreaIds, List<String> scopeAreaTables, List<String> scopeAreaIds) {
        validateInput(userAreaTables, userAreaIds, scopeAreaTables, scopeAreaIds);

        String userAreaTablesString = convertToString(userAreaTables);
        String userAreaIdsString = convertToString(userAreaIds);

        String scopeAreaTablesString = convertToString(scopeAreaTables);
        String scopeAreaIdsString = convertToString(scopeAreaIds);

        String queryString = propertyHolder.getProperty(FILTER_AREAS_QUERY);
        Query query = getSession().createSQLQuery(queryString)
                .setParameter(USER_AREA_TABLES, userAreaTablesString)
                .setParameter(USER_AREA_IDS, userAreaIdsString)
                .setParameter(SCOPE_AREA_TABLES, scopeAreaTablesString)
                .setParameter(SCOPE_AREA_IDS, scopeAreaIdsString)
                .setResultTransformer(Transformers.aliasToBean(FilterAreasDto.class));

        return (FilterAreasDto) query.list().get(0);
    }

    public List<ServiceLayerDto> findServiceLayerBySubType(List<String> subAreaTypes, boolean isWithBing) {
        if (isWithBing) {
            return createNamedQueryWithParameterList(QueryNameConstants.FIND_SERVICE_LAYER_BY_SUBTYPE, SUB_TYPE, subAreaTypes, ServiceLayerDto.class).list();
        } else {
            return createNamedQueryWithParameterList(QueryNameConstants.FIND_SERVICE_LAYER_BY_SUBTYPE_WITHOUT_BING, SUB_TYPE, subAreaTypes, ServiceLayerDto.class).list();
        }

    }

    private void validateInput(List<String> userAreaTables, List<String> userAreaIds, List<String> scopeAreaTables, List<String> scopeAreaIds) {
        if ((userAreaTables.size() != userAreaIds.size()) || (scopeAreaTables.size() != scopeAreaIds.size())) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR);
        }
    }

    private String convertToString(List<String> userAreaTypes) {
        String[] userAreaTypesArr = userAreaTypes.toArray(new String[userAreaTypes.size()]);
        String userAreaTypesString = Arrays.toString(userAreaTypesArr);
        userAreaTypesString = userAreaTypesString.substring(1, userAreaTypesString.length() - 1);
        return userAreaTypesString;
    }

    @SuppressWarnings("unchecked")
    private List<AreaExtendedIdentifierDto> executeAreasByLocation(String queryString, Point point, String areaDbTable) {
        queryString = replaceTableName(queryString, areaDbTable);
        GeometryType geometryType = GeometryMapper.INSTANCE.geometryToWKT(point);
        int crs = point.getSRID();

        return createSQLQueryForClosestArea(queryString, geometryType.getGeometry(), crs).list();
    }

    private List executeClosest(String queryString, Point point, MeasurementUnit unit, String areaDbTable, Class resultClass) {
        queryString = replaceTableName(queryString, areaDbTable);
        GeometryType geometryType = GeometryMapper.INSTANCE.geometryToWKT(point);
        int crs = point.getSRID();
        double unitRatio = unit.getRatio();
        return createSQLQuery(queryString, geometryType.getGeometry(), crs, unitRatio, resultClass).list();
    }

    private String replaceTableName(String queryString, String tableName) {
        return queryString.replace(TABLE_NAME_PLACEHOLDER, tableName);
    }

    private String replaceSearchStrings(String queryString, String tableName, String filter) {
        return queryString.replace(TABLE_NAME_PLACEHOLDER, tableName).replace(NAME_PLACEHOLDER, filter).replace(CODE_PLACEHOLDER, filter);
    }
}
