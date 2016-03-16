package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.area.SystemAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;

import java.util.List;

public interface SpatialService {

    List<Location> getClosestLocationByLocationType(ClosestLocationSpatialRQ request) throws ServiceException;

    List<AreaExtendedIdentifierType> getAreaTypesByLocation(AreaByLocationSpatialRQ request);

    List<Area> getClosestAreas(ClosestAreaSpatialRQ request) throws ServiceException;

    FilterAreasSpatialRS filterAreas(FilterAreasSpatialRQ filterAreasSpatialRQ) throws ServiceException;

    List<SystemAreaDto> getAreasByFilter(String tableName, String filter) throws ServiceException;

    LocationDetails getLocationDetails(LocationTypeEntry locationTypeEntry) throws ServiceException;

}
