package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.common.SpatialUtils;
import eu.europa.ec.fisheries.uvms.service.CrudService;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaEntry;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialRequest;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialResponse;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UnitType;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.handler.SpatialExceptionHandler;
import eu.europa.ec.fisheries.uvms.util.ModelUtils;
import eu.europa.ec.fisheries.uvms.util.SqlPropertyHolder;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
@Local(AreaService.class)
@Transactional
@Slf4j
public class AreaServiceBean implements AreaService {

    @Inject
    private CrudService crudService;

    private final static String TABLE_NAME_PLACEHOLDER = "{tableName}";
    private final static String CLOSEST_AREA_QUERY = "sql.closestArea";
    private final static String CLOSEST_AREA_QUERY_WITH_TRANSFORM = "sql.closestAreaWithTransform";
    private final static String OTHER_CRS = "otherCrs";
    private final static String DEFAULT_CRS = "defaultCrs";
    private final static String WKT = "wkt";
    private final static String UNIT = "unit";

    @EJB
    private SqlPropertyHolder prop;

    @Override
    @SpatialExceptionHandler(responseType = ClosestAreaSpatialResponse.class)
    public ClosestAreaSpatialResponse getClosestArea(final ClosestAreaSpatialRequest request) {

        ClosestAreaSpatialResponse response = new ClosestAreaSpatialResponse();
        response.setMessageType(ModelUtils.createSuccessResponseMessage());
        List<AreaType> areaTypes = request.getArea();
        Map<String, String> areaMap = getAreaMap();
        String queryString;
        if (SpatialUtils.isDefaultCrs(request.getCrs())) {
            queryString = prop.getProperty(CLOSEST_AREA_QUERY);
        } else {
            queryString = prop.getProperty(CLOSEST_AREA_QUERY_WITH_TRANSFORM);
        }
        for (AreaType areaType: areaTypes){
            SQLQuery sqlQuery;
            String replaced = queryString.replace(TABLE_NAME_PLACEHOLDER, areaMap.get(areaType.value()));
            sqlQuery = createSQLQuery(replaced, request);
            if (!SpatialUtils.isDefaultCrs(request.getCrs())) {
                sqlQuery.setInteger(OTHER_CRS, request.getCrs());
            }
            List queryResult = sqlQuery.list();
            if (queryResult != null && queryResult.size() == 1) {
                ClosestAreaEntry entry = (ClosestAreaEntry) queryResult.get(0);
                response.getClosestArea().add(entry);
            }
        }
        return response;
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> getAreaMap() {
        List<AreaTypesEntity> allEntity = crudService.findAllEntity(AreaTypesEntity.class);
        Map<String, String> areaMap = new HashMap<>();
        for (AreaTypesEntity i : allEntity) areaMap.put(i.getTypeName(),i.getAreaDbTable());
        return areaMap;
    }

    private SQLQuery createSQLQuery(String queryString, ClosestAreaSpatialRequest request) {
        SQLQuery sqlQuery = crudService.getEntityManager().unwrap(Session.class).createSQLQuery(queryString);
        sqlQuery.setResultTransformer(Transformers.aliasToBean(ClosestAreaEntry.class));
        sqlQuery.setString(WKT, request.getWkt());
        sqlQuery.setInteger(DEFAULT_CRS, SpatialUtils.DEFAULT_CRS);
        sqlQuery.setDouble(UNIT, UnitType.valueOf(request.getUnit().name()).getUnit());
        return sqlQuery;
    }
}
