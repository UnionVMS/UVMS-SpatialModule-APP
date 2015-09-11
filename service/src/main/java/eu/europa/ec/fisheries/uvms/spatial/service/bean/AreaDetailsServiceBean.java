package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import static com.google.common.collect.Maps.newHashMap;
import static eu.europa.ec.fisheries.uvms.util.ModelUtils.createSuccessResponseMessage;
import static eu.europa.ec.fisheries.uvms.util.ColumnAliasNameHelper.getFieldMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.transaction.Transactional;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;

import eu.europa.ec.fisheries.uvms.service.CrudService;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.CountriesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.FaoEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.GfcmEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.RacEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.RfmoEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.StatRectEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetailsSpatialRequest;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetailsSpatialResponse;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaProperty;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.handler.ExceptionHandlerInterceptor;

@Stateless
@Local(AreaDetailsService.class)
@Transactional
public class AreaDetailsServiceBean implements AreaDetailsService {

    private static Logger LOG = LoggerFactory.getLogger(AreaDetailsServiceBean.class.getName());

    private ImmutableMap<String, Class> entityMap = ImmutableMap.<String, Class>builder()
            .put(AreaType.EEZ.value(), EezEntity.class)
            .put(AreaType.RFMO.value(), RfmoEntity.class)
            .put(AreaType.COUNTRY.value(), CountriesEntity.class)
            .put(AreaType.FAO.value(), FaoEntity.class)
            .put(AreaType.GFCM.value(), GfcmEntity.class)
            .put(AreaType.RAC.value(), RacEntity.class)
            .put(AreaType.S_TAT_RECT.value(), StatRectEntity.class)
            .build();
    
    private static String TYPE_NAME = "typeName";

    @EJB
    private CrudService crudService;

    @SuppressWarnings("unchecked")
    @Override
    @Interceptors(value = ExceptionHandlerInterceptor.class)
    public AreaDetailsSpatialResponse getAreaDetails(AreaDetailsSpatialRequest request) {
        String areaTypeName = request.getAreaType().getAreaType();
        LOG.info("Area Type name received : " + areaTypeName);
        Map<String, String> parameters = newHashMap();
        parameters.put(TYPE_NAME, areaTypeName.toUpperCase());
        List<AreaLocationTypesEntity> areasTypes = crudService.findEntityByNamedQuery(AreaLocationTypesEntity.class, QueryNameConstants.FIND_TYPE_BY_ID, parameters, 1);
        if (!areasTypes.isEmpty()) {        	
        	AreaLocationTypesEntity areaType = areasTypes.get(0);  // We just have one entity in the result
        	if (areaType.getIsSystemWide()) {
        		Map<String, String> properties = getSystemAreaDetails(areaType, request.getAreaType().getId());
                return createAreaDetailsSpatialResponse(properties, request);
        	} else {
        		// TODO Get area details for custom areas
        		throw new NotImplementedException();
        	}        	
        } else {
            throw new SpatialServiceException(SpatialServiceErrors.INVALID_AREA_TYPE, areaTypeName);
        }
    }

    @SuppressWarnings("unchecked")
	private Map<String, String> getSystemAreaDetails(AreaLocationTypesEntity areaTypeEntity, String id) {
		LOG.info("Area Type entity to be retrieved : " + areaTypeEntity.getTypeName());
		if (!StringUtils.isNumeric(id)) {
			throw new SpatialServiceException(SpatialServiceErrors.INVALID_AREA_ID, id);
		}
		Class entityClass = entityMap.get(areaTypeEntity.getTypeName());
		Object object = crudService.findEntityById(entityClass, Integer.parseInt(id));
		if (object != null) {
			return getFieldMap(object); // Get the alias name set in the annotation using helper
		} else {
			throw new SpatialServiceException(SpatialServiceErrors.AREA_NOT_FOUND, areaTypeEntity.getTypeName());
		}
	}

    private AreaDetailsSpatialResponse createAreaDetailsSpatialResponse(Map<String, String> properties, AreaDetailsSpatialRequest request) {
        List<AreaProperty> areaProperties = new ArrayList<AreaProperty>();
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            areaProperties.add(new AreaProperty(entry.getKey(), entry.getValue()));
        }
        AreaDetails areaDetails = new AreaDetails();
        areaDetails.setAreaType(request.getAreaType());
        areaDetails.setAreaProperties(areaProperties);
        AreaDetailsSpatialResponse response = new AreaDetailsSpatialResponse();
        response.setAreaDetails(areaDetails);
        response.setResponseMessage(createSuccessResponseMessage());
        return response;
    }
}
