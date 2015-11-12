package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.Maps;
import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.service.DAO;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestLocationSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Location;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationType;
import eu.europa.ec.fisheries.uvms.spatial.repository.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.ClosestLocationDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.MeasurementUnit;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import eu.europa.ec.fisheries.uvms.spatial.util.SpatialUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static eu.europa.ec.fisheries.uvms.spatial.util.SpatialUtils.convertToPointInWGS84;

/**
 * Created by Michal Kopyczok on 03-Sep-15.
 */
@Stateless
@Local(ClosestLocationService.class)
@Transactional
@Slf4j
public class ClosestLocationServiceBean implements ClosestLocationService {

    @EJB
    private SpatialRepository repository;

    @Override
    public List<Location> getClosestLocations(ClosestLocationSpatialRQ request) {
        Point point = convertToPointInWGS84(request.getPoint());
        MeasurementUnit measurementUnit = MeasurementUnit.getMeasurement(request.getUnit().name());

        Map<String, String> areaType2TableName = getLocationType2TableNameMap();
        List<Location> closestLocations = newArrayList();
        for (LocationType locationType : request.getLocationTypes().getLocationTypes()) {
            String areaDbTable = areaType2TableName.get(locationType.value());

            List<ClosestLocationDto> closestAreaList = repository.findClosestlocation(point, measurementUnit, areaDbTable);
            validateResponse(closestAreaList);

            ClosestLocationDto closestLocationDto = closestAreaList.get(0);
            if (closestLocationDto != null) {
                Location closestLocationEntry = new Location();
                closestLocationEntry.setId(closestLocationDto.getId());
                closestLocationEntry.setDistance(closestLocationDto.getDistance());
                closestLocationEntry.setUnit(request.getUnit());
                closestLocationEntry.setCode(closestLocationDto.getCode());
                closestLocationEntry.setName(closestLocationDto.getName());
                closestLocationEntry.setLocationType(locationType);
                closestLocations.add(closestLocationEntry);
            }
        }

        return closestLocations;
    }

    @Override
    public List<ClosestLocationDto> getClosestLocations(double lat, double lon, int crs, String unit, List<String> locations) {
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

    @SneakyThrows
    private Map<String, String> getLocationType2TableNameMap() {
        List<AreaLocationTypesEntity> locations = repository.findEntityByNamedQuery(QueryNameConstants.FIND_ALL_LOCATIONS);
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

}
