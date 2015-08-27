package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialRS;

public interface AreaTypeClosestArea {

    /**
     * Returns a list of the closest area types and distance from the specified position
     *
     * @param request a location point
     * @return A list of the closest area types
     */
    ClosestAreaSpatialRS getClosestArea(ClosestAreaSpatialRQ request);
}
