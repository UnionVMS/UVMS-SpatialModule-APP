package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.schema.spatial.source.GetAreaTypesSpatialRS;
import eu.europa.ec.fisheries.schema.spatial.source.GetAreasByLocationRS;

/**
 * Created by kopyczmi on 03-Aug-15.
 */
public interface AreaService {
    GetAreaTypesSpatialRS getAreaTypes();

    GetAreasByLocationRS getAreasByLocation(double lat, double lon, int crs);
}
