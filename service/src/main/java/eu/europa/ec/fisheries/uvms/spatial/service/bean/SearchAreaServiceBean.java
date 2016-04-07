package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import eu.europa.ec.fisheries.uvms.spatial.util.SpatialTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Stateless
@Local(SearchAreaService.class)
@Slf4j
public class SearchAreaServiceBean implements SearchAreaService {

    private static final String GID = "gid";
    private static final String AREA_TYPE = "areaType";

    private @EJB SpatialRepository repository;

    @Override
    @Transactional
    public List<Map<String, String>> getSelectedAreaColumns(final List<AreaTypeEntry> areaTypes) throws ServiceException {

        List<Map<String, String>> columnMapList = new ArrayList<>();

        for(AreaTypeEntry areaTypeEntry : areaTypes) {

            String gid = areaTypeEntry.getId();
            String areaType = areaTypeEntry.getAreaType().value();

            if (!StringUtils.isNumeric(gid)) {
                throw new SpatialServiceException(SpatialServiceErrors.INVALID_ID_TYPE, gid);
            }

            List<AreaLocationTypesEntity> areasLocationTypes = repository.findAreaLocationTypeByTypeName(areaType.toUpperCase());

            if (areasLocationTypes.isEmpty()) {
                throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR, areasLocationTypes);
            }

            String namedQuery = null;

            for (SpatialTypeEnum type : SpatialTypeEnum.values()) {
                if(type.getType().equalsIgnoreCase(areaType)) {
                    namedQuery = type.getNamedQuery();
                }
            }

            List<Map<String, String>> selectedAreaColumns = repository.findSelectedAreaColumns(namedQuery, Long.parseLong(gid));

            if (selectedAreaColumns.isEmpty()) {
                throw new SpatialServiceException(SpatialServiceErrors.ENTITY_NOT_FOUND);
            }

            Map<String, String> columnMap = selectedAreaColumns.get(0);
            columnMap.put(GID, gid);
            columnMap.put(AREA_TYPE, areaType.toUpperCase());

            columnMapList.add(columnMap);

        }

        return columnMapList;
    }

}
