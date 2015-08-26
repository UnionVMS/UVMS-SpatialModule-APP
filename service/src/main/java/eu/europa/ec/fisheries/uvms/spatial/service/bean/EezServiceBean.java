package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.service.CommonGenericDAO;
import eu.europa.ec.fisheries.uvms.service.exception.CommonGenericDAOException;
import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ResponseMessageType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SuccessType;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.EezDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.handler.ExceptionHandlerInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.handler.SpatialExceptionHandler;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.EezDtoMapper;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.EezTypeMapper;
import lombok.SneakyThrows;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.transaction.Transactional;

@Stateless
@Local(EezService.class)
@Transactional
public class EezServiceBean implements EezService {

    @EJB
    private CommonGenericDAO commonDao;

    @Inject
    private EezTypeMapper eezMapper;

    @Override
    @SuppressWarnings("unchecked")
    @SneakyThrows(CommonGenericDAOException.class)
    @SpatialExceptionHandler(responseType = EezSpatialRS.class)
    @Interceptors(value = ExceptionHandlerInterceptor.class)
    public EezSpatialRS getEezByIdQueue(EezSpatialRQ getEezSpatialRQ) {
        EezEntity eez = (EezEntity) commonDao.findEntityById(EezEntity.class, retrieveIdFromRQ(getEezSpatialRQ));
        EezType eezType = eezMapper.eezEntityToEezType(eez);
        return createSuccessResponse(eezType);
    }

    @Override
    @SuppressWarnings("unchecked")
    @SneakyThrows(CommonGenericDAOException.class)
    public EezDto getEezByIdRest(int id) {
        EezEntity eez = (EezEntity) commonDao.findEntityById(EezEntity.class, id);
        return EezDtoMapper.INSTANCE.eezEntityToEezDto(eez);
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
