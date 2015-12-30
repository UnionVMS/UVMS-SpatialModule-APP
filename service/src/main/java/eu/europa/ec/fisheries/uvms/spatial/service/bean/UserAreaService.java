package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import java.util.List;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Coordinate;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UserAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UserAreaGeomDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UserAreaLayerDto;

public interface UserAreaService {

	UserAreaLayerDto getUserAreaLayerDefination(String userName, String scopeName);

	List<UserAreaDto> getUserAreaDetails(Coordinate coordinate, String userName, String scopeName);

	List<UserAreaDto> searchUserAreasByCriteria(String userName, String scopeName, String searchCriteria);

	boolean storeUserArea(UserAreaGeomDto userAreaDto, String remoteUser, String scopeName) throws ServiceException;

	boolean updateUserArea(UserAreaGeomDto userAreaDto, String userName, String scopeName) throws ServiceException;

	void deleteUserArea(Long userAreaId, String userName, String scopeName) throws ServiceException;
}
