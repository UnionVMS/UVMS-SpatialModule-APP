package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.Lists;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.FilterAreasSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.FilterAreasSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ScopeAreasType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.UserAreasType;
import eu.europa.ec.fisheries.uvms.spatial.repository.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.AreaIdentifierDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import eu.europa.ec.fisheries.uvms.spatial.util.TransformUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

import static org.apache.commons.collections.CollectionUtils.isEmpty;

@Stateless
@Local(FilterAreasService.class)
@Transactional
@Slf4j
public class FilterAreasServiceBean implements FilterAreasService {

    @EJB
    private SpatialRepository repository;

    @Override
    public FilterAreasSpatialRS filterAreas(FilterAreasSpatialRQ filterAreasSpatialRQ) {
        validateUserAreas(filterAreasSpatialRQ.getUserAreas());

        List<AreaIdentifierDto> userAreas = mapToDtoList(filterAreasSpatialRQ.getUserAreas());
        List<AreaIdentifierDto> scopeAreas = mapToDtoList(filterAreasSpatialRQ.getScopeAreas());

        String wktGeometry = repository.filterAreas(userAreas, scopeAreas);
        validateResponse(wktGeometry);

        return createResponse(wktGeometry);
    }

    private void validateUserAreas(UserAreasType userAreas) {
        if (userAreas == null || isEmpty(userAreas.getUserAreas())) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR);
        }
    }

    private void validateResponse(String wktGeometry) {
        if (StringUtils.isBlank(wktGeometry)) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR);
        }
    }

    private List<AreaIdentifierDto> mapToDtoList(ScopeAreasType scopeAreasType) {
        if (scopeAreasType != null) {
            return Lists.transform(scopeAreasType.getScopeAreas(), TransformUtils.AREA_IDENTIFIER_TO_DTO);
        }
        return Collections.emptyList();
    }

    private List<AreaIdentifierDto> mapToDtoList(UserAreasType userAreasType) {
        if (userAreasType != null) {
            return Lists.transform(userAreasType.getUserAreas(), TransformUtils.AREA_IDENTIFIER_TO_DTO);
        }
        return Collections.emptyList();
    }

    private FilterAreasSpatialRS createResponse(String geometry) {
        FilterAreasSpatialRS response = new FilterAreasSpatialRS();
        response.setGeometry(geometry);
        return response;
    }

}
