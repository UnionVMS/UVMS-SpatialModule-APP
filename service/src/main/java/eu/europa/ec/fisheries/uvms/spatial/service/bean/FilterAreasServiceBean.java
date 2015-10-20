package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.repository.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import eu.europa.ec.fisheries.uvms.spatial.util.TransformUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.apache.commons.collections.CollectionUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Stateless
@Local(FilterAreasService.class)
@Transactional
@Slf4j
public class FilterAreasServiceBean implements FilterAreasService {

    private static final String TYPE_NAMES = "typeNames";

    @EJB
    private SpatialRepository repository;

    @Override
    @SneakyThrows
    public FilterAreasSpatialRS filterAreas(FilterAreasSpatialRQ request) {
        UserAreasType userAreas = request.getUserAreas();
        ScopeAreasType scopeAreas = request.getScopeAreas();
        validateAreas(userAreas, scopeAreas);

        List<String> userAreaTypes = mapToStringList(userAreas, TransformUtils.EXTRACT_AREA_TYPE);
        List<String> userAreaIds = mapToStringList(userAreas, TransformUtils.EXTRACT_AREA_ID);
        List<String> userAreaTables = convertToUserAreaTables(userAreaTypes);

        List<String> scopeAreaTypes = mapToStringList(scopeAreas, TransformUtils.EXTRACT_AREA_TYPE);
        List<String> scopeAreaIds = mapToStringList(scopeAreas, TransformUtils.EXTRACT_AREA_ID);
        List<String> scopeAreaTables = convertToUserAreaTables(scopeAreaTypes);

        String wktGeometry = repository.filterAreas(userAreaTables, userAreaIds, scopeAreaTables, scopeAreaIds);
        validateResponse(wktGeometry);

        return createResponse(wktGeometry);
    }

    // TODO Marge find by Names from user and scope areas into one request
    @SneakyThrows
    private List<String> convertToUserAreaTables(List<String> userAreaTypes) {
        Map<String, List<String>> parameters = createParameters(userAreaTypes);
        List<AreaLocationTypesEntity> areaEntities = repository.findEntityByNamedQuery(AreaLocationTypesEntity.class, QueryNameConstants.FIND_TYPE_BY_NAMES, parameters);
        Map<String, String> areaType2TableMap = createAreaType2TableMap(areaEntities);
        return validateAndTransform(userAreaTypes, areaType2TableMap);
    }

    private List<String> mapToStringList(ScopeAreasType scopeAreasType, Function<AreaIdentifierType, String> func) {
        if (scopeAreasType != null) {
            return Lists.transform(scopeAreasType.getScopeAreas(), func);
        }
        return Collections.emptyList();
    }

    private List<String> mapToStringList(UserAreasType userAreasType, Function<AreaIdentifierType, String> func) {
        if (userAreasType != null) {
            return Lists.transform(userAreasType.getUserAreas(), func);
        }
        return Collections.emptyList();
    }

    private Map<String, String> createAreaType2TableMap(List<AreaLocationTypesEntity> areaEntities) {
        final Map<String, String> areaType2TableName = Maps.newHashMap();
        for (AreaLocationTypesEntity areaEntity : areaEntities) {
            areaType2TableName.put(areaEntity.getTypeName(), areaEntity.getAreaDbTable());
        }
        return areaType2TableName;
    }

    private Map<String, List<String>> createParameters(List<String> userAreaTypes) {
        return ImmutableMap.<String, List<String>>builder().put(TYPE_NAMES, userAreaTypes).build();
    }

    private List<String> validateAndTransform(List<String> userAreaTypes, final Map<String, String> areaType2TableName) {
        return Lists.transform(userAreaTypes, new Function<String, String>() {
            @Override
            public String apply(String userAreaType) {
                String userAreaTableName = areaType2TableName.get(userAreaType);
                validateTableName(userAreaType, userAreaTableName);
                return userAreaTableName;
            }

            private void validateTableName(String userAreaType, String userAreaTableName) {
                if (isBlank(userAreaTableName)) {
                    throw new SpatialServiceException(SpatialServiceErrors.INVALID_AREA_TYPE, userAreaType);
                }
            }
        });
    }

    private void validateAreas(UserAreasType userAreas, ScopeAreasType scopeAreas) {
        if (isUserAreasEmpty(userAreas) && isScopeAreasEmpty(scopeAreas)) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR);
        }
    }

    private boolean isScopeAreasEmpty(ScopeAreasType scopeAreas) {
        return scopeAreas == null || isEmpty(scopeAreas.getScopeAreas());
    }

    private boolean isUserAreasEmpty(UserAreasType userAreas) {
        return userAreas == null || isEmpty(userAreas.getUserAreas());
    }

    private void validateResponse(String wktGeometry) {
        if (StringUtils.isBlank(wktGeometry)) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR);
        }
    }

    private FilterAreasSpatialRS createResponse(String geometry) {
        FilterAreasSpatialRS response = new FilterAreasSpatialRS();
        response.setGeometry(geometry);
        return response;
    }

}
