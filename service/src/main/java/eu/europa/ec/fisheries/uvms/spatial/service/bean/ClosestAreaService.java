package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.ClosestAreaDto;

import java.util.List;

public interface ClosestAreaService extends SpatialEnrichmentSupport{

    ClosestAreaSpatialRS getClosestAreas(ClosestAreaSpatialRQ request);

    List<ClosestAreaDto> getClosestAreasRest(double lat, double lon, int crs, String unit, List<String> areaTypes);
}
