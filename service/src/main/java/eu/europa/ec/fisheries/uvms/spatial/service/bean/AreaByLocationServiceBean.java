package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.Lists;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.service.exception.CommonGenericDAOException;
import eu.europa.ec.fisheries.uvms.spatial.dao.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.AreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.handler.ExceptionHandlerInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.handler.SpatialExceptionHandler;
import lombok.SneakyThrows;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by kopyczmi on 18-Aug-15.
 */
@Stateless
@Local(AreaByLocationService.class)
@Transactional
public class AreaByLocationServiceBean implements AreaByLocationService {

    private static final int DEFAULT_CRS = 4326;
    private static final String EPSG = "EPSG:";

    @EJB
    private SpatialRepository repository;

    @Override
    @SneakyThrows(CommonGenericDAOException.class)
    @SpatialExceptionHandler(responseType = AreaByLocationSpatialRS.class)
    @Interceptors(value = ExceptionHandlerInterceptor.class)
    public AreaByLocationSpatialRS getAreasByLocation(AreaByLocationSpatialRQ request) {
        List<AreaTypesEntity> systemAreaTypes = repository.findEntityByNamedQuery(AreaTypesEntity.class, QueryNameConstants.FIND_SYSTEM_AREAS);

        List<AreaTypeEntry> areaTypes = Lists.newArrayList();
        for (AreaTypesEntity areaType : systemAreaTypes) {
            String areaDbTable = areaType.getAreaDbTable();
            String areaTypeName = areaType.getTypeName();

            PointType schemaPoint = request.getPoint();
            Point point = convertToPointInWGS84(schemaPoint.getLongitude(), schemaPoint.getLatitude(), retrieveCrs(schemaPoint.getCrs()));

            List<Integer> resultList = repository.findAreasIdByLocation(point, areaDbTable);
            for (Integer id : resultList) {
                AreaTypeEntry area = new AreaTypeEntry(String.valueOf(id), areaTypeName);
                areaTypes.add(area);
            }
        }

        return createSuccessGetAreasByLocationResponse(new AreasByLocationType(areaTypes));
    }

    @Override
    @SneakyThrows(CommonGenericDAOException.class)
    public List<AreaDto> getAreasByLocationRest(double lat, double lon, int crs) {
        List<AreaTypesEntity> systemAreaTypes = repository.findEntityByNamedQuery(AreaTypesEntity.class, QueryNameConstants.FIND_SYSTEM_AREAS);

        Point point = convertToPointInWGS84(lon, lat, crs);

        List<AreaDto> areaTypes = Lists.newArrayList();
        for (AreaTypesEntity areaType : systemAreaTypes) {
            String areaDbTable = areaType.getAreaDbTable();
            String areaTypeName = areaType.getTypeName();

            List<Integer> resultList = repository.findAreasIdByLocation(point, areaDbTable);
            for (Integer id : resultList) {
                AreaDto areaDto = new AreaDto(String.valueOf(id), areaTypeName);
                areaTypes.add(areaDto);
            }
        }

        return areaTypes;
    }

    private Point convertToPointInWGS84(double lon, double lat, int crs) {
        try {
            GeometryFactory gf = new GeometryFactory();
            Point point = gf.createPoint(new Coordinate(lon, lat));
            if (crs != DEFAULT_CRS) {
                point = transform(crs, point);
            }
            point.setSRID(DEFAULT_CRS);
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

    private Integer retrieveCrs(Integer crs) {
        if (crs == null) {
            return DEFAULT_CRS;
        }
        return crs;
    }

    private AreaByLocationSpatialRS createSuccessGetAreasByLocationResponse(AreasByLocationType areasByLocation) {
        return new AreaByLocationSpatialRS(createSuccessResponseMessage(), areasByLocation);
    }

    private ResponseMessageType createSuccessResponseMessage() {
        ResponseMessageType responseMessage = new ResponseMessageType();
        responseMessage.setSuccess(new SuccessType());
        return responseMessage;
    }
}
