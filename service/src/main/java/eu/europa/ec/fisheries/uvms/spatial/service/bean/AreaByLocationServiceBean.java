package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.Lists;
import eu.europa.ec.fisheries.uvms.service.exception.CommonGenericDAOException;
import eu.europa.ec.fisheries.uvms.spatial.dao.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.AreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.handler.ExceptionHandlerInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.handler.SpatialExceptionHandler;
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
@Local(AreaByLocationService.class)
@Transactional
public class AreaByLocationServiceBean implements AreaByLocationService {

    private static final int DEFAULT_CRS = 4326;

    @EJB
    private SpatialRepository repository;

    @Override
    @SneakyThrows(CommonGenericDAOException.class)
    @SpatialExceptionHandler(responseType = AreaByLocationSpatialRS.class)
    @Interceptors(value = ExceptionHandlerInterceptor.class)
    public AreaByLocationSpatialRS getAreasByLocation(AreaByLocationSpatialRQ request) {
        List<AreaTypesEntity> systemAreaTypes = repository.findEntityByNamedQuery(AreaTypesEntity.class, QueryNameConstants.FIND_SYSTEM_AREAS);

        List<AreaTypeEntry> areaTypes = Lists.newArrayList();
        for (AreaTypesEntity areaType : systemAreaTypes) {
            String areaDbTable = areaType.getAreaDbTable();
            String areaTypeName = areaType.getTypeName();

            PointType point = request.getPoint();
            List<Integer> resultList = repository.findAreasIdByLocation(point.getLatitude(), point.getLongitude(), retrieveCrs(point.getCrs()), areaDbTable);
            for (Integer id : resultList) {
                AreaTypeEntry area = new AreaTypeEntry(String.valueOf(id), areaTypeName);
                areaTypes.add(area);
            }
        }

        return createSuccessGetAreasByLocationResponse(new AreasByLocationType(areaTypes));
    }

    @Override
    @SneakyThrows(CommonGenericDAOException.class)
    public List<AreaDto> getAreasByLocationRest(double lat, double lon, int crs) {
        List<AreaTypesEntity> systemAreaTypes = repository.findEntityByNamedQuery(AreaTypesEntity.class, QueryNameConstants.FIND_SYSTEM_AREAS);

        List<AreaDto> areaTypes = Lists.newArrayList();
        for (AreaTypesEntity areaType : systemAreaTypes) {
            String areaDbTable = areaType.getAreaDbTable();
            String areaTypeName = areaType.getTypeName();

            List<Integer> resultList = repository.findAreasIdByLocation(lat, lon, crs, areaDbTable);
            for (Integer id : resultList) {
                AreaDto areaDto = new AreaDto(String.valueOf(id), areaTypeName);
                areaTypes.add(areaDto);
            }
        }

        return areaTypes;
    }

    private Integer retrieveCrs(Integer crs) {
        if (crs == null) {
            return new Integer(DEFAULT_CRS);
        }
        return crs;
    }

    private AreaByLocationSpatialRS createSuccessGetAreasByLocationResponse(AreasByLocationType areasByLocation) {
        return new AreaByLocationSpatialRS(createSuccessResponseMessage(), areasByLocation);
    }

    private ResponseMessageType createSuccessResponseMessage() {
        ResponseMessageType responseMessage = new ResponseMessageType();
        responseMessage.setSuccess(new SuccessType());
        return responseMessage;
    }
}
