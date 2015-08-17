package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.Maps;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaTypeEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreasNameType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.GetAreaTypesSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.GetAreasByLocationSpatialRS;
import eu.europa.ec.fisheries.uvms.util.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.util.exception.SpatialServiceException;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Local;
import javax.ejb.Stateless;
import java.util.HashMap;
import java.util.List;

import static java.lang.String.valueOf;

@Stateless
@Local(AreaService.class)
public class AreaServiceBean extends AbstractServiceBean implements AreaService {

    private final static Logger LOG = LoggerFactory.getLogger(AreaServiceBean.class);

    private static final String LAT = "lat";
    private static final String LON = "lon";
    private static final String CRS = "crs";

    @Override
    @SuppressWarnings("unchecked")
    public GetAreaTypesSpatialRS getAreaTypes() {
        List<String> areaTypes;
        try {
            areaTypes = commonDao.findEntityByNamedQuery(String.class, AreaTypeEntity.FIND_ALL);
        } catch (HibernateException hex) {
            // Stacktrace logged in commons lib
            SpatialServiceErrors error = SpatialServiceErrors.INTERNAL_APPLICATION_ERROR;
            return createErrorGetAreaTypesResponse(error.formatMessage(), error.getErrorCode());
        } catch (Exception ex) {
            LOG.error("Exception: ", ex);
            LOG.error("Exception cause: ", ex.getCause());

            if (ex instanceof SpatialServiceException) {
                SpatialServiceException sse = (SpatialServiceException) ex;
                return createErrorGetAreaTypesResponse(sse.getMessage(), sse.getErrorCode());
            } else {
                SpatialServiceErrors error = SpatialServiceErrors.INTERNAL_APPLICATION_ERROR;
                return createErrorGetAreaTypesResponse(error.formatMessage(), error.getErrorCode());
            }
        }

        return createSuccessGetAreaTypesResponse(areaTypes);
    }

    private GetAreaTypesSpatialRS createSuccessGetAreaTypesResponse(List<String> areaTypeNames) {
        return new GetAreaTypesSpatialRS(createSuccessResponseMessage(), new AreasNameType(areaTypeNames));
    }

    private GetAreaTypesSpatialRS createErrorGetAreaTypesResponse(String errorMessage, Integer errorCode) {
        return new GetAreaTypesSpatialRS(createErrorResponseMessage(errorMessage, errorCode), null);
    }

    @Override
    public GetAreasByLocationSpatialRS getAreasByLocation(double lat, double lon, int crs) {
        try {
            List<AreaTypeEntity> systemAreaTypes = commonDao.findEntityByNamedQuery(AreaTypeEntity.class, AreaTypeEntity.FIND_SYSTEM);
            for (AreaTypeEntity areaType : systemAreaTypes) {
                String areaDbTable = areaType.getAreaDbTable();

                HashMap<String, String> paramaters = createParamaters(lat, lon, crs);
                List resultList = commonDao.findEntityByNativeQuery("SELECT * FROM " + areaDbTable);

            }
        } catch (HibernateException hex) {
            // Stacktrace logged in commons lib
            SpatialServiceErrors error = SpatialServiceErrors.INTERNAL_APPLICATION_ERROR;
            return createErrorGetAreasByLocationResponse(error.formatMessage(), error.getErrorCode());
        } catch (Exception ex) {
            LOG.error("Exception: ", ex);
            LOG.error("Exception cause: ", ex.getCause());

            if (ex instanceof SpatialServiceException) {
                SpatialServiceException sse = (SpatialServiceException) ex;
                return createErrorGetAreasByLocationResponse(sse.getMessage(), sse.getErrorCode());
            } else {
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

    private GetAreasByLocationSpatialRS createSuccessGetAreasByLocationResponse() {
        return new GetAreasByLocationSpatialRS(createSuccessResponseMessage(), null);
    }

    private GetAreasByLocationSpatialRS createErrorGetAreasByLocationResponse(String errorMessage, Integer errorCode) {
        return new GetAreasByLocationSpatialRS(createErrorResponseMessage(errorMessage, errorCode), null);
    }

}
