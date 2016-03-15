package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationDetailsSpatialRequest;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationTypeEntry;

public interface LocationDetailsService {	
	LocationDetails getLocationDetails(LocationTypeEntry locationTypeEntry) throws ServiceException;
}
