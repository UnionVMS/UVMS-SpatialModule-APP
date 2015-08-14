package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.dao.CommonGenericDAO;
import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.EezMapper;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;

import static com.google.common.collect.Lists.newArrayList;

/**
 * //TODO create test
 */
@Stateless
@Local(ExclusiveEconomicZoneService.class)
@Transactional(Transactional.TxType.REQUIRED)
public class ExclusiveEconomicZoneServiceBean implements ExclusiveEconomicZoneService {

    private final static Logger LOG = LoggerFactory.getLogger(ExclusiveEconomicZoneServiceBean.class);

    @EJB
    private CommonGenericDAO commonGenericDAO;

    @Inject
    private EezMapper eezMapper;

    @Override
    @SuppressWarnings("unchecked")
    public GetEezSpatialRS getExclusiveEconomicZoneById(int id) {
        EezEntity eez = null;
        try {
            eez = (EezEntity) commonGenericDAO.findEntityById(EezEntity.class, id);
        } catch (HibernateException hex) {
            LOG.debug("HibernateException: ", hex);
            LOG.debug("HibernateException cause: ", hex.getCause());

            return createErrorResponse();
        }
        return createSuccessResponse(eez);
    }

    private GetEezSpatialRS createErrorResponse() {
        return new GetEezSpatialRS(createErrorResponseMessage(), null);
    }

    private ResponseMessageType createErrorResponseMessage() {
        ResponseMessageType responseMessage = new ResponseMessageType();
        ErrorMessageType errorMessageType = new ErrorMessageType("Error message", "1232");
        ArrayList<ErrorMessageType> errorMessageTypes = newArrayList(errorMessageType);
        responseMessage.setErrors(new ErrorsType(errorMessageTypes));
        return responseMessage;
    }

    private GetEezSpatialRS createSuccessResponse(EezEntity eez) {
        return new GetEezSpatialRS(createSuccessResponseMessage(), eezMapper.eezEntityToSchema(eez));
    }

    private ResponseMessageType createSuccessResponseMessage() {
        ResponseMessageType responseMessage = new ResponseMessageType();
        responseMessage.setSuccess(new SuccessType());
        return responseMessage;
    }
}
