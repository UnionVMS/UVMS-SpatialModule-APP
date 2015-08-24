package eu.europa.ec.fisheries.uvms.spatial.service.rest;

import com.google.common.collect.Lists;
import eu.europa.ec.fisheries.uvms.service.exception.CommonGenericDAOException;
import eu.europa.ec.fisheries.uvms.spatial.dao.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreasWithIdType;
import eu.europa.ec.fisheries.uvms.spatial.service.queue.AreaByLocationQueueService;
import eu.europa.ec.fisheries.uvms.spatial.service.queue.handler.ExceptionHandlerInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.service.rest.dto.AreaDto;
import lombok.SneakyThrows;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Michal Kopyczok on 24-Aug-15.
 */
@Stateless
@Local(AreaByLocationQueueService.class)
@Transactional
public class AreaByLocationRestServiceBean implements AreaByLocationRestService {

    @EJB
    private SpatialRepository repository;

    @Override
    @SneakyThrows(CommonGenericDAOException.class)
    public List<AreaDto> getAreasByLocation(double lat, double lon, int crs) {
        List<AreaTypesEntity> systemAreaTypes = repository.findEntityByNamedQuery(AreaTypesEntity.class, QueryNameConstants.FIND_SYSTEM_AREAS);

        List<AreaDto> areaTypes = Lists.newArrayList();
        for (AreaTypesEntity areaType : systemAreaTypes) {
            String areaDbTable = areaType.getAreaDbTable();
            String areaTypeName = areaType.getTypeName();

            List<Integer> resultList = repository.findAreaIdByLocation(lat, lon, crs, areaDbTable);
            for (Integer id : resultList) {
                AreaDto area = new AreaDto(String.valueOf(id), areaTypeName);
                areaTypes.add(area);
            }
        }

        return areaTypes;
    }
}
