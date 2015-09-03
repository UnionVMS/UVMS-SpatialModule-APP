package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetailsSpatialRequest;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetailsSpatialResponse;

public interface AreaDetailsService {	
	
	public AreaDetailsSpatialResponse getAreaDetails(final AreaDetailsSpatialRequest request);

}
