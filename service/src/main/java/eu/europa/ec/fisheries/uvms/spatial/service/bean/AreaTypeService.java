package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.GetAreaTypesSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.GetAreasByLocationSpatialRS;

/**
 * Created by kopyczmi on 03-Aug-15.
 */
public interface AreaTypeService {
    GetAreaTypesSpatialRS getAreaTypes();
}
