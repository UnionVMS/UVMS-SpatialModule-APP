package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetailsSpatialRequest;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;

public interface AreaDetailsService {

    AreaDetails getAreaDetails(AreaDetailsSpatialRequest request);
    
    AreaDetails getAreaDetails(AreaTypeEntry request);

}
