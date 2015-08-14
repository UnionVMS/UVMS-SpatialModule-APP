package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.Maps;
import eu.europa.ec.fisheries.uvms.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.exception.SpatialServiceException;
import eu.europa.ec.fisheries.uvms.spatial.dao.CommonGenericDAO;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaTypeEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreasNameType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.GetAreaTypesSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.GetAreasByLocationSpatialRS;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import java.util.HashMap;
import java.util.List;

import static java.lang.String.valueOf;

@Stateless
@Local(AreaService.class)
public class AreaServiceBean extends AbstractServiceBean implements AreaService {

    public static final String LAT = "lat";
    public static final String LON = "lon";
    public static final String CRS = "crs";
    private final static Logger LOG = LoggerFactory.getLogger(AreaServiceBean.class);
    @EJB
    private CommonGenericDAO commonDao;

    @Override
    @SuppressWarnings("unchecked")
    public GetAreaTypesSpatialRS getAreaTypes() {
        List<String> areaTypes = null;
        try {
            areaTypes = commonDao.findEntityByNamedQuery(String.class, AreaTypeEntity.FIND_ALL);
        } catch (HibernateException hex) {
            LOG.debug("HibernateException: ", hex);
            LOG.debug("HibernateException cause: ", hex.getCause());

            SpatialServiceErrors error = SpatialServiceErrors.DAO_FIX_IT_ERROR;
            return createErrorGetAreaTypesResponse(error.formatMessage(), error.getErrorCode());
        } catch (Exception ex) {
            LOG.debug("Exception: ", ex);
            LOG.debug("Exception cause: ", ex.getCause());

            SpatialServiceErrors error = SpatialServiceErrors.DAO_FIX_IT_ERROR;
            return createErrorGetAreaTypesResponse(error.formatMessage(), error.getErrorCode());
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
        } catch (Exception e) {
            LOG.error("Error during getting areas by location ", e);
            throw new SpatialServiceException(SpatialServiceErrors.DAO_FIX_IT_ERROR, e.getCause());
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
        return new GetAreasByLocationSpatialRS();
    }

}
