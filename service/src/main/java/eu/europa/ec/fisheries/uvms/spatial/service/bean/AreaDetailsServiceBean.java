package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import static eu.europa.ec.fisheries.uvms.spatial.util.ColumnAliasNameHelper.getFieldMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.NotImplementedException;

import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetailsSpatialRequest;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaProperty;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import lombok.extern.slf4j.Slf4j;

@Stateless
@Local(AreaDetailsService.class)
@Transactional
@Slf4j
public class AreaDetailsServiceBean extends SpatialServiceBean implements AreaDetailsService {

    @Override
    public AreaDetails getAreaDetails(AreaDetailsSpatialRequest request) {
    	return getAreaDetailsById(request.getAreaType());
    }
    
    @Override
    public AreaDetails getAreaDetailsById(AreaTypeEntry areaTypeEntry) {
    	AreaLocationTypesEntity areaType = getAreaLocationType(areaTypeEntry.getAreaType());
    	if (areaType.getIsSystemWide()) {
    		return getSystemAreaDetails(areaTypeEntry, areaType);
    	} else {
    		// TODO Get area details for custom areas and User Areas
    		throw new NotImplementedException("Not implemented");
    	} 
    }    
    
    @Override
    public List<AreaDetails> getAreaDetailsByLocation(AreaTypeEntry areaTypeEntry) {
    	AreaLocationTypesEntity areaType = getAreaLocationType(areaTypeEntry.getAreaType());
    	if (areaType.getIsSystemWide()) {
    		List allAreas = getAllAreaByCoordinates(areaTypeEntry, areaType);
    		return getAllAreaDetails(allAreas, areaTypeEntry);
    	} else {
    		// TODO Get area details for custom areas and User Areas
    		throw new NotImplementedException("Not implemented");
    	}
    }
    
    private List<AreaDetails> getAllAreaDetails(List allAreas, AreaTypeEntry areaTypeEntry) {
    	List<AreaDetails> areaDetailsList = new ArrayList<AreaDetails>();
		for (int i = 0 ; i < allAreas.size() ; i ++) {
			Map<String, String> properties = getFieldMap(allAreas.get(i));
			areaDetailsList.add(createAreaDetailsSpatialResponse(properties, areaTypeEntry));
		}
		return areaDetailsList;
	}

	private AreaDetails getSystemAreaDetails(AreaTypeEntry areaTypeEntry, AreaLocationTypesEntity areaType) {
		validateId(areaTypeEntry.getId());
    	Map<String, String> properties = getAreaLocationDetailsById(Integer.parseInt(areaTypeEntry.getId()), areaType);
    	return createAreaDetailsSpatialResponse(properties, areaTypeEntry);
    }

    private AreaDetails createAreaDetailsSpatialResponse(Map<String, String> properties, AreaTypeEntry areaTypeEntry) {
        List<AreaProperty> areaProperties = Lists.newArrayList();
		for (Map.Entry<String, String> entry : properties.entrySet()) {
            AreaProperty areaProperty = new AreaProperty();
            areaProperty.setPropertyName(entry.getKey());
            areaProperty.setPropertyValue(entry.getValue());
            areaProperties.add(areaProperty);
        }

        AreaDetails areaDetails = new AreaDetails();
        areaDetails.setAreaType(areaTypeEntry);
        areaDetails.getAreaProperties().addAll(areaProperties);
        return areaDetails;
    }
}
