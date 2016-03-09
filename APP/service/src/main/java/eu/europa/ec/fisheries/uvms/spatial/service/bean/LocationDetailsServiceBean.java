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
    public LocationDetails getLocationDetails(LocationTypeEntry locationTypeEntry) {
    	AreaLocationTypesEntity locationType = getAreaLocationType(locationTypeEntry.getLocationType());
    	Map<String, Object> properties;
    	if (locationTypeEntry.getId() != null) {
    		validateId(locationTypeEntry.getId());
    		properties = getAreaLocationDetailsById(Long.parseLong(locationTypeEntry.getId()), locationType);
    	} else {
    		properties = getAreaLocationDetailsByCoordinates(locationTypeEntry, locationType);
    	}
        return createLocationDetailsResponse(properties, locationTypeEntry);
    }

    private LocationDetails createLocationDetailsResponse(Map<String, Object> properties, LocationTypeEntry locationType) {
        List<LocationProperty> locationProperties = new ArrayList<LocationProperty>();
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            LocationProperty locationProperty = new LocationProperty();
            locationProperty.setPropertyName(entry.getKey());
            locationProperty.setPropertyValue(entry.getValue()!=null?entry.getValue().toString():null);
            locationProperties.add(locationProperty);
        }
        LocationDetails locationDetails = new LocationDetails();
        locationDetails.setLocationType(locationType);
        locationDetails.getLocationProperties().addAll(locationProperties);
        return locationDetails;
    }
}