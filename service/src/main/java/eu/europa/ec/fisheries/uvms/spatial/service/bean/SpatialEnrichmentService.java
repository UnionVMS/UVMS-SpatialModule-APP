package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialEnrichmentRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialEnrichmentRS;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.EnrichmentDto;

import java.util.List;

public interface SpatialEnrichmentService {

    SpatialEnrichmentRS getSpatialEnrichment(SpatialEnrichmentRQ spatialEnrichmentRQ);

    EnrichmentDto getSpatialEnrichment(double lat, double lon, int crs, String unit, List<String> areaTypes, List<String> locationTypes);
}
