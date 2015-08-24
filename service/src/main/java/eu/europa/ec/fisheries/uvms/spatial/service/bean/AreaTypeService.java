package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeSpatialRS;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaRequest;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaResponse;

public interface AreaTypeService {
    AreaTypeSpatialRS getAreaTypes();

    /**
     * Returns a list of the closest area types and distance from the specified position
     *
     * @param  request a location point
     * @return A list of the closest area types
     */
    ClosestAreaResponse getClosestArea(ClosestAreaRequest request);
}
