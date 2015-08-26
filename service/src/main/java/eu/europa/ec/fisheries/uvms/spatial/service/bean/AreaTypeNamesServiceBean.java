package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.service.CommonGenericDAO;
import eu.europa.ec.fisheries.uvms.service.exception.CommonGenericDAOException;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.handler.ExceptionHandlerInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.handler.SpatialExceptionHandler;
import lombok.SneakyThrows;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.transaction.Transactional;
import java.util.List;

@Stateless
@Local(AreaTypeNamesService.class)
@Transactional
public class AreaTypeNamesServiceBean implements AreaTypeNamesService {

    @EJB
    private CommonGenericDAO commonDao;

    @Override
    @SuppressWarnings("unchecked")
    @SneakyThrows(CommonGenericDAOException.class)
    @SpatialExceptionHandler(responseType = AreaTypeSpatialRS.class)
    @Interceptors(value = ExceptionHandlerInterceptor.class)
    public AreaTypeSpatialRS getAreaTypesQueue() {
        List<String> areaTypes = commonDao.findEntityByNamedQuery(String.class, QueryNameConstants.FIND_ALL_AREAS);
        return createSuccessGetAreaTypesResponse(areaTypes);
    }

    @Override
    @SuppressWarnings("unchecked")
    @SneakyThrows(CommonGenericDAOException.class)
    public List<String> getAreaTypesRest() {
        return commonDao.findEntityByNamedQuery(String.class, QueryNameConstants.FIND_ALL_AREAS);
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
