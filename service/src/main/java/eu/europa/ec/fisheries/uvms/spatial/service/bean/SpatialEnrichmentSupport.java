package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialEnrichmentRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialEnrichmentRS;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.EnrichmentDto;

import java.util.List;

/**
 * Created by Michal Kopyczok on 01-Sep-15.
 */
public interface SpatialEnrichmentSupport {

    EnrichmentDto handleRequest(double lat, double lon, int crs, String unit, List<String> areaTypes, List<String> locationTypes, EnrichmentDto enrichmentDto);

    SpatialEnrichmentRS handleRequest(SpatialEnrichmentRQ request, SpatialEnrichmentRS response);

}
