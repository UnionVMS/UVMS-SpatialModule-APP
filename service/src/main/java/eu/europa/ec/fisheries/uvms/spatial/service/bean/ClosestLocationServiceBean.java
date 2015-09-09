package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.Maps;
import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.service.CrudService;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.repository.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.ClosestLocationDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.EnrichmentDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.MeasurementUnit;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.handler.ExceptionHandlerInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.handler.SpatialExceptionHandler;
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

/**
 * Created by Michal Kopyczok on 03-Sep-15.
 */
@Stateless(name = "closestLocationService")
@Local(ClosestLocationService.class)
@Transactional
@Slf4j
public class ClosestLocationServiceBean implements ClosestLocationService, SpatialEnrichmentSupport {

    @EJB
    private SpatialRepository repository;

    @EJB
    private CrudService crudService;

    @Override
    public SpatialEnrichmentRS handleRequest(SpatialEnrichmentRQ request, SpatialEnrichmentRS response) {
        if (containsError(response.getResponseMessage())) {
            return response;
        }

        List<LocationType> locationTypes = request.getLocationTypes().getLocationType();
        ClosestLocationSpatialRS closestLocationsRS = getClosestLocations(new ClosestLocationSpatialRQ(request.getPoint(), new ClosestLocationSpatialRQ.LocationTypes(locationTypes), request.getUnit()));

        if (containsError(closestLocationsRS.getResponseMessage())) {
            return addErrorMessage(response, closestLocationsRS.getResponseMessage());
        }
        response.setClosestLocations(closestLocationsRS.getClosestLocations());
        
        return response;
    }

    private SpatialEnrichmentRS addErrorMessage(SpatialEnrichmentRS response, ResponseMessageType responseMessage) {
        response.setResponseMessage(responseMessage);
        return response;
    }

    @Override
    public EnrichmentDto handleRequest(double lat, double lon, int crs, String unit, List<String> areaTypes, List<String> locationTypes, EnrichmentDto enrichmentDto) {
        List<ClosestLocationDto> closestLocations = getClosestLocationsRest(lat, lon, crs, unit, locationTypes);
        enrichmentDto.setClosestLocations(closestLocations);
        return enrichmentDto;
    }

    @Override
    @SpatialExceptionHandler(responseType = ClosestLocationSpatialRS.class)
    @Interceptors(value = ExceptionHandlerInterceptor.class)
    public ClosestLocationSpatialRS getClosestLocations(ClosestLocationSpatialRQ request) {
        Point point = convertToPointInWGS84(request.getPoint());
        MeasurementUnit measurementUnit = MeasurementUnit.getMeasurement(request.getUnit().name());

        Map<String, String> areaType2TableName = getLocationType2TableNameMap();
        List<ClosestLocationEntry> closestLocations = newArrayList();
        for (LocationType locationType : request.getLocationTypes().getLocationType()) {
            String areaDbTable = areaType2TableName.get(locationType.value());

            List<ClosestLocationDto> closestAreaList = repository.findClosestlocation(point, measurementUnit, areaDbTable);
            validateResponse(closestAreaList);

            ClosestLocationDto closestLocationDto = closestAreaList.get(0);
            if (closestLocationDto != null) {
                ClosestLocationEntry closestLocationEntry = new ClosestLocationEntry(closestLocationDto.getId(), locationType, closestLocationDto.getDistance(), request.getUnit());
                closestLocations.add(closestLocationEntry);
            }
        }

        return createSuccessClosestLocationResponse(new ClosestLocationsType(closestLocations));
    }

    @Override
    public List<ClosestLocationDto> getClosestLocationsRest(double lat, double lon, int crs, String unit, List<String> locations) {
        Point point = convertToPointInWGS84(lon, lat, crs);
        MeasurementUnit measurementUnit = MeasurementUnit.getMeasurement(unit);

        List<String> locationTypes = SpatialUtils.toUpperCase(locations);
        Map<String, String> areaType2TableName = getLocationType2TableNameMap();
        List<ClosestLocationDto> closestLocations = newArrayList();
        for (String locationType : locationTypes) {
            String areaDbTable = areaType2TableName.get(locationType);
            validateLocationType(locationType, areaDbTable);

            List<ClosestLocationDto> closestAreaList = repository.findClosestlocation(point, measurementUnit, areaDbTable);
            validateResponse(closestAreaList);

            ClosestLocationDto closestLocationDto = closestAreaList.get(0);
            if (closestLocationDto != null) {
                closestLocationDto.setLocationType(locationType);
                closestLocationDto.setUnit(measurementUnit.name());
                closestLocations.add(closestLocationDto);
            }
        }

        return closestLocations;
    }

    private void validateLocationType(String locationType, String areaDbTable) {
        if (areaDbTable == null) {
            throw new SpatialServiceException(SpatialServiceErrors.WRONG_LOCATION_TYPE, locationType);
        }
    }

    private Map<String, String> getLocationType2TableNameMap() {
        List<AreaLocationTypesEntity> locations = crudService.findEntityByNamedQuery(AreaLocationTypesEntity.class, QueryNameConstants.FIND_ALL_LOCATIONS);
        Map<String, String> locationMap = Maps.newHashMap();
        for (AreaLocationTypesEntity location : locations) {
            locationMap.put(location.getTypeName().toUpperCase(), location.getAreaDbTable());
        }
        return locationMap;
    }

    private void validateResponse(List<ClosestLocationDto> closestLocationList) {
        if (closestLocationList.size() > 1) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR);
        }
    }

    private ClosestLocationSpatialRS createSuccessClosestLocationResponse(ClosestLocationsType closestLocationsType) {
        return new ClosestLocationSpatialRS(createSuccessResponseMessage(), closestLocationsType);
    }
}
