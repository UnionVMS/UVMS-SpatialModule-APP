package eu.europa.ec.fisheries.uvms.spatial.service.queue;

import eu.europa.ec.fisheries.uvms.service.CommonGenericDAO;
import eu.europa.ec.fisheries.uvms.service.exception.CommonGenericDAOException;
import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ResponseMessageType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SuccessType;
import eu.europa.ec.fisheries.uvms.spatial.service.queue.handler.ExceptionHandlerInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.service.queue.handler.SpatialExceptionHandler;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.EezTypeMapper;
import lombok.SneakyThrows;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.transaction.Transactional;

@Stateless
@Local(EezQueueService.class)
@Transactional
@Interceptors(value = ExceptionHandlerInterceptor.class)
public class EezQueueServiceBean implements EezQueueService {

    @EJB
    private CommonGenericDAO commonDao;

    @Inject
    private EezTypeMapper eezMapper;

    @Override
    @SuppressWarnings("unchecked")
    @SneakyThrows(CommonGenericDAOException.class)
    @SpatialExceptionHandler(responseType = EezSpatialRS.class)
    public EezSpatialRS getEezById(EezSpatialRQ getEezSpatialRQ) {
        EezEntity eez = (EezEntity) commonDao.findEntityById(EezEntity.class, retrieveIdFromRQ(getEezSpatialRQ));
        EezType eezType = eezMapper.eezEntityToEezType(eez);
        return createSuccessResponse(eezType);
    }

    private int retrieveIdFromRQ(EezSpatialRQ getEezSpatialRQ) {
        return Integer.parseInt(getEezSpatialRQ.getEezId());
    }

    private EezSpatialRS createSuccessResponse(EezType eez) {
        return new EezSpatialRS(createSuccessResponseMessage(), eez);
    }

    private ResponseMessageType createSuccessResponseMessage() {
        ResponseMessageType responseMessage = new ResponseMessageType();
        responseMessage.setSuccess(new SuccessType());
        return responseMessage;
    }
}
