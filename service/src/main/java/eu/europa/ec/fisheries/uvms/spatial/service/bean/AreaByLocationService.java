package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaByLocationSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaIdentifierType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.AreaIdentifierDto;

import java.util.List;

public interface AreaByLocationService {
    List<AreaIdentifierType> getAreaTypesByLocation(AreaByLocationSpatialRQ request);

    List<AreaIdentifierDto> getAreaTypesByLocation(double lat, double lon, int crs);
}
