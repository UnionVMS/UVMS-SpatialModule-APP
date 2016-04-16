package eu.europa.ec.fisheries.uvms.spatial.service;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.GenericSystemAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.UserAreaDto;

import java.util.List;

public interface SpatialService {

    List<Location> getClosestPointToPointByType(ClosestLocationSpatialRQ request) throws ServiceException;

    List<Area> getClosestAreasToPointByType(ClosestAreaSpatialRQ request) throws ServiceException;

    List<AreaExtendedIdentifierType> getAreasByPoint(AreaByLocationSpatialRQ request) throws ServiceException;

    FilterAreasSpatialRS computeAreaFilter(FilterAreasSpatialRQ filterAreasSpatialRQ) throws ServiceException;

    List<GenericSystemAreaDto> searchAreasByNameOrCode(String areaType, String filter) throws ServiceException;

    LocationDetails getLocationDetails(LocationTypeEntry locationTypeEntry) throws ServiceException;

    List<AreaDetails> getAreaDetailsByLocation(AreaTypeEntry areaTypeEntry) throws ServiceException;

    List<UserAreaDto> getUserAreaDetailsWithExtentByLocation(Coordinate coordinate, String userName) throws ServiceException;

    List<AreaDetails> getUserAreaDetailsByLocation(AreaTypeEntry areaTypeEntry, String userName) throws ServiceException;

    String calculateBuffer(Double latitude, Double longitude, Double buffer);

    String translate(Double tx, Double ty, String wkt) throws ServiceException;


}
