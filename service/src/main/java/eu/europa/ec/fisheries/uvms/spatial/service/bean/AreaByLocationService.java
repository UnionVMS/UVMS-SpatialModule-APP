package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaByLocationSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaByLocationSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.AreaDto;

import java.util.List;

/**
 * Created by kopyczmi on 18-Aug-15.
 */
public interface AreaByLocationService {
    AreaByLocationSpatialRS getAreasByLocation(AreaByLocationSpatialRQ request);

    List<AreaDto> getAreasByLocationRest(double lat, double lon, int crs);
}
