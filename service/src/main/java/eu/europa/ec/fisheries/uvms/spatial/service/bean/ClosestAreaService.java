package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialRS;

public interface ClosestAreaService {

    ClosestAreaSpatialRS getClosestArea(ClosestAreaSpatialRQ request);

}
