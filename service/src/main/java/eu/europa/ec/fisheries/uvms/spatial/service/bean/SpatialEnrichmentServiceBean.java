package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialEnrichmentRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialEnrichmentRS;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.EnrichmentDto;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.List;

import static eu.europa.ec.fisheries.uvms.util.ModelUtils.containsError;
import static eu.europa.ec.fisheries.uvms.util.ModelUtils.createSuccessResponseMessage;

/**
 * Created by Cegeka on 01-Sep-15.
 */
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
    public SpatialEnrichmentRS getSpatialEnrichment(SpatialEnrichmentRQ spatialEnrichmentRQ) {
        SpatialEnrichmentRS spatialEnrichmentRS = areaByLocationService.handleSpatialEnrichment(spatialEnrichmentRQ, createSuccessSpatialEnrichmentResponse());
        if (containsError(spatialEnrichmentRS.getResponseMessage())) {
            return spatialEnrichmentRS;
        }

        spatialEnrichmentRS = closestAreaService.handleSpatialEnrichment(spatialEnrichmentRQ, spatialEnrichmentRS);
        if (containsError(spatialEnrichmentRS.getResponseMessage())) {
            return spatialEnrichmentRS;
        }

        return closestLocationService.handleSpatialEnrichment(spatialEnrichmentRQ, spatialEnrichmentRS);
    }

    @Override
    public EnrichmentDto getSpatialEnrichment(double lat, double lon, int crs, String unit, List<String> areaTypes, List<String> locationTypes) {
        EnrichmentDto enrichmentDto = areaByLocationService.handleSpatialEnrichment(lat, lon, crs, unit, areaTypes, locationTypes, new EnrichmentDto());
        enrichmentDto = closestAreaService.handleSpatialEnrichment(lat, lon, crs, unit, areaTypes, locationTypes, enrichmentDto);
        return closestLocationService.handleSpatialEnrichment(lat, lon, crs, unit, areaTypes, locationTypes, enrichmentDto);
    }

    private SpatialEnrichmentRS createSuccessSpatialEnrichmentResponse() {
        return new SpatialEnrichmentRS(createSuccessResponseMessage(), null, null, null);
    }
}
