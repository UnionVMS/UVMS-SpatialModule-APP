package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.Maps;
import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.service.CrudService;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.repository.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.ClosestAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.EnrichmentDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.MeasurementUnit;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.handler.ExceptionHandlerInterceptor;
import eu.europa.ec.fisheries.uvms.util.SpatialUtils;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static eu.europa.ec.fisheries.uvms.util.ModelUtils.containsError;
import static eu.europa.ec.fisheries.uvms.util.ModelUtils.createSuccessResponseMessage;
import static eu.europa.ec.fisheries.uvms.util.SpatialUtils.convertToPointInWGS84;

@Stateless
@Local(ClosestAreaService.class)
@Transactional
@Slf4j
public class ClosestAreaServiceBean implements ClosestAreaService, SpatialEnrichmentSupport {

    @EJB(beanName = "ClosestLocationServiceBean")
    private SpatialEnrichmentSupport next;

    @EJB
    private SpatialRepository repository;

    @EJB
    private CrudService crudService;

    @Override
    public SpatialEnrichmentRS handleRequest(SpatialEnrichmentRQ request, SpatialEnrichmentRS response) {
        List<AreaType> areaTypes = request.getAreaTypes().getAreaTypes();
        ClosestAreaSpatialRS closestAreasRS = getClosestAreas(new ClosestAreaSpatialRQ(request.getPoint(), new ClosestAreaSpatialRQ.AreaTypes(areaTypes), request.getUnit()));

        if (containsError(closestAreasRS.getResponseMessage())) {
            return addErrorMessage(response, closestAreasRS.getResponseMessage());
        }
        response.setClosestAreas(closestAreasRS.getClosestAreas());

        return next.handleRequest(request, response);
    }

    private SpatialEnrichmentRS addErrorMessage(SpatialEnrichmentRS response, ResponseMessageType responseMessage) {
        response.setResponseMessage(responseMessage);
        return response;
    }

    @Override
    public EnrichmentDto handleRequest(double lat, double lon, int crs, String unit, List<String> areaTypes, List<String> locationTypes, EnrichmentDto enrichmentDto) {
        List<ClosestAreaDto> closestAreas = getClosestAreasRest(lat, lon, crs, unit, locationTypes);
        enrichmentDto.setClosestAreas(closestAreas);
        return next.handleRequest(lat, lon, crs, unit, areaTypes, locationTypes, enrichmentDto);
    }

    @Override
    @Interceptors(value = ExceptionHandlerInterceptor.class)
    public ClosestAreaSpatialRS getClosestAreas(ClosestAreaSpatialRQ request) {
        Point point = convertToPointInWGS84(request.getPoint());
        MeasurementUnit measurementUnit = MeasurementUnit.getMeasurement(request.getUnit().name());

        Map<String, String> areaType2TableName = getAreaType2TableNameMap();
        List<ClosestAreaEntry> closestAreas = newArrayList();
        for (AreaType areaType : request.getAreaTypes().getAreaTypes()) {
            String areaDbTable = areaType2TableName.get(areaType.value());

            List<ClosestAreaDto> closestAreaList = repository.findClosestArea(point, measurementUnit, areaDbTable);
            validateResponse(closestAreaList);

            ClosestAreaDto closestAreaDto = closestAreaList.get(0);
            if (closestAreaDto != null) {
                ClosestAreaEntry closestAreaEntry = new ClosestAreaEntry(closestAreaDto.getId(), areaType, closestAreaDto.getDistance(), request.getUnit());
                closestAreas.add(closestAreaEntry);
            }
        }

        return createSuccessClosestAreaResponse(new ClosestAreasType(closestAreas));
    }

    @Override
    public List<ClosestAreaDto> getClosestAreasRest(double lat, double lon, int crs, String unit, List<String> types) {
        Point point = convertToPointInWGS84(lon, lat, crs);
        MeasurementUnit measurementUnit = MeasurementUnit.getMeasurement(unit);

        List<String> areaTypes = SpatialUtils.toUpperCase(types);
        Map<String, String> areaType2TableName = getAreaType2TableNameMap();
        List<ClosestAreaDto> closestAreas = newArrayList();
        for (String areaType : areaTypes) {
            String areaDbTable = areaType2TableName.get(areaType);
            validateAreaType(areaType, areaDbTable);

            List<ClosestAreaDto> closestAreaList = repository.findClosestArea(point, measurementUnit, areaDbTable);
            validateResponse(closestAreaList);

            ClosestAreaDto closestAreaDto = closestAreaList.get(0);
            if (closestAreaDto != null) {
                closestAreaDto.setAreaType(areaType);
                closestAreaDto.setUnit(measurementUnit.name());
                closestAreas.add(closestAreaDto);
            }
        }

        return closestAreas;
    }

    private void validateAreaType(String areaType, String areaDbTable) {
        if (areaDbTable == null) {
            throw new SpatialServiceException(SpatialServiceErrors.WRONG_AREA_TYPE, areaType);
        }
    }

    private Map<String, String> getAreaType2TableNameMap() {
        List<AreaLocationTypesEntity> areas = crudService.findEntityByNamedQuery(AreaLocationTypesEntity.class, QueryNameConstants.FIND_ALL_AREAS);
        Map<String, String> areaMap = Maps.newHashMap();
        for (AreaLocationTypesEntity area : areas) {
            areaMap.put(area.getTypeName().toUpperCase(), area.getAreaDbTable());
        }
        return areaMap;
    }

    private void validateResponse(List<ClosestAreaDto> closestAreaList) {
        if (closestAreaList.size() > 1) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR);
        }
    }

    private ClosestAreaSpatialRS createSuccessClosestAreaResponse(ClosestAreasType closestAreasType) {
        return new ClosestAreaSpatialRS(createSuccessResponseMessage(), closestAreasType);
    }

}
