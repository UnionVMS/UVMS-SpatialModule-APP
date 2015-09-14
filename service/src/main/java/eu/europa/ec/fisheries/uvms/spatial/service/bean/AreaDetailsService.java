package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetailsSpatialRequest;

public interface AreaDetailsService {

    AreaDetails getAreaDetails(AreaDetailsSpatialRequest request);

}
