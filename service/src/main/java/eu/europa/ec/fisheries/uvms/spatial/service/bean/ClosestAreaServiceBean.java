package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.common.SpatialUtils;
import eu.europa.ec.fisheries.uvms.service.CrudService;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaEntry;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialRequest;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialResponse;
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

@Stateless
@Local(ClosestAreaService.class)
@Transactional
@Slf4j
public class ClosestAreaServiceBean implements ClosestAreaService {

    @EJB
    private CrudService crudService;

    private static final String EPSG = "EPSG:";
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


        String wkt = request.getWkt();
        if (!SpatialUtils.isDefaultCrs(request.getCrs()))
        {
            wkt = null;
        }


        for (AreaType areaType: areaTypes){
            SQLQuery sqlQuery;
            String replaced = queryString.replace(TABLE_NAME_PLACEHOLDER, areaMap.get(areaType.value()));

            sqlQuery = createSQLQuery(replaced, wkt, SpatialUtils.DEFAULT_CRS, UnitType.valueOf(request.getUnit().name()).getUnit());
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

    private Point convertToPointInWGS84(double lon, double lat, int crs) {
        try {
            GeometryFactory gf = new GeometryFactory();
            Point point = gf.createPoint(new Coordinate(lon, lat));
            if (crs != SpatialUtils.DEFAULT_CRS) {
                point = transform(crs, point);
            }
            point.setSRID(SpatialUtils.DEFAULT_CRS);
            return point;
        } catch (FactoryException ex) {
            throw new SpatialServiceException(SpatialServiceErrors.NO_SUCH_CRS_CODE_ERROR, String.valueOf(crs));
        } catch (MismatchedDimensionException | TransformException ex) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR);
        }
    }

    private Point transform(int crs, Point point) throws FactoryException, TransformException {
        CoordinateReferenceSystem inputCrs = CRS.decode(EPSG + crs);
        MathTransform mathTransform = CRS.findMathTransform(inputCrs, DefaultGeographicCRS.WGS84, false);
        point = (Point) JTS.transform(point, mathTransform);
        return point;
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> getAreaMap() {
        List<AreaTypesEntity> allEntity = crudService.findAllEntity(AreaTypesEntity.class);
        Map<String, String> areaMap = new HashMap<>();
        for (AreaTypesEntity i : allEntity) areaMap.put(i.getTypeName(),i.getAreaDbTable());
        return areaMap;
    }

    private SQLQuery createSQLQuery(String queryString, String wktPoint, int defaultCrs, double unit) {
        SQLQuery sqlQuery = crudService.getEntityManager().unwrap(Session.class).createSQLQuery(queryString);
        sqlQuery.setResultTransformer(Transformers.aliasToBean(ClosestAreaEntry.class));
        sqlQuery.setString(WKT, wktPoint);
        sqlQuery.setInteger(DEFAULT_CRS, defaultCrs);
        sqlQuery.setDouble(UNIT, unit);
        return sqlQuery;
    }
}
