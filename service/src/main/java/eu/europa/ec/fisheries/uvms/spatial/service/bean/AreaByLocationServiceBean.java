package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.Maps;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaTypeEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaByLocationSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.handler.ExceptionHandler;
import eu.europa.ec.fisheries.uvms.util.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.util.exception.SpatialServiceException;
import org.apache.commons.lang3.NotImplementedException;
import org.hibernate.HibernateException;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;

import static java.lang.String.valueOf;

/**
 * Created by kopyczmi on 18-Aug-15.
 */
@Stateless
@Local(AreaByLocationService.class)
@Transactional(Transactional.TxType.REQUIRED)
public class AreaByLocationServiceBean extends AbstractServiceBean implements AreaByLocationService {

    private static final String LAT = "lat";
    private static final String LON = "lon";
    private static final String CRS = "crs";

    @Override
    public AreaByLocationSpatialRS getAreasByLocation(double lat, double lon, int crs)  {
        try {
            List<AreaTypeEntity> systemAreaTypes = commonDao.findEntityByNamedQuery(AreaTypeEntity.class, AreaTypeEntity.FIND_SYSTEM);
            for (AreaTypeEntity areaType : systemAreaTypes) {
                String areaDbTable = areaType.getAreaDbTable();
                HashMap<String, String> paramaters = createParamaters(lat, lon, crs);
                List resultList = commonDao.findEntityByNativeQuery("SELECT * FROM " + areaDbTable);
                System.out.println("Test");
            }
        } catch (Exception ex) {
            if (ex instanceof HibernateException) {
                SpatialServiceErrors error = exceptionMapper.convertToSpatialError(ex.getClass());
                return createErrorGetAreasByLocationResponse(error.formatMessage(), error.getErrorCode());
            } else if (ex instanceof SpatialServiceException) {
                logError(ex);
                SpatialServiceException sse = (SpatialServiceException) ex;
                return createErrorGetAreasByLocationResponse(sse.getMessage(), sse.getErrorCode());
            } else {
                logError(ex);
                SpatialServiceErrors error = SpatialServiceErrors.INTERNAL_APPLICATION_ERROR;
                return createErrorGetAreasByLocationResponse(error.formatMessage(), error.getErrorCode());
            }
        }

        return createSuccessGetAreasByLocationResponse();
    }

    private HashMap<String, String> createParamaters(double lat, double lon, int crs) {
        HashMap<String, String> result = Maps.newHashMap();
        result.put(LAT, valueOf(lat));
        result.put(LON, valueOf(lon));
        result.put(CRS, valueOf(crs));
        return result;
    }

    private AreaByLocationSpatialRS createSuccessGetAreasByLocationResponse() {
        return new AreaByLocationSpatialRS(createSuccessResponseMessage(), null);
    }

    private AreaByLocationSpatialRS createErrorGetAreasByLocationResponse(String errorMessage, Integer errorCode) {
        return new AreaByLocationSpatialRS(createErrorResponseMessage(errorMessage, errorCode), null);
    }

}
