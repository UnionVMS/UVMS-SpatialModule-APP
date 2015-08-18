package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.entity.AreaTypeEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreasNameType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.GetAreaTypesSpatialRS;
import eu.europa.ec.fisheries.uvms.util.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.util.exception.SpatialServiceException;
import org.hibernate.HibernateException;

import javax.ejb.Local;
import javax.ejb.Stateless;
import java.util.List;

@Stateless
@Local(AreaTypeService.class)
public class AreaTypeTypeServiceBean extends AbstractServiceBean implements AreaTypeService {

    @Override
    @SuppressWarnings("unchecked")
    public GetAreaTypesSpatialRS getAreaTypes() {
        List<String> areaTypes;
        try {
            areaTypes = commonDao.findEntityByNamedQuery(String.class, AreaTypeEntity.FIND_ALL);
        } catch (Exception ex) {
            if (ex instanceof HibernateException) {
                SpatialServiceErrors error = exceptionMapper.convertToSpatialError(ex.getClass());
                return createErrorGetAreaTypesResponse(error.formatMessage(), error.getErrorCode());
            } else if (ex instanceof SpatialServiceException) {
                logError(ex);
                SpatialServiceException sse = (SpatialServiceException) ex;
                return createErrorGetAreaTypesResponse(sse.getMessage(), sse.getErrorCode());
            } else {
                logError(ex);
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

}
