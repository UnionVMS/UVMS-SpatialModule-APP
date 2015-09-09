package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.ImmutableMap;
import eu.europa.ec.fisheries.uvms.service.CrudService;
import eu.europa.ec.fisheries.uvms.spatial.entity.*;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.handler.ExceptionHandlerInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.handler.SpatialExceptionHandler;
import eu.europa.ec.fisheries.uvms.util.ColumnAliasNameHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static eu.europa.ec.fisheries.uvms.util.ModelUtils.createSuccessResponseMessage;
import static org.apache.commons.lang.StringUtils.isNumeric;

@Stateless
@Local(AreaDetailsService.class)
@Transactional
@Slf4j
public class AreaDetailsServiceBean implements AreaDetailsService {

    private static final ImmutableMap<String, Class> entityMap = ImmutableMap.<String, Class>builder()
            .put(AreaType.EEZ.value(), EezEntity.class)
            .put(AreaType.RFMO.value(), RfmoEntity.class)
            .put(AreaType.COUNTRY.value(), CountriesEntity.class)
            .put(AreaType.FAO.value(), FaoEntity.class)
            .put(AreaType.GFCM.value(), GfcmEntity.class)
            .put(AreaType.RAC.value(), RacEntity.class)
            .put(AreaType.S_TAT_RECT.value(), StatRectEntity.class)
            .build();

    private static final String TYPE_NAME = "typeName";

    @EJB
    private CrudService crudService;

    @SuppressWarnings("unchecked")
    @Override
    @SpatialExceptionHandler(responseType = AreaDetailsSpatialResponse.class)
    @Interceptors(value = ExceptionHandlerInterceptor.class)
    public AreaDetailsSpatialResponse getAreaDetails(AreaDetailsSpatialRequest request) {
        String areaTypeName = request.getAreaType().getAreaType();
        log.info("Area Type name received : " + areaTypeName);

        Map<String, String> parameters = createParameters(areaTypeName);
        List<AreaLocationTypesEntity> areasTypes = crudService.findEntityByNamedQuery(AreaLocationTypesEntity.class, QueryNameConstants.FIND_AREAS_BY_ID, parameters, 1);
        if (!areasTypes.isEmpty()) {
            if (isSystemAreaType(areaTypeName)) { //proceed only if the area is in the MAP
                Map<String, String> properties = getSystemAreaDetails(areasTypes.get(0), request.getAreaType().getId());
                return createAreaDetailsSpatialResponse(properties, request);
            } else {
                // TODO handle user areas
                throw new NotImplementedException("Not implemented yet");
            }
        } else {
            throw new SpatialServiceException(SpatialServiceErrors.INVALID_AREA_TYPE, areaTypeName);
        }
    }

    private Map<String, String> createParameters(String areaTypeName) {
        Map<String, String> parameters = newHashMap();
        parameters.put(TYPE_NAME, areaTypeName.toUpperCase());
        return parameters;
    }

    private boolean isSystemAreaType(String areaTypeName) {
        for (String key : entityMap.keySet()) {
            if (key.equalsIgnoreCase(areaTypeName)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> getSystemAreaDetails(AreaLocationTypesEntity areaTypeEntity, String id) {
        log.info("Area Type entity to be retrieved : " + areaTypeEntity.getTypeName());
        validateAreaId(id);

        Class entityClass = entityMap.get(areaTypeEntity.getTypeName()); // Check whether the area is already registered in the Map
        if (entityClass != null) {
            Object object = crudService.findEntityById(entityClass, Integer.parseInt(id));
            if (object != null) {
                return ColumnAliasNameHelper.getFieldMap(object); // Get the alias name set in the annotation using helper
            } else {
                throw new SpatialServiceException(SpatialServiceErrors.AREA_NOT_FOUND, areaTypeEntity.getTypeName());
            }
        } else {
            throw new SpatialServiceException(SpatialServiceErrors.AREA_NOT_FOUND, areaTypeEntity.getTypeName());
        }
    }

    private void validateAreaId(String id) {
        if (!isNumeric(id)) {
            throw new SpatialServiceException(SpatialServiceErrors.INVALID_AREA_ID, id);
        }
    }

    private AreaDetailsSpatialResponse createAreaDetailsSpatialResponse(Map<String, String> properties, AreaDetailsSpatialRequest request) {
        AreaDetails areaDetails = new AreaDetails(request.getAreaType(), createAreaProperties(properties));
        return new AreaDetailsSpatialResponse(createSuccessResponseMessage(), areaDetails);
    }

    private List<AreaProperty> createAreaProperties(Map<String, String> properties) {
        List<AreaProperty> areaProperties = new ArrayList<AreaProperty>();
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            areaProperties.add(new AreaProperty(entry.getKey(), entry.getValue()));
        }
        return areaProperties;
    }

}
