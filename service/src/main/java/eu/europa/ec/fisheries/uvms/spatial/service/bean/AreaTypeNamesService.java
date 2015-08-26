package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeNamesSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialRS;

import java.util.List;

public interface AreaTypeNamesService {
    AreaTypeNamesSpatialRS getAreaTypesQueue();

    List<String> getAreaTypesRest();

    /**
     * Returns a list of the closest area types and distance from the specified position
     *
     * @param request a location point
     * @return A list of the closest area types
     */
    ClosestAreaSpatialRS getClosestArea(ClosestAreaSpatialRQ request);
}
