package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.service.exception.CommonGenericDAOException;
import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezType;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.handler.ExceptionHandlerInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.handler.SpatialExceptionHandler;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.EezMapper;
import lombok.SneakyThrows;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.transaction.Transactional;

@Stateless
@Local(EezService.class)
@Transactional
@Interceptors(value = ExceptionHandlerInterceptor.class)
public class EezServiceBean extends AbstractServiceBean implements EezService {

    @Inject
    private EezMapper eezMapper;

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

}
