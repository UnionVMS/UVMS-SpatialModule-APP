package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialEnrichmentRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialEnrichmentRS;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.AreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.ClosestAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.ClosestLocationDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.EnrichmentDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Michal Kopyczok on 01-Sep-15.
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
    public EnrichmentDto getSpatialEnrichment(double lat, double lon, int crs, String unit, List<String> areaTypes, List<String> locationTypes) {
        List<AreaDto> areasByLocation = areaByLocationService.getAreasByLocationRest(lat, lon, crs);
        List<ClosestAreaDto> closestAreas = closestAreaService.getClosestAreasRest(lat, lon, crs, unit, areaTypes);
        List<ClosestLocationDto> closestLocations = closestLocationService.getClosestLocationsRest(lat, lon, crs, unit, locationTypes);

        return new EnrichmentDto(areasByLocation, closestAreas, closestLocations);
    }

    @Override
    public SpatialEnrichmentRS getSpatialEnrichment(SpatialEnrichmentRQ spatialEnrichmentRQ) {
        throw new NotImplementedException("Not implemented yet");
    }
}
