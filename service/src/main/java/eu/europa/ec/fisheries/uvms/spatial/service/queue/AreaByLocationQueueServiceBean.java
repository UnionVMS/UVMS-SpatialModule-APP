package eu.europa.ec.fisheries.uvms.spatial.service.queue;

import com.google.common.collect.Lists;
import eu.europa.ec.fisheries.uvms.service.exception.CommonGenericDAOException;
import eu.europa.ec.fisheries.uvms.spatial.dao.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.service.queue.handler.ExceptionHandlerInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.service.queue.handler.SpatialExceptionHandler;
import lombok.SneakyThrows;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by kopyczmi on 18-Aug-15.
 */
@Stateless
@Local(AreaByLocationQueueService.class)
@Transactional
@Interceptors(value = ExceptionHandlerInterceptor.class)
public class AreaByLocationQueueServiceBean implements AreaByLocationQueueService {

    private static final int DEFAULT_CRS = 4326;

    @EJB
    private SpatialRepository repository;

    @Override
    @SneakyThrows(CommonGenericDAOException.class)
    @SpatialExceptionHandler(responseType = AreaByLocationSpatialRS.class)
    public AreaByLocationSpatialRS getAreasByLocation(AreaByLocationSpatialRQ request) {
        List<AreaTypesEntity> systemAreaTypes = repository.findEntityByNamedQuery(AreaTypesEntity.class, QueryNameConstants.FIND_SYSTEM_AREAS);

        List<AreaType> areaTypes = Lists.newArrayList();
        for (AreaTypesEntity areaType : systemAreaTypes) {
            String areaDbTable = areaType.getAreaDbTable();
            String areaTypeName = areaType.getTypeName();

            List<Integer> resultList = repository.findAreaIdByLocation(request.getLatitude(), request.getLongitude(), getCrs(request.getCrs()), areaDbTable);
            for (Integer id : resultList) {
                AreaType area = new AreaType(String.valueOf(id), areaTypeName);
                areaTypes.add(area);
            }
        }

        return createSuccessGetAreasByLocationResponse(new AreasWithIdType(areaTypes));
    }

    private Integer getCrs(Integer crs) {
        if (crs == null) {
            return new Integer(DEFAULT_CRS);
        }
        return crs;
    }

    private AreaByLocationSpatialRS createSuccessGetAreasByLocationResponse(AreasWithIdType areasWithIdType) {
        return new AreaByLocationSpatialRS(createSuccessResponseMessage(), areasWithIdType);
    }

    private ResponseMessageType createSuccessResponseMessage() {
        ResponseMessageType responseMessage = new ResponseMessageType();
        responseMessage.setSuccess(new SuccessType());
        return responseMessage;
    }
}
