package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialRequest;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialResponse;

public interface AreaService {

    /**
     * Returns a list of the closest area types and distance from the specified position
     *
     * @param request a location point
     * @return A list of the closest area types
     */
    ClosestAreaSpatialResponse getClosestArea(ClosestAreaSpatialRequest request);

}
