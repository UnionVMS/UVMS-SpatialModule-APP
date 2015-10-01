package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import java.util.List;
import java.util.Map;

public interface SearchAreaService {

	List<Map<String, String>> getAreasByFilter(String areaType, String filter);
}
