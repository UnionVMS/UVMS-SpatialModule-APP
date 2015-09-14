package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaByLocationSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.AreaDto;

import java.util.List;

/**
 * Created by kopyczmi on 18-Aug-15.
 */
public interface AreaByLocationService {
    List<AreaTypeEntry> getAreaTypesByLocation(AreaByLocationSpatialRQ request);

    List<AreaDto> getAreaTypesByLocation(double lat, double lon, int crs);
}
