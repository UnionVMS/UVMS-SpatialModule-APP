package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import java.util.List;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Coordinate;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.UserAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.geojson.UserAreaGeoJsonDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UserAreaLayerDto;

public interface UserAreaService {

	UserAreaLayerDto getUserAreaLayerDefination(String userName, String scopeName);

	List<UserAreaDto> getUserAreaDetailsWithExtentByLocation(Coordinate coordinate, String userName);

	List<AreaDetails> getUserAreaDetailsByLocation(AreaTypeEntry areaTypeEntry, String userName);

	AreaDetails getUserAreaDetailsWithExtentById(AreaTypeEntry areaTypeEntry, String userName, String scopeName) throws ServiceException;

	List<AreaDetails> getUserAreaDetailsById(AreaTypeEntry areaTypeEntry, String userName, String scopeName) throws ServiceException;

	AreaDetails getUserAreaDetailsWithExtentById(AreaTypeEntry areaTypeEntry, String userName, boolean isPowerUser, String scopeName) throws ServiceException;

	List<AreaDetails> getUserAreaDetailsById(AreaTypeEntry areaTypeEntry, String userName, boolean isPowerUser, String scopeName) throws ServiceException;

	List<UserAreaDto> searchUserAreasByCriteria(String userName, String scopeName, String searchCriteria, boolean isPowerUser);

	long storeUserArea(UserAreaGeoJsonDto userAreaDto, String userName) throws ServiceException;

	long updateUserArea(UserAreaGeoJsonDto userAreaDto, String userName, String scopeName) throws ServiceException;

	void deleteUserArea(Long userAreaId, String userName, String scopeName) throws ServiceException;

	long updateUserArea(UserAreaGeoJsonDto userAreaDto, String userName, boolean isPowerUser, String scopeName) throws ServiceException;

	void deleteUserArea(Long userAreaId, String userName, boolean isPowerUser, String scopeName) throws ServiceException;

	List<String> getUserAreaTypes(String userName, String scopeName, boolean isPowerUser) throws ServiceException;
}
