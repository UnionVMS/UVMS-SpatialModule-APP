package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.dao.CommonGenericDAOBean;
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

    private final static Logger LOG = LoggerFactory.getLogger(ExclusiveEconomicZoneServiceBean.class);

    @Inject
    private CommonGenericDAOBean commonDao;

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
        } catch (HibernateException hex) {
            LOG.error("HibernateException: ", hex);
            LOG.error("HibernateException cause: ", hex.getCause());

            SpatialServiceErrors error = SpatialServiceErrors.INTERNAL_APPLICATION_ERROR;
            return createErrorGetEEzResponse(error.formatMessage(), error.getErrorCode());
        } catch (Exception ex) {
            LOG.error("Exception: ", ex);
            LOG.error("Exception cause: ", ex.getCause());

            if (ex instanceof SpatialServiceException) {
                SpatialServiceException sse = (SpatialServiceException) ex;
                return createErrorGetEEzResponse(sse.getMessage(), sse.getErrorCode());
            } else {
                SpatialServiceErrors error = SpatialServiceErrors.INTERNAL_APPLICATION_ERROR;
                return createErrorGetEEzResponse(error.formatMessage(), error.getErrorCode());
            }
        }

        return createSuccessResponse(eezType);
    }

    private GetEezSpatialRS createErrorGetEEzResponse(String errorMessage, Integer errorCode) {
        return new GetEezSpatialRS(createErrorResponseMessage(errorMessage, errorCode), null);
    }

    private GetEezSpatialRS createSuccessResponse(EezType eez) {
        return new GetEezSpatialRS(createSuccessResponseMessage(), eez);
    }

}
