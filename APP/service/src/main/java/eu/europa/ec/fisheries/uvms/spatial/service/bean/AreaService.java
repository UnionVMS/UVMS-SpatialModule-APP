package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Area;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialRQ;

import java.util.List;

public interface AreaService {

    List<Area> getClosestAreas(ClosestAreaSpatialRQ request) throws ServiceException;
}
