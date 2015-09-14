package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationDetailsSpatialRequest;

public interface LocationDetailsService {

	public LocationDetails getLocationDetails(LocationDetailsSpatialRequest request);

}
