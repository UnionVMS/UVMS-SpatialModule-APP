package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import java.util.List;
import java.util.Map;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.area.SystemAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;

public interface SearchAreaService {

	List<SystemAreaDto> getAreasByFilter(String tableName, String filter) throws ServiceException;
	
	List<Map<String, String>> getSelectedAreaColumns(List<AreaTypeEntry> areaTypes);

 }
