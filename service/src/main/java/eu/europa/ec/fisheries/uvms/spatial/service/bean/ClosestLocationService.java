package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestLocationSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestLocationSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.ClosestLocationDto;

import java.util.List;

/**
 * Created by Michal Kopyczok on 03-Sep-15.
 */
public interface ClosestLocationService extends SpatialEnrichmentSupport {
    ClosestLocationSpatialRS getClosestLocations(ClosestLocationSpatialRQ request);

    List<ClosestLocationDto> getClosestLocationsRest(double lat, double lon, int crs, String unit, List<String> locationTypes);
}
