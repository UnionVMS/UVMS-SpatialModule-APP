package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;

import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationProperty;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationTypeEntry;
import lombok.extern.slf4j.Slf4j;

@Stateless
@Local(LocationDetailsService.class)
@Transactional
@Slf4j
public class LocationDetailsServiceBean extends SpatialServiceBean implements LocationDetailsService {
	
    @Override
    public LocationDetails getLocationDetails(LocationTypeEntry request) {
    	AreaLocationTypesEntity locationType = getAreaLocationType(request.getLocationType());
    	Map<String, String> properties;
    	if (request.getId() != null) {
    		validateId(request.getId());
    		properties = getAreaLocationDetailsById(Long.parseLong(request.getId()), locationType);
    	} else {
    		properties = getAreaLocationDetailsByCoordinates(request, locationType);
    	}
        return createLocationDetailsResponse(properties, request);
    }

    private LocationDetails createLocationDetailsResponse(Map<String, String> properties, LocationTypeEntry locationType) {
        List<LocationProperty> locationProperties = new ArrayList<LocationProperty>();
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            LocationProperty locationProperty = new LocationProperty();
            locationProperty.setPropertyName(entry.getKey());
            locationProperty.setPropertyValue(entry.getValue());
            locationProperties.add(locationProperty);
        }
        LocationDetails locationDetails = new LocationDetails();
        locationDetails.setLocationType(locationType);
        locationDetails.getLocationProperty().addAll(locationProperties);
        return locationDetails;
    }
}