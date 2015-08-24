package eu.europa.ec.fisheries.uvms.spatial.service.queue;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezSpatialRS;

/**
 * //TODO create test
 */
public interface EezQueueService {

    public EezSpatialRS getEezById(EezSpatialRQ getEezSpatialRQ);

}
