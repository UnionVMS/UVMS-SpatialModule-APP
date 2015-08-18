package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.GetEezSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.GetEezSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.EezMapper;
import eu.europa.ec.fisheries.uvms.util.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.util.exception.SpatialServiceException;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;

/**
 * //TODO create test
 */
@Stateless
@Local(ExclusiveEconomicZoneService.class)
@Transactional(Transactional.TxType.REQUIRED)
public class ExclusiveEconomicZoneServiceBean extends AbstractServiceBean implements ExclusiveEconomicZoneService {

    @Inject
    private EezMapper eezMapper;

    @Override
    @SuppressWarnings("unchecked")
    public GetEezSpatialRS getExclusiveEconomicZoneById(GetEezSpatialRQ getEezSpatialRQ) {
        EezType eezType;
        try {
            int eezId = Integer.parseInt(getEezSpatialRQ.getEezId());
            EezEntity eez = (EezEntity) commonDao.findEntityById(EezEntity.class, eezId);
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

    private GetEezSpatialRS createErrorGetEezResponse(String errorMessage, Integer errorCode) {
        return new GetEezSpatialRS(createErrorResponseMessage(errorMessage, errorCode), null);
    }

    private GetEezSpatialRS createSuccessResponse(EezType eez) {
        return new GetEezSpatialRS(createSuccessResponseMessage(), eez);
    }

}
