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
@Interceptors({ExceptionHandlerInterceptor.class})
public class EezServiceBean extends AbstractServiceBean implements EezService {

    @Inject
    private EezMapper eezMapper;

    @Override
    @SuppressWarnings("unchecked")
    @SneakyThrows(CommonGenericDAOException.class)
    @SpatialExceptionHandler(responseType = EezSpatialRS.class)
    public EezSpatialRS getExclusiveEconomicZoneById(EezSpatialRQ getEezSpatialRQ) {
        EezType eezType;
        int eezId = Integer.parseInt(getEezSpatialRQ.getEezId());
        EezEntity eez = null;
        Object entityById = commonDao.findEntityById(EezEntity.class, eezId);
        if (entityById != null) {
            eez = (EezEntity) entityById;
        }
        eezType = eezMapper.eezEntityToEezType(eez);

        return createSuccessResponse(eezType);
    }

    private EezSpatialRS createSuccessResponse(EezType eez) {
        return new EezSpatialRS(createSuccessResponseMessage(), eez);
    }

}
