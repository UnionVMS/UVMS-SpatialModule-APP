package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.repository.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.FilterAreasDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import eu.europa.ec.fisheries.uvms.spatial.util.TransformUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.*;

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
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public FilterAreasSpatialRS filterAreas(FilterAreasSpatialRQ request) {
        UserAreasType userAreas = request.getUserAreas();
        ScopeAreasType scopeAreas = request.getScopeAreas();
        validateAreas(userAreas, scopeAreas);

        List<String> userAreaTypes = mapToStringList(userAreas, TransformUtils.EXTRACT_AREA_TYPE);
        List<String> userAreaIds = mapToStringList(userAreas, TransformUtils.EXTRACT_AREA_ID);

        List<String> scopeAreaTypes = mapToStringList(scopeAreas, TransformUtils.EXTRACT_AREA_TYPE);
        List<String> scopeAreaIds = mapToStringList(scopeAreas, TransformUtils.EXTRACT_AREA_ID);

        Map<String, String> areaType2TableName = createAreaTableMapping(createUnionAreas(userAreaTypes, scopeAreaTypes));
        List<String> userAreaTables = convertToAreaTables(userAreaTypes, areaType2TableName);
        List<String> scopeAreaTables = convertToAreaTables(scopeAreaTypes, areaType2TableName);

        FilterAreasDto result = repository.filterAreas(userAreaTables, userAreaIds, scopeAreaTables, scopeAreaIds);

        return createResponse(result);
    }

    @SneakyThrows
    private Map<String, String> createAreaTableMapping(List<String> userAreaTypes) {
        Map<String, List<String>> parameters = createParameters(userAreaTypes);
        List<AreaLocationTypesEntity> areaEntities = repository.findEntityByNamedQuery(AreaLocationTypesEntity.class, QueryNameConstants.FIND_TYPE_BY_NAMES, parameters);
        Map<String, String> areaType2TableMap = createAreaType2TableMap(areaEntities);
        validateTableNames(userAreaTypes, areaType2TableMap);
        return areaType2TableMap;
    }

    private List<String> createUnionAreas(List<String> userAreaTypes, List<String> scopeAreaTypes) {
        Set<String> uniqueAreasSet = new HashSet(userAreaTypes);
        uniqueAreasSet.addAll(scopeAreaTypes);
        return Lists.newArrayList(uniqueAreasSet);
    }

    private List<String> convertToAreaTables(List<String> areaTypes, final Map<String, String> areaType2TableName) {
        return Lists.transform(areaTypes, new Function<String, String>() {
            @Override
            public String apply(String areaType) {
                return areaType2TableName.get(areaType);
            }
        });
    }

    private void validateTableNames(List<String> areaTypes, final Map<String, String> areaType2TableName) {
        for (String areaType : areaTypes) {
            String userAreaTableName = areaType2TableName.get(areaType);
            if (isBlank(userAreaTableName)) {
                throw new SpatialServiceException(SpatialServiceErrors.INVALID_AREA_TYPE, areaType);
            }
        }
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

    private FilterAreasSpatialRS createResponse(FilterAreasDto result) {
        FilterAreasSpatialRS response = new FilterAreasSpatialRS();
        response.setGeometry(result.getWktgeometry());
        response.setCode(result.getResultcode());
        return response;
    }

}
