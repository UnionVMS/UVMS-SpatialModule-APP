package eu.europa.ec.fisheries.uvms.spatial.dao;

import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import eu.europa.ec.fisheries.uvms.service.QueryParameter;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.GeometryType;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.ServiceLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.GeometryMapper;
import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.AreaLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.ClosestAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.util.MeasurementUnit;

public class AreaDao extends CommonDao {

    private static final String TABLE_NAME_PLACEHOLDER = "{tableName}";
    private static final String SUB_TYPE = "subTypes";

    public AreaDao(EntityManager em) {
    	super(em);
    }

    public List<ClosestAreaDto> findClosestArea(Point point, MeasurementUnit unit, String areaDbTable) {

        // FIXME native query alert
        String q = "WITH prox_query AS (SELECT CAST(gid AS text), code, name, st_closestpoint(geom, st_geomfromtext(CAST(:wktPoint as text), :crs)) as closestPoint FROM spatial.{tableName} where not ST_IsEmpty(geom) and enabled = 'Y' ORDER BY geom <#> st_geomfromtext(CAST(:wktPoint as text), :crs) limit 30) SELECT gid AS id, code, name, st_length_spheroid(st_makeline(closestPoint, st_geomfromtext(CAST(:wktPoint as text), :crs)), 'SPHEROID[\"WGS 84\",6378137,298.257223563]') /:unit AS distance FROM prox_query ORDER BY distance LIMIT 1";

        String queryString = q.replace(TABLE_NAME_PLACEHOLDER, areaDbTable);
        GeometryType geometryType = GeometryMapper.INSTANCE.geometryToWKT(point);
        int crs = point.getSRID();
        double unitRatio = unit.getRatio();

        return createSQLQuery(queryString, geometryType.getGeometry(), crs, unitRatio, ClosestAreaDto.class).list();

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

    public List<ServiceLayerDto> findServiceLayerBySubType(List<String> subAreaTypes, boolean isWithBing) {
        if (isWithBing) {
            return createNamedQueryWithParameterList(QueryNameConstants.FIND_SERVICE_LAYER_BY_SUBTYPE, SUB_TYPE, subAreaTypes, ServiceLayerDto.class).list();
        } else {
            return createNamedQueryWithParameterList(QueryNameConstants.FIND_SERVICE_LAYER_BY_SUBTYPE_WITHOUT_BING, SUB_TYPE, subAreaTypes, ServiceLayerDto.class).list();
        }

    }

    public List<String> listAreaGroups(String userName, String scopeName, boolean isPowerUser) {
        QueryParameter params =  QueryParameter.with("userName", userName).and("scopeName", scopeName).and("isPowerUser", isPowerUser);

        return createNamedQuery(QueryNameConstants.FIND_USER_AREA_TYPES, params.parameters()).list();
    }
}
