package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import static com.google.common.collect.Maps.newHashMap;
import static eu.europa.ec.fisheries.uvms.util.ModelUtils.createSuccessResponseMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;

import eu.europa.ec.fisheries.uvms.service.CrudService;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.PortsEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationDetailsSpatialRequest;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationDetailsSpatialResponse;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationProperty;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationType;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.handler.ExceptionHandlerInterceptor;
import eu.europa.ec.fisheries.uvms.util.ColumnAliasNameHelper;

@Stateless
@Local(LocationDetailsService.class)
@Transactional
public class LocationDetailsServiceBean implements LocationDetailsService {
	
	private static Logger LOG = LoggerFactory.getLogger(LocationDetailsServiceBean.class.getName());
	
    @EJB
    private CrudService crudService;
    
    private ImmutableMap<String, Class> entityMap = ImmutableMap.<String, Class>builder()
            .put(LocationType.PORT.value(), PortsEntity.class).build();

    @SuppressWarnings("unchecked")
    @Override
    //@SpatialExceptionHandler(responseType = LocationDetailsSpatialResponse.class)
    @Interceptors(value = ExceptionHandlerInterceptor.class)
	public LocationDetailsSpatialResponse getLocationDetails(LocationDetailsSpatialRequest request) {
    	
    	LocationDetailsSpatialResponse response = null;
        LocationType locationType = request.getLocationType().getLocationType();
        LOG.info("Location Type name received : " + locationType.value());
        Map<String, String> parameters = newHashMap();
        parameters.put("typeName", locationType.value().toUpperCase());
        List<AreaLocationTypesEntity> locationTypes = crudService.findEntityByNamedQuery(AreaLocationTypesEntity.class, QueryNameConstants.FIND_TYPE_BY_ID, parameters, 1);
        if (!locationTypes.isEmpty()) {
        	Map<String, String> properties = getLocationDetails(locationTypes.get(0), request.getLocationType().getId());
            response = createLocationDetailsSpatialResponse(properties, request);
        } else {
            throw new SpatialServiceException(SpatialServiceErrors.INVALID_AREA_TYPE, locationType.value());
        }
        return response;
	}


    @SuppressWarnings("unchecked")
	private Map<String, String> getLocationDetails(AreaLocationTypesEntity areaTypeEntity, String id) {
		Map<String, String> properties = newHashMap();
		LOG.info("Location Type entity to be retrieved : " + areaTypeEntity.getTypeName());
		if (!StringUtils.isNumeric(id)) {
			throw new SpatialServiceException(SpatialServiceErrors.INVALID_LOCATION_ID, id);
		}
		Class entityClass = entityMap.get(areaTypeEntity.getTypeName()); // Check whether the area is already registered in the Map
		if (entityClass != null) {
			Object object = crudService.findEntityById(entityClass, Integer.parseInt(id));
			if (object != null) {
				properties = ColumnAliasNameHelper.getFieldMap(object); // Get the alias name set in the annotation using helper
			} else {
				throw new SpatialServiceException(SpatialServiceErrors.LOCATION_NOT_FOUND, areaTypeEntity.getTypeName());
			}
		} else {
			throw new SpatialServiceException(SpatialServiceErrors.LOCATION_NOT_FOUND, areaTypeEntity.getTypeName());
		}
		return properties; 
	}

    private LocationDetailsSpatialResponse createLocationDetailsSpatialResponse(Map<String, String> properties, LocationDetailsSpatialRequest request) {
        List<LocationProperty> locationProperties = new ArrayList<LocationProperty>();
        for (Map.Entry<String, String> entry : properties.entrySet()) {
        	locationProperties.add(new LocationProperty(entry.getKey(), entry.getValue()));
        }
        LocationDetails locationDetails = new LocationDetails();
        locationDetails.setLocationType(request.getLocationType());
        locationDetails.setLocationProperties(locationProperties);
        LocationDetailsSpatialResponse response = new LocationDetailsSpatialResponse();
        response.setLocationDetails(locationDetails);
        response.setResponseMessage(createSuccessResponseMessage());
        return response;
    }
}
