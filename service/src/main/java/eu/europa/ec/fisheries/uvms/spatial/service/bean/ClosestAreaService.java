package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialRequest;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialResponse;

public interface ClosestAreaService {

    ClosestAreaSpatialResponse getClosestArea(ClosestAreaSpatialRequest request);

}
