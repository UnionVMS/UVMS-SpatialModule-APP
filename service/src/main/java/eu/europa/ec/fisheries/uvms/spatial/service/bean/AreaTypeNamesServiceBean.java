package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.service.CrudService;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeNamesSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreasNameType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ResponseMessageType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SuccessType;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.handler.ExceptionHandlerInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.handler.SpatialExceptionHandler;
import eu.europa.ec.fisheries.uvms.util.ModelUtils;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.transaction.Transactional;
import java.util.List;

import static eu.europa.ec.fisheries.uvms.util.ModelUtils.createSuccessResponseMessage;

@Stateless
@Local(AreaTypeNamesService.class)
@Transactional
public class AreaTypeNamesServiceBean implements AreaTypeNamesService {

    @EJB
    private CrudService crudService;

    @Override
    @SuppressWarnings("unchecked")
    @SpatialExceptionHandler(responseType = AreaTypeNamesSpatialRS.class)
    @Interceptors(value = ExceptionHandlerInterceptor.class)
    public AreaTypeNamesSpatialRS getAreaTypes() {
        List<String> areaTypes = crudService.findEntityByNamedQuery(String.class, QueryNameConstants.FIND_ALL_AREAS);
        return createSuccessGetAreaTypesResponse(areaTypes);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> getAreaTypesRest() {
        return crudService.findEntityByNamedQuery(String.class, QueryNameConstants.FIND_ALL_AREAS);
    }

    private AreaTypeNamesSpatialRS createSuccessGetAreaTypesResponse(List<String> areaTypeNames) {
        return new AreaTypeNamesSpatialRS(createSuccessResponseMessage(), new AreasNameType(areaTypeNames));
    }

}
