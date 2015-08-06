package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.schema.spatial.source.GetAreaTypesSpatialRS;

/**
 * Created by kopyczmi on 03-Aug-15.
 */
public interface AreaService {
    GetAreaTypesSpatialRS getAreaTypes();

    // TODO change return type
    Object getAreasByLocation(double lat, double lon, int crs);
}
