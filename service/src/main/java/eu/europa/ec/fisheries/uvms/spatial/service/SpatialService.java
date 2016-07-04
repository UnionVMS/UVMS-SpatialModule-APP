package eu.europa.ec.fisheries.uvms.spatial.service;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.GenericSystemAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.SystemAreaNamesDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.UserAreaDto;

import java.util.List;

public interface SpatialService {

    List<Location> getClosestPointToPointByType(ClosestLocationSpatialRQ request) throws ServiceException;

    List<Area> getClosestArea(ClosestAreaSpatialRQ request) throws ServiceException;

    List<AreaExtendedIdentifierType> getAreasByPoint(AreaByLocationSpatialRQ request) throws ServiceException;

    FilterAreasSpatialRS computeAreaFilter(FilterAreasSpatialRQ filterAreasSpatialRQ) throws ServiceException;

    /**
     * This API search for all the AREAS that has a matching <code>NAME</> or <code>CODE</> by Area Type and Filter in the input
     *
     * @param areaType Area Type to search
     * @param filter Filter code
     * @return Collection of Gid, code, area Type, extent of the area and Name
     * @throws ServiceException Exception
     */
    List<GenericSystemAreaDto> searchAreasByNameOrCode(String areaType, String filter) throws ServiceException;

    /**
     * <p>This API search for all the unique <code>AREA CODES<code/> and corresponding <code>AREA NAMES<code/> by Area Type and Filter text received.
     * It groups the Area Code and the mapped Areas Names and combines with the extent of the areas.
     * <p/>
     * @param areaType Area Type to search
     * @param filter Filter code
     * @return Collection of area Names, combined extend and search code
     * @throws ServiceException Exception
     *
     * @see SpatialService#searchAreasByNameOrCode(String, String)
     */
    List<SystemAreaNamesDto> searchAreasByCode(String areaType, String filter) throws ServiceException;

    LocationDetails getLocationDetails(LocationTypeEntry locationTypeEntry) throws ServiceException;

    List<AreaDetails> getAreaDetailsByLocation(AreaTypeEntry areaTypeEntry) throws ServiceException;

    List<UserAreaDto> getUserAreaDetailsWithExtentByLocation(Coordinate coordinate, String userName) throws ServiceException;

    List<AreaDetails> getUserAreaDetailsByLocation(AreaTypeEntry areaTypeEntry, String userName) throws ServiceException;

    String calculateBuffer(Double latitude, Double longitude, Double buffer);

    String translate(Double tx, Double ty, String wkt) throws ServiceException;


}
