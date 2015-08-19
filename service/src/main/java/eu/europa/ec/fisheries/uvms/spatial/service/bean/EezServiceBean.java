package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezType;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.EezMapper;
import eu.europa.ec.fisheries.uvms.util.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.util.exception.SpatialServiceException;
import org.hibernate.HibernateException;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

/**
 * //TODO create test
 */
@Stateless
@Local(EezService.class)
@Transactional
public class EezServiceBean extends AbstractServiceBean implements EezService {

    @Inject
    private EezMapper eezMapper;

    @Override
    @SuppressWarnings("unchecked")
    public EezSpatialRS getExclusiveEconomicZoneById(EezSpatialRQ getEezSpatialRQ) {
        EezType eezType;
        try {
            int eezId = Integer.parseInt(getEezSpatialRQ.getEezId());
            EezEntity eez = null;
            Object entityById = commonDao.findEntityById(EezEntity.class, eezId);
            if (entityById != null) {
                eez = (EezEntity) entityById;
            }
            eezType = eezMapper.eezEntityToEezType(eez);
        } catch (Exception ex) {
            if (ex instanceof HibernateException) {
                SpatialServiceErrors error = exceptionMapper.convertToSpatialError(ex.getClass());
                return createErrorGetEezResponse(error.formatMessage(), error.getErrorCode());
            } else if (ex instanceof SpatialServiceException) {
                logError(ex);
                SpatialServiceException sse = (SpatialServiceException) ex;
                return createErrorGetEezResponse(sse.getMessage(), sse.getErrorCode());
            } else {
                logError(ex);
                SpatialServiceErrors error = SpatialServiceErrors.INTERNAL_APPLICATION_ERROR;
                return createErrorGetEezResponse(error.formatMessage(), error.getErrorCode());
            }
        }

        return createSuccessResponse(eezType);
    }

    private EezSpatialRS createErrorGetEezResponse(String errorMessage, Integer errorCode) {
        return new EezSpatialRS(createErrorResponseMessage(errorMessage, errorCode), null);
    }

    private EezSpatialRS createSuccessResponse(EezType eez) {
        return new EezSpatialRS(createSuccessResponseMessage(), eez);
    }

}
