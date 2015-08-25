package eu.europa.ec.fisheries.uvms.spatial.service.queue;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaByLocationSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaByLocationSpatialRS;

/**
 * Created by kopyczmi on 18-Aug-15.
 */
public interface AreaByLocationQueueService {
    AreaByLocationSpatialRS getAreasByLocation(AreaByLocationSpatialRQ request);
}
