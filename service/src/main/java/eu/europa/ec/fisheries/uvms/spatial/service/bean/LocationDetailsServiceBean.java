package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.ImmutableMap;
import eu.europa.ec.fisheries.uvms.service.CrudService;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.PortsEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationDetailsSpatialRequest;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationProperty;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationType;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import eu.europa.ec.fisheries.uvms.util.ColumnAliasNameHelper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

@Stateless
@Local(LocationDetailsService.class)
@Transactional
public class LocationDetailsServiceBean implements LocationDetailsService {
	
	private static final Logger LOG = LoggerFactory.getLogger(LocationDetailsServiceBean.class.getName());
	
    private ImmutableMap<String, Class> entityMap = ImmutableMap.<String, Class>builder()
            .put(LocationType.PORT.value(), PortsEntity.class).build();
	
    private static final String TYPE_NAME = "typeName";
    
    @EJB
    private CrudService crudService;

    @SuppressWarnings("unchecked")
    @Override
	public LocationDetails getLocationDetails(LocationDetailsSpatialRequest request) {
        LocationType locationType = request.getLocationType().getLocationType();
        LOG.info("Location Type name received : " + locationType.value());
        Map<String, String> parameters = newHashMap();
        parameters.put(TYPE_NAME, locationType.value().toUpperCase());
        List<AreaLocationTypesEntity> locationTypes = crudService.findEntityByNamedQuery(AreaLocationTypesEntity.class, QueryNameConstants.FIND_TYPE_BY_ID, parameters, 1);
        if (!locationTypes.isEmpty()) {
        	Map<String, String> properties = getLocationDetails(locationTypes.get(0), request.getLocationType().getId());
            return createLocationDetails(properties, request);
        } else {
            throw new SpatialServiceException(SpatialServiceErrors.INVALID_LOCATION_TYPE, locationType.value());
        }
	}

    @SuppressWarnings("unchecked")
	private Map<String, String> getLocationDetails(AreaLocationTypesEntity areaTypeEntity, String id) {
		LOG.info("Location Type entity to be retrieved : " + areaTypeEntity.getTypeName());
		if (!StringUtils.isNumeric(id)) {
			throw new SpatialServiceException(SpatialServiceErrors.INVALID_LOCATION_ID, id);
		}
		Class entityClass = entityMap.get(areaTypeEntity.getTypeName());
		Object object = crudService.findEntityById(entityClass, Integer.parseInt(id));
		if (object != null) {
			return ColumnAliasNameHelper.getFieldMap(object); // Get the alias name set in the annotation using helper
		} else {
			throw new SpatialServiceException(SpatialServiceErrors.LOCATION_NOT_FOUND, areaTypeEntity.getTypeName());
		}
	}

    private LocationDetails createLocationDetails(Map<String, String> properties, LocationDetailsSpatialRequest request) {
        List<LocationProperty> locationProperties = new ArrayList<LocationProperty>();
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            LocationProperty locationProperty = new LocationProperty();
            locationProperty.setPropertyName(entry.getKey());
            locationProperty.setPropertyValue(entry.getValue());
            locationProperties.add(locationProperty);
        }
        LocationDetails locationDetails = new LocationDetails();
        locationDetails.setLocationType(request.getLocationType());
        locationDetails.getLocationProperty().addAll(locationProperties);
        return locationDetails;
    }
}
