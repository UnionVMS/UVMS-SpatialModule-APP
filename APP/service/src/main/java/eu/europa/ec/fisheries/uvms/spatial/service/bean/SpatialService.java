package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Area;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaByLocationSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaExtendedIdentifierType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestLocationSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.FilterAreasSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.FilterAreasSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Location;

import java.util.List;

public interface SpatialService {

    List<Location> getClosestLocationByLocationType(ClosestLocationSpatialRQ request) throws ServiceException;

    List<AreaExtendedIdentifierType> getAreaTypesByLocation(AreaByLocationSpatialRQ request);

    List<Area> getClosestAreas(ClosestAreaSpatialRQ request) throws ServiceException;

    FilterAreasSpatialRS filterAreas(FilterAreasSpatialRQ filterAreasSpatialRQ) throws ServiceException;

}
