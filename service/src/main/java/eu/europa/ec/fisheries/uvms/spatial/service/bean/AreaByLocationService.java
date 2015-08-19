package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.service.exception.CommonGenericDAOException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaByLocationSpatialRS;

/**
 * Created by kopyczmi on 18-Aug-15.
 */
public interface AreaByLocationService {
    AreaByLocationSpatialRS getAreasByLocation(double lat, double lon, int crs) throws CommonGenericDAOException;
}
