package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.area.SystemAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.ClosestAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.ClosestLocationDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.EnrichmentDto;
import lombok.extern.slf4j.Slf4j;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.List;

@Stateless
@Local(SpatialEnrichmentService.class)
@Slf4j
public class SpatialEnrichmentServiceBean implements SpatialEnrichmentService {

    private @EJB AreaByLocationService areaByLocationService;
    private @EJB AreaService areaService;
    private @EJB ClosestLocationService closestLocationService;

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public SpatialEnrichmentRS getSpatialEnrichment(SpatialEnrichmentRQ request) throws ServiceException {

        AreaByLocationSpatialRQ areaByLocationSpatialRQ = new AreaByLocationSpatialRQ();
        areaByLocationSpatialRQ.setPoint(request.getPoint());
        List<AreaExtendedIdentifierType> areaTypesByLocation = areaByLocationService.getAreaTypesByLocation(areaByLocationSpatialRQ);

        List<AreaType> areaTypes = request.getAreaTypes().getAreaTypes();
        ClosestAreaSpatialRQ closestAreaSpatialRQ = new ClosestAreaSpatialRQ();
        ClosestAreaSpatialRQ.AreaTypes types = new ClosestAreaSpatialRQ.AreaTypes();
        types.getAreaTypes().addAll(areaTypes);
        closestAreaSpatialRQ.setAreaTypes(types);
        closestAreaSpatialRQ.setUnit(request.getUnit());
        closestAreaSpatialRQ.setPoint(request.getPoint());

        List<Area> closestAreas = areaService.getClosestAreas(closestAreaSpatialRQ);

        List<LocationType> locationTypes = request.getLocationTypes().getLocationTypes();
        ClosestLocationSpatialRQ closestLocationSpatialRQ = new ClosestLocationSpatialRQ();
        closestLocationSpatialRQ.setPoint(request.getPoint());
        closestLocationSpatialRQ.setUnit(request.getUnit());
        ClosestLocationSpatialRQ.LocationTypes locationTp = new ClosestLocationSpatialRQ.LocationTypes();
        locationTp.getLocationTypes().addAll(locationTypes);
        closestLocationSpatialRQ.setLocationTypes(locationTp);

        List<Location> closestLocations = closestLocationService.getClosestLocationByLocationType(closestLocationSpatialRQ);

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
    @Transactional
    public EnrichmentDto getSpatialEnrichment(double lat, double lon, int crs, String unit, List<String> areaTypes, List<String> locationTypes) throws ServiceException {
        List<SystemAreaDto> areasByLocation = areaByLocationService.getAreaTypesByLocation(lat, lon, crs);
        List<ClosestAreaDto> closestAreas = areaService.getClosestAreas(lat, lon, crs, unit, areaTypes);
        List<ClosestLocationDto> closestLocations = closestLocationService.getClosestLocations(lat, lon, crs, unit, locationTypes);
        EnrichmentDto dto = new EnrichmentDto();
        dto.setAreasByLocation(areasByLocation);
        dto.setClosestAreas(closestAreas);
        dto.setClosestLocations(closestLocations);
        return dto;
    }

}
