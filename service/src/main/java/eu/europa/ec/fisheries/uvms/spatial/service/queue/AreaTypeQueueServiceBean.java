package eu.europa.ec.fisheries.uvms.spatial.service.queue;

import eu.europa.ec.fisheries.uvms.service.CommonGenericDAO;
import eu.europa.ec.fisheries.uvms.service.exception.CommonGenericDAOException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreasNameType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaRequest;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaResponse;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ResponseMessageType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SuccessType;
import eu.europa.ec.fisheries.uvms.spatial.service.queue.handler.ExceptionHandlerInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.service.queue.handler.SpatialExceptionHandler;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import lombok.SneakyThrows;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.transaction.Transactional;
import java.util.List;

@Stateless
@Local(AreaTypeQueueService.class)
@Transactional
@Interceptors(value = ExceptionHandlerInterceptor.class)
public class AreaTypeQueueServiceBean implements AreaTypeQueueService {

    @EJB
    private CommonGenericDAO commonDao;

    @Override
    @SuppressWarnings("unchecked")
    @SneakyThrows(CommonGenericDAOException.class)
    @SpatialExceptionHandler(responseType = AreaTypeSpatialRS.class)
    public AreaTypeSpatialRS getAreaTypes() {
        List<String> areaTypes = commonDao.findEntityByNamedQuery(String.class, QueryNameConstants.FIND_ALL_AREAS);
        return createSuccessGetAreaTypesResponse(areaTypes);
    }

    @Override
    public ClosestAreaResponse getClosestArea(ClosestAreaRequest request) {
        return null;
    }

    private AreaTypeSpatialRS createSuccessGetAreaTypesResponse(List<String> areaTypeNames) {
        return new AreaTypeSpatialRS(createSuccessResponseMessage(), new AreasNameType(areaTypeNames));
    }

    private ResponseMessageType createSuccessResponseMessage() {
        ResponseMessageType responseMessage = new ResponseMessageType();
        responseMessage.setSuccess(new SuccessType());
        return responseMessage;
    }

}
