package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.service.exception.CommonGenericDAOException;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaTypeEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreasNameType;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.handler.ExceptionHandlerInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.handler.SpatialExceptionHandler;
import lombok.SneakyThrows;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.transaction.Transactional;
import java.util.List;

@Stateless
@Local(AreaTypeService.class)
@Transactional
@Interceptors(value = ExceptionHandlerInterceptor.class)
public class AreaTypeServiceBean extends AbstractServiceBean implements AreaTypeService {

    @Override
    @SuppressWarnings("unchecked")
    @SneakyThrows(CommonGenericDAOException.class)
    @SpatialExceptionHandler(responseType = AreaTypeSpatialRS.class)
    public AreaTypeSpatialRS getAreaTypes() {
        List<String> areaTypes = commonDao.findEntityByNamedQuery(String.class, AreaTypeEntity.FIND_ALL);
        return createSuccessGetAreaTypesResponse(areaTypes);
    }

    private AreaTypeSpatialRS createSuccessGetAreaTypesResponse(List<String> areaTypeNames) {
        return new AreaTypeSpatialRS(createSuccessResponseMessage(), new AreasNameType(areaTypeNames));
    }

}
