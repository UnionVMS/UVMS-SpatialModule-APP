package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.Lists;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.common.SpatialUtils;
import eu.europa.ec.fisheries.uvms.service.CrudService;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UnitType;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.handler.SpatialExceptionHandler;
import eu.europa.ec.fisheries.uvms.util.ModelUtils;
import eu.europa.ec.fisheries.uvms.util.SqlPropertyHolder;
import lombok.extern.slf4j.Slf4j;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static eu.europa.ec.fisheries.uvms.common.SpatialUtils.DEFAULT_CRS;
import static eu.europa.ec.fisheries.uvms.util.ModelUtils.createSuccessResponseMessage;
import static eu.europa.ec.fisheries.uvms.util.SpatialUtils.convertToPointInWGS84;
import static eu.europa.ec.fisheries.uvms.util.SpatialUtils.convertToWkt;
import static eu.europa.ec.fisheries.uvms.util.SpatialUtils.defaultIfNull;

@Stateless
@Local(ClosestAreaService.class)
@Transactional
@Slf4j
public class ClosestAreaServiceBean implements ClosestAreaService {

    @EJB
    private CrudService crudService;

    private static final String EPSG = "EPSG:";
    private final static String TABLE_NAME_PLACEHOLDER = "{tableName}";

    private final static String CLOSEST_AREA_QUERY_WITH_TRANSFORM = "sql.closestAreaWithTransform";
    private final static String OTHER_CRS = "otherCrs";
    private final static String DEFAULT_CRS = "defaultCrs";
    private final static String WKT = "wkt";
    private final static String UNIT = "unit";

    @EJB
    private SqlPropertyHolder prop;

    @Override
    @SpatialExceptionHandler(responseType = ClosestAreaSpatialRS.class)
    public ClosestAreaSpatialRS getClosestArea(final ClosestAreaSpatialRQ request) {
        List<AreaType> areaTypes = request.getAreaTypes().getAreaType();
        Map<String, String> areaMap = getAreaMap();
        //String queryString = prop.getProperty(CLOSEST_AREA_QUERY);

        PointType schemaPoint = request.getPoint();
        Point point = convertToPointInWGS84(schemaPoint.getLongitude(), schemaPoint.getLatitude(), defaultIfNull(schemaPoint.getCrs()));

        List<ClosestAreaEntry> closestAreaList = Lists.newArrayList();
        for (AreaType areaType: areaTypes){
            //String replaced = queryString.replace(TABLE_NAME_PLACEHOLDER, areaMap.get(areaType.value()));
            String wktPoint = convertToWkt(point);
            //SQLQuery sqlQuery = createSQLQuery(replaced, wktPoint, SpatialUtils.DEFAULT_CRS, UnitType.valueOf(request.getUnit().name()).getUnit());
            List queryResult = null;//sqlQuery.list();
            if (queryResult != null && queryResult.size() == 1) {
                ClosestAreaEntry area = (ClosestAreaEntry) queryResult.get(0);
                closestAreaList.add(area);
            }
        }
        return createSuccessClosestAreaResponse(new ClosestAreasType(closestAreaList));
    }

    private ClosestAreaSpatialRS createSuccessClosestAreaResponse(ClosestAreasType closestAreasType) {
        return new ClosestAreaSpatialRS(createSuccessResponseMessage(), closestAreasType);
    }

    private ClosestAreaSpatialRS createResponse() {
        ClosestAreaSpatialRS response = new ClosestAreaSpatialRS();
        response.setResponseMessage(ModelUtils.createSuccessResponseMessage());
        return response;
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> getAreaMap() {
        List<AreaTypesEntity> allEntity = crudService.findAllEntity(AreaTypesEntity.class);
        Map<String, String> areaMap = new HashMap<>();
        for (AreaTypesEntity i : allEntity) areaMap.put(i.getTypeName(),i.getAreaDbTable());
        return areaMap;
    }


}
