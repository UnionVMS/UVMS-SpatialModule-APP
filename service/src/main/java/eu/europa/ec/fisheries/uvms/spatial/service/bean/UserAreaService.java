package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import java.util.List;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Coordinate;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UserAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UserAreaGeomDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UserAreaLayerDto;

public interface UserAreaService {

	boolean storeUserArea(UserAreaGeomDto userAreaDto, String remoteUser, String scopeName) throws ServiceException;

	UserAreaLayerDto getUserAreaLayerDefination(String userName, String scopeName);

	List<UserAreaDto> getUserAreaDetails(Coordinate coordinate, String userName, String scopeName);
	
	List<UserAreaDto> searchUserAreasByCriteria(String userName, String scopeName, String searchCriteria);
}
