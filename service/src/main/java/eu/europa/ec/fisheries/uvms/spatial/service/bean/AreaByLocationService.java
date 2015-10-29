package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaByLocationSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaExtendedIdentifierType;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.AreaExtendedIdentifierDto;

import java.util.List;

public interface AreaByLocationService {
    List<AreaExtendedIdentifierType> getAreaTypesByLocation(AreaByLocationSpatialRQ request);

    List<AreaExtendedIdentifierDto> getAreaTypesByLocation(double lat, double lon, int crs);
}
