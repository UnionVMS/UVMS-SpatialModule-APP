package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationDetailsSpatialRequest;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationDetailsSpatialResponse;

public interface LocationDetailsService {
	
    /**
     * API getLocationDetails. This service provides Location Details for ID received in the input. 
     * In response it provides list of Location Property object which contains every column of the 
     * requested entity as a key value pair.
     * 
     * <br></br>
     * Currently PORT is the only supported location
     * 
     * @param request {@link LocationDetailsSpatialRequest}
     * @return {@link LocationDetailsSpatialResponse}
     * 
     */
	public LocationDetailsSpatialResponse getLocationDetails(LocationDetailsSpatialRequest request);

}
