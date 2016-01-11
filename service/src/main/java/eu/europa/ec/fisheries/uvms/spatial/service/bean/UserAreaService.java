package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import java.util.List;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.entity.UserAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Coordinate;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UserAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UserAreaGeomDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UserAreaLayerDto;

public interface UserAreaService {

	UserAreaLayerDto getUserAreaLayerDefination(String userName, String scopeName);

	List<UserAreaDto> getUserAreaDetailsWithExtentByLocation(Coordinate coordinate, String userName, String scopeName);

	List<AreaDetails> getUserAreaDetailsByLocation(AreaTypeEntry areaTypeEntry, String userName, String scopeName);

	AreaDetails getUserAreaDetailsWithExtentById(AreaTypeEntry areaTypeEntry, String userName) throws ServiceException;

	List<AreaDetails> getUserAreaDetailsById(AreaTypeEntry areaTypeEntry, String userName) throws ServiceException;

	List<UserAreaDto> searchUserAreasByCriteria(String userName, String scopeName, String searchCriteria);

	boolean storeUserArea(UserAreaGeomDto userAreaDto, String remoteUser) throws ServiceException;

	boolean updateUserArea(UserAreaGeomDto userAreaDto, String userName) throws ServiceException;

	void deleteUserArea(Long userAreaId, String userName) throws ServiceException;
}
