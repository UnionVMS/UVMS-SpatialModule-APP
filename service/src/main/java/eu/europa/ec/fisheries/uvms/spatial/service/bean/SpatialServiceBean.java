package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import static eu.europa.ec.fisheries.uvms.spatial.util.ColumnAliasNameHelper.getFieldMap;
import static eu.europa.ec.fisheries.uvms.spatial.util.SpatialTypeEnum.getEntityClassByType;
import static eu.europa.ec.fisheries.uvms.spatial.util.SpatialTypeEnum.getNativeQueryByType;
import static eu.europa.ec.fisheries.uvms.spatial.util.SpatialUtils.convertToPointInWGS84;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;

import eu.europa.ec.fisheries.uvms.service.DAO;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ImmutableMap;
import com.vividsolutions.jts.geom.Point;

import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Coordinate;
import eu.europa.ec.fisheries.uvms.spatial.repository.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class SpatialServiceBean {
	
    protected static final String TYPE_NAME = "typeName";

    @EJB
    private SpatialRepository repository;
	
    @SuppressWarnings("unchecked")
    @SneakyThrows
	protected AreaLocationTypesEntity getAreaLocationType(String type) {
    	Map<String, String> parameters = ImmutableMap.<String, String>builder().put(TYPE_NAME, type.toUpperCase()).build();
     	List<AreaLocationTypesEntity> areasLocationTypes = repository.findEntityByNamedQuery(AreaLocationTypesEntity.class, QueryNameConstants.FIND_TYPE_BY_ID, parameters, 1);
     	if (areasLocationTypes.isEmpty()) {
     		throw new SpatialServiceException(SpatialServiceErrors.INVALID_AREA_LOCATION_TYPE, areasLocationTypes);
     	}
     	return areasLocationTypes.get(0);
    }
    
    protected void validateId(String id) {
		if (!StringUtils.isNumeric(id)) {
			throw new SpatialServiceException(SpatialServiceErrors.INVALID_ID_TYPE, id);
		}
    }
    
    @SuppressWarnings("unchecked")
    @SneakyThrows
	protected Map<String, String> getAreaLocationDetailsById(Number id, AreaLocationTypesEntity areaLocationTypeEntity) {
		Object object = repository.findEntityById(getEntityClassByType(areaLocationTypeEntity.getTypeName()), id);
		if (object == null) {
			throw new SpatialServiceException(SpatialServiceErrors.ENTITY_NOT_FOUND, areaLocationTypeEntity.getTypeName());
		}
		return getFieldMap(object);
	}
    
    @SuppressWarnings("rawtypes")
	protected Map<String, String> getAreaLocationDetailsByCoordinates(Coordinate request, AreaLocationTypesEntity areaLocationTypeEntry) {
    	Point point = convertToPointInWGS84(request.getLongitude(), request.getLatitude(), request.getCrs());
    	List list = repository.findAreaOrLocationByCoordinates(point, getNativeQueryByType(areaLocationTypeEntry.getTypeName()));
    	if (list.isEmpty()) {
    		throw new SpatialServiceException(SpatialServiceErrors.ENTITY_NOT_FOUND, areaLocationTypeEntry.getTypeName());
    	}
    	return getFieldMap(list.get(0));
    }
}
