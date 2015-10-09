package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.Lists;
import com.vividsolutions.jts.geom.Geometry;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.repository.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.AreaIdentifierDto;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.AreaIdentifierDtoMapper;
import eu.europa.ec.fisheries.uvms.spatial.util.SpatialUtils;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Stateless
@Local(SpatialEnrichmentService.class)
@Transactional
@Slf4j
public class FilterAreasServiceBean implements FilterAreasService {

    @Inject
    private AreaIdentifierDtoMapper areaIdentifierDtoMapper;

    @EJB
    private SpatialRepository repository;

    @Override
    public FilterAreasSpatialRS filterAreas(FilterAreasSpatialRQ filterAreasSpatialRQ) {
        List<AreaIdentifierDto> userAreas = mapToDtoList(filterAreasSpatialRQ.getUserAreas());
        List<AreaIdentifierDto> scopeAreas = mapToDtoList(filterAreasSpatialRQ.getScopeAreas());

        Geometry geometry = repository.filterAreas(userAreas, scopeAreas);
        String wktGeometry = SpatialUtils.convertToWkt(geometry);

        return createResponse(wktGeometry);
    }

    private List<AreaIdentifierDto> mapToDtoList(ScopeAreasType scopeAreasType) {
        if (scopeAreasType != null) {
            return convertToDtoList(scopeAreasType.getScopeArea());
        }
        return Collections.emptyList();
    }

    private List<AreaIdentifierDto> mapToDtoList(UserAreasType userAreasType) {
        if (userAreasType != null) {
            return convertToDtoList(userAreasType.getUserArea());
        }
        return Collections.emptyList();
    }

    private List<AreaIdentifierDto> convertToDtoList(List<AreaIdentifierType> areaIdentifiers) {
        List<AreaIdentifierDto> areas = Lists.newArrayList();
        for (AreaIdentifierType areaIdentifier : areaIdentifiers) {
            AreaIdentifierDto areaIdentifierDto = areaIdentifierDtoMapper.areaIdentifierTypeToAreaDto(areaIdentifier);
            areas.add(areaIdentifierDto);
        }
        return areas;
    }

    private FilterAreasSpatialRS createResponse(String geometry) {
        FilterAreasSpatialRS response = new FilterAreasSpatialRS();
        response.setGeometry(geometry);
        return response;
    }

}
