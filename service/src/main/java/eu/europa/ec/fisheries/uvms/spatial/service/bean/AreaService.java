package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.schemas.GetAreaTypesSpatialRS;
import eu.schemas.GetAreasByLocationSpatialRS;

/**
 * Created by kopyczmi on 03-Aug-15.
 */
public interface AreaService {
    GetAreaTypesSpatialRS getAreaTypes();

    GetAreasByLocationSpatialRS getAreasByLocation(double lat, double lon, int crs);
}
