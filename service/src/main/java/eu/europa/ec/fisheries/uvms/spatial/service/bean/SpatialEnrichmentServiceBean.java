package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.AreaIdentifierDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.ClosestAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.ClosestLocationDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.EnrichmentDto;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.List;

@Stateless
@Local(SpatialEnrichmentService.class)
@Transactional
@Slf4j
public class SpatialEnrichmentServiceBean implements SpatialEnrichmentService {

    @EJB
    private AreaByLocationService areaByLocationService;

    @EJB
    private ClosestAreaService closestAreaService;

    @EJB
    private ClosestLocationService closestLocationService;

    @Override
    public SpatialEnrichmentRS getSpatialEnrichment(SpatialEnrichmentRQ request) {

        AreaByLocationSpatialRQ areaByLocationSpatialRQ = new AreaByLocationSpatialRQ();
        areaByLocationSpatialRQ.setPoint(request.getPoint());
        List<AreaTypeEntry> areaTypesByLocation = areaByLocationService.getAreaTypesByLocation(areaByLocationSpatialRQ);

        List<AreaType> areaTypes = request.getAreaTypes().getAreaTypes();
        ClosestAreaSpatialRQ closestAreaSpatialRQ = new ClosestAreaSpatialRQ();
        ClosestAreaSpatialRQ.AreaTypes types = new ClosestAreaSpatialRQ.AreaTypes();
        types.getAreaTypes().addAll(areaTypes);
        closestAreaSpatialRQ.setAreaTypes(types);
        closestAreaSpatialRQ.setUnit(request.getUnit());
        closestAreaSpatialRQ.setPoint(request.getPoint());

        List<Area> closestAreas = closestAreaService.getClosestAreas(closestAreaSpatialRQ);

        List<LocationType> locationTypes = request.getLocationTypes().getLocationTypes();
        ClosestLocationSpatialRQ closestLocationSpatialRQ = new ClosestLocationSpatialRQ();
        closestLocationSpatialRQ.setPoint(request.getPoint());
        closestLocationSpatialRQ.setUnit(request.getUnit());
        ClosestLocationSpatialRQ.LocationTypes locationTp = new ClosestLocationSpatialRQ.LocationTypes();
        locationTp.getLocationTypes().addAll(locationTypes);
        closestLocationSpatialRQ.setLocationTypes(locationTp);

        List<Location> closestLocations = closestLocationService.getClosestLocations(closestLocationSpatialRQ);

        SpatialEnrichmentRS response = new SpatialEnrichmentRS();

        if(areaTypesByLocation != null){
            AreasByLocationType areasByLocationType = new AreasByLocationType();
            areasByLocationType.getAreas().addAll(areaTypesByLocation);
            response.setAreasByLocation(areasByLocationType);
        }

        if (closestAreas != null) {
            ClosestAreasType closestAreasType = new ClosestAreasType();
            closestAreasType.getClosestAreas().addAll(closestAreas);
            response.setClosestAreas(closestAreasType);
        }

        if (closestLocations != null){
            ClosestLocationsType locationType = new ClosestLocationsType();
            locationType.getClosestLocations().addAll(closestLocations);
            response.setClosestLocations(locationType);
        }

        return response;
    }

    @Override
    public EnrichmentDto getSpatialEnrichment(double lat, double lon, int crs, String unit, List<String> areaTypes, List<String> locationTypes) {

        List<AreaIdentifierDto> areasByLocation = areaByLocationService.getAreaTypesByLocation(lat, lon, crs);
        List<ClosestAreaDto> closestAreas = closestAreaService.getClosestAreas(lat, lon, crs, unit, areaTypes);
        List<ClosestLocationDto> closestLocations = closestLocationService.getClosestLocations(lat, lon, crs, unit, locationTypes);
        EnrichmentDto dto = new EnrichmentDto();
        dto.setAreasByLocation(areasByLocation);
        dto.setClosestAreas(closestAreas);
        dto.setClosestLocations(closestLocations);
        return dto;
    }

}
