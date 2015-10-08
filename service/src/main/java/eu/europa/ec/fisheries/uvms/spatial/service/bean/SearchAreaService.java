package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import java.util.List;
import java.util.Map;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;

public interface SearchAreaService {

	List<Map<String, String>> getAreasByFilter(String areaType, String filter);
	
	List<Map<String, String>> getSelectedAreaColumns(List<AreaTypeEntry> areaTypes);
}
