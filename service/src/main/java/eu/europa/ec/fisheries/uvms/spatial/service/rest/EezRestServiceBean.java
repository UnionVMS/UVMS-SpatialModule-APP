package eu.europa.ec.fisheries.uvms.spatial.service.rest;

import eu.europa.ec.fisheries.uvms.service.CommonGenericDAO;
import eu.europa.ec.fisheries.uvms.service.exception.CommonGenericDAOException;
import eu.europa.ec.fisheries.uvms.spatial.dto.EezDto;
import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.handler.ExceptionHandlerInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.handler.SpatialExceptionHandler;
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
@Local(EezRestService.class)
@Transactional
@Interceptors(value = ExceptionHandlerInterceptor.class)
public class EezRestServiceBean implements EezRestService {

    @EJB
    private CommonGenericDAO commonDao;

    @Inject
    private EezTypeMapper eezMapper;

    @Override
    @SuppressWarnings("unchecked")
    @SneakyThrows(CommonGenericDAOException.class)
    @SpatialExceptionHandler(responseType = EezDto.class)
    public EezDto getEezById(final Integer id) {
        EezEntity eez = (EezEntity) commonDao.findEntityById(EezEntity.class, id);
        return EezDtoMapper.INSTANCE.eezEntityToEezDto(eez);
    }

}
