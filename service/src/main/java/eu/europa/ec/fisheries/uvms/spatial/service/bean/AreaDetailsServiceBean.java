package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;

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
    	return getAreaDetails(request.getAreaType());
    }
    
    @Override
    public AreaDetails getAreaDetails(AreaTypeEntry request) {
    	AreaLocationTypesEntity areaType = getAreaLocationType(request.getAreaType());
    	if (areaType.getIsSystemWide()) {
    		return getSystemAreaDetails(request, areaType);
    	} else {
    		// TODO Get area details for custom areas and User Areas
    		throw new NotImplementedException("Not implemented");
    	} 
    }
    
    private AreaDetails getSystemAreaDetails(AreaTypeEntry request, AreaLocationTypesEntity areaType) {
    	Map<String, String> properties;
    	if (request.getId() != null) {
    		validateId(request.getId());
    		properties = getAreaLocationDetailsById(Integer.parseInt(request.getId()), areaType);
    	} else {
    		properties = getAreaLocationDetailsByCoordinates(request, areaType);
    	}
    	return createAreaDetailsSpatialResponse(properties, request);
    }

    private AreaDetails createAreaDetailsSpatialResponse(Map<String, String> properties, AreaTypeEntry areaType) {
        List<AreaProperty> areaProperties = new ArrayList<AreaProperty>();
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            AreaProperty areaProperty = new AreaProperty();
            areaProperty.setPropertyName(entry.getKey());
            areaProperty.setPropertyValue(entry.getValue());
            areaProperties.add(areaProperty);
        }

        AreaDetails areaDetails = new AreaDetails();
        areaDetails.setAreaType(areaType);
        areaDetails.getAreaProperty().addAll(areaProperties);
        return areaDetails;
    }
}
