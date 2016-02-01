package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestLocationSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Location;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.ClosestLocationDto;

import java.util.List;

public interface ClosestLocationService {
    List<Location> getClosestLocations(ClosestLocationSpatialRQ request);

    List<ClosestLocationDto> getClosestLocations(double lat, double lon, int crs, String unit, List<String> locationTypes);
}
