package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetailsSpatialRequest;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetailsSpatialResponse;

public interface AreaDetailsService {

    /**
     * API getAreaDetails. This service provides Area Details for a system area or user based on the input type.
     * In response it provides list of Area Property object which contains every column of the requested entity as a key value pair
     * 
     * @param request {@link AreaDetailsSpatialRequest}
     * @return {@link AreaDetailsSpatialResponse}
     * 
     */
    AreaDetailsSpatialResponse getAreaDetails(AreaDetailsSpatialRequest request);

}
