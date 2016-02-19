package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Area;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.ClosestAreaDto;

import java.util.List;

public interface ClosestAreaService {

    List<ClosestAreaDto> getClosestAreas(double lat, double lon, int crs, String unit, List<String> areaTypes);

    List<Area> getClosestAreas(ClosestAreaSpatialRQ request);
}
