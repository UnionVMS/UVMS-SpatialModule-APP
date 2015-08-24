package eu.europa.ec.fisheries.uvms.spatial.service.rest;

import eu.europa.ec.fisheries.uvms.spatial.service.rest.dto.AreaDto;

import java.util.List;

/**
 * Created by Michal Kopyczok on 24-Aug-15.
 */
public interface AreaByLocationRestService {
    List<AreaDto> getAreasByLocation(double lat, double lon, int crs);
}
