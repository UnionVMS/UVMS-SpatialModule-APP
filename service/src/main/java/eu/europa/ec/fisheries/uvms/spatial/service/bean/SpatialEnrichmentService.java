package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialEnrichmentRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialEnrichmentRS;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.AreaDto;

import java.util.List;

/**
 * Created by Michal Kopyczok on 01-Sep-15.
 */
public interface SpatialEnrichmentService {

    List<AreaDto> getClosestLocationRest(double lat, double lon, int crs, List<String> locationTypes, String unit);

    SpatialEnrichmentRS getClosestLocation(SpatialEnrichmentRQ spatialEnrichmentRQ);

}
