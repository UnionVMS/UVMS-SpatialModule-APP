package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.GetAreasByLocationSpatialRS;

/**
 * Created by kopyczmi on 18-Aug-15.
 */
public interface AreaByLocationService {
    GetAreasByLocationSpatialRS getAreasByLocation(double lat, double lon, int crs);
}
