package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import static eu.europa.ec.fisheries.uvms.spatial.util.SpatialTypeEnum.getNamedQueryByType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import com.google.common.collect.ImmutableMap;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Stateless
@Local(SearchAreaService.class)
@Slf4j
public class SearchAreaServiceBean implements SearchAreaService {

    private static final String GID = "gid";
    private static final String TYPE_NAME = "typeName";
    private static final String AREA_TYPE = "areaType";

    private @EJB SpatialRepository repository;

    @Override
    @Transactional
    public List<Map<String, String>> getSelectedAreaColumns(List<AreaTypeEntry> areaTypes) throws ServiceException {
        List<Map<String, String>> columnMapList = new ArrayList<>();

        for(AreaTypeEntry areaTypeEntry : areaTypes) {

            String gid = areaTypeEntry.getId();
            String areaType = areaTypeEntry.getAreaType().value();

            if (!StringUtils.isNumeric(gid)) {
                throw new SpatialServiceException(SpatialServiceErrors.INVALID_ID_TYPE, gid);
            }

            Map<String, String> parameters = ImmutableMap.<String, String>builder().put(TYPE_NAME, areaType.toUpperCase()).build();

            List<AreaLocationTypesEntity> areasLocationTypes =
                    repository.findEntityByNamedQuery(AreaLocationTypesEntity.class, AreaLocationTypesEntity.FIND_TYPE_BY_NAME, parameters, 1); // TODO @Greg Dao

            if (areasLocationTypes.isEmpty()) {
                throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR, areasLocationTypes);
            }

            columnMapList.add(getSelectedColumnMap(areaTypeEntry.getAreaType().value(), areaTypeEntry.getId()));
        }

        return columnMapList;
    }

    private Map<String, String> getSelectedColumnMap(String areaType, String gid) {

        String namedQuery = getNamedQueryByType(areaType);
        List<Map<String, String>> selectedAreaColumns = repository.findSelectedAreaColumns(namedQuery, Long.parseLong(gid));

        if (selectedAreaColumns.isEmpty()) {
            throw new SpatialServiceException(SpatialServiceErrors.ENTITY_NOT_FOUND);
        }

        Map<String, String> columnMap = selectedAreaColumns.get(0);
        columnMap.put(GID, gid);
        columnMap.put(AREA_TYPE, areaType.toUpperCase());
        return columnMap;
    }
}
