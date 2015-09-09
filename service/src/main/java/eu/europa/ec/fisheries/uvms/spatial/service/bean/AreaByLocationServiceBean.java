package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.Lists;
import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.service.CrudService;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.repository.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.AreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.EnrichmentDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.handler.ExceptionHandlerInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.handler.SpatialExceptionHandler;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.transaction.Transactional;
import java.util.List;

import static eu.europa.ec.fisheries.uvms.util.ModelUtils.containsError;
import static eu.europa.ec.fisheries.uvms.util.ModelUtils.createSuccessResponseMessage;
import static eu.europa.ec.fisheries.uvms.util.SpatialUtils.convertToPointInWGS84;

/**
 * Created by kopyczmi on 18-Aug-15.
 */
@Stateless(name = "areaByLocationService")
@Local(AreaByLocationService.class)
@Transactional
@Slf4j
public class AreaByLocationServiceBean implements AreaByLocationService, SpatialEnrichmentSupport {

    @EJB(name = "closestAreaService")
    private SpatialEnrichmentSupport next;

    @EJB
    private SpatialRepository repository;

    @EJB
    private CrudService crudService;

    @Override
    public SpatialEnrichmentRS handleRequest(SpatialEnrichmentRQ request, SpatialEnrichmentRS response) {
        if (containsError(response.getResponseMessage())) {
            return response;
        }

        AreaByLocationSpatialRS areaByLocationRS = getAreasByLocation(new AreaByLocationSpatialRQ(request.getPoint()));

        if (containsError(areaByLocationRS.getResponseMessage())) {
            return addErrorMessage(response, areaByLocationRS.getResponseMessage());
        }
        response.setAreasByLocation(areaByLocationRS.getAreasByLocation());

        return next.handleRequest(request, response);
    }

    private SpatialEnrichmentRS addErrorMessage(SpatialEnrichmentRS response, ResponseMessageType responseMessage) {
        response.setResponseMessage(responseMessage);
        return response;
    }

    @Override
    public EnrichmentDto handleRequest(double lat, double lon, int crs, String unit, List<String> areaTypes, List<String> locationTypes, EnrichmentDto enrichmentDto) {
        List<AreaDto> areasByLocation = getAreasByLocationRest(lat, lon, crs);
        enrichmentDto.setAreasByLocation(areasByLocation);
        return next.handleRequest(lat, lon, crs, unit, areaTypes, locationTypes, enrichmentDto);
    }

    @Override
    @SpatialExceptionHandler(responseType = AreaByLocationSpatialRS.class)
    @Interceptors(value = ExceptionHandlerInterceptor.class)
    public AreaByLocationSpatialRS getAreasByLocation(AreaByLocationSpatialRQ request) {
        Point point = convertToPointInWGS84(request.getPoint());

        List<AreaLocationTypesEntity> systemAreaTypes = getAreaTypes();
        List<AreaTypeEntry> areaTypes = Lists.newArrayList();
        for (AreaLocationTypesEntity areaType : systemAreaTypes) {
            String areaDbTable = areaType.getAreaDbTable();
            String areaTypeName = areaType.getTypeName();

            List<Integer> resultList = repository.findAreasIdByLocation(point, areaDbTable);
            for (Integer id : resultList) {
                AreaTypeEntry area = new AreaTypeEntry(String.valueOf(id), areaTypeName);
                areaTypes.add(area);
            }
        }

        return createSuccessGetAreasByLocationResponse(new AreasByLocationType(areaTypes));
    }

    @Override
    public List<AreaDto> getAreasByLocationRest(double lat, double lon, int crs) {
        Point point = convertToPointInWGS84(lon, lat, crs);

        List<AreaLocationTypesEntity> systemAreaTypes = getAreaTypes();
        List<AreaDto> areaTypes = Lists.newArrayList();
        for (AreaLocationTypesEntity areaType : systemAreaTypes) {
            String areaDbTable = areaType.getAreaDbTable();
            String areaTypeName = areaType.getTypeName();

            List<Integer> resultList = repository.findAreasIdByLocation(point, areaDbTable);
            for (Integer id : resultList) {
                AreaDto areaDto = new AreaDto(String.valueOf(id), areaTypeName);
                areaTypes.add(areaDto);
            }
        }

        return areaTypes;
    }

    private List<AreaLocationTypesEntity> getAreaTypes() {
        return crudService.findEntityByNamedQuery(AreaLocationTypesEntity.class, QueryNameConstants.FIND_SYSTEM_AREAS);
    }

    private AreaByLocationSpatialRS createSuccessGetAreasByLocationResponse(AreasByLocationType areasByLocation) {
        return new AreaByLocationSpatialRS(createSuccessResponseMessage(), areasByLocation);
    }

}
