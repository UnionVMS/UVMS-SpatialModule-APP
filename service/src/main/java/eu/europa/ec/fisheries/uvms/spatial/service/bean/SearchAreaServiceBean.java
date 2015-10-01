package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;

import eu.europa.ec.fisheries.uvms.spatial.repository.SpatialRepository;
import lombok.extern.slf4j.Slf4j;

@Stateless
@Local(SearchAreaService.class)
@Transactional
@Slf4j
public class SearchAreaServiceBean extends SpatialServiceBean implements SearchAreaService {
	
    @EJB
    private SpatialRepository repository;
    
	@Override
	public List<Map<String, String>> getAreasByFilter(String areaType, String filter) {
		validateAreaType(areaType);
		List<Map<String, String>> mapList = repository.findAreaByFilter(areaType, filter);
		return mapList;
	}
	
	private void validateAreaType(String areaType) {
		getAreaLocationType(areaType);
	}

}
