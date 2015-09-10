package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.service.CrudService;
import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.EezDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.handler.ExceptionHandlerInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.EezDtoMapper;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.EezTypeMapper;

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
    private CrudService crudService;

    @Inject
    private EezTypeMapper eezMapper;

    @Override
    @SuppressWarnings("unchecked")
    @Interceptors(value = ExceptionHandlerInterceptor.class)
    public EezSpatialRS getEezById(EezSpatialRQ getEezSpatialRQ) {
        EezEntity eez = (EezEntity) crudService.findEntityById(EezEntity.class, retrieveIdFromRQ(getEezSpatialRQ));
        EezType eezType = eezMapper.eezEntityToEezType(eez);
        return createSuccessResponse(eezType);
    }

    @Override
    @SuppressWarnings("unchecked")
    public EezDto getEezByIdRest(int id) {
        EezEntity eez = (EezEntity) crudService.findEntityById(EezEntity.class, id);
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
