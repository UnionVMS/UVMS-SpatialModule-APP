package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.Maps;
import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.service.CrudService;
import eu.europa.ec.fisheries.uvms.spatial.dao.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.handler.SpatialExceptionHandler;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static eu.europa.ec.fisheries.uvms.util.ModelUtils.createSuccessResponseMessage;
import static eu.europa.ec.fisheries.uvms.util.SpatialUtils.convertToPointInWGS84;

@Stateless
@Local(ClosestAreaService.class)
@Transactional
@Slf4j
public class ClosestAreaServiceBean implements ClosestAreaService {

    @EJB
    private SpatialRepository repository;
    @EJB
    private CrudService crudService;

    @Override
    @SpatialExceptionHandler(responseType = ClosestAreaSpatialRS.class)
    public ClosestAreaSpatialRS getClosestArea(final ClosestAreaSpatialRQ request) {
        Map<String, String> areaType2TableName = getAreaType2TableNameMap();
        Point point = convertToPointInWGS84(request.getPoint());

        List<ClosestAreaEntry> closestAreas = newArrayList();
        for (AreaType areaType : request.getAreaTypes().getAreaType()) {
            String areaDbTable = areaType2TableName.get(areaType.value());
            UnitType unit = request.getUnit();

            List<ClosestAreaEntry> closestAreaList = repository.findClosestAreas(point, unit, areaDbTable);
            validateResponse(closestAreaList);

            ClosestAreaEntry closestAreaEntry = closestAreaList.get(0);
            if (closestAreaEntry != null) {
                closestAreaEntry.setAreaTypeName(areaType);
                closestAreaEntry.setUnits(unit);
                closestAreas.add(closestAreaEntry);
            }
        }

        return createSuccessClosestAreaResponse(new ClosestAreasType(closestAreas));
    }

    private void validateResponse(List<ClosestAreaEntry> closestAreaList) {
        if (isMoreThanOneArea(closestAreaList)) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR);
        }
    }

    private boolean isMoreThanOneArea(List<ClosestAreaEntry> closestAreaList) {
        return closestAreaList.size() > 1;
    }

    private ClosestAreaSpatialRS createSuccessClosestAreaResponse(ClosestAreasType closestAreasType) {
        return new ClosestAreaSpatialRS(createSuccessResponseMessage(), closestAreasType);
    }

    private Map<String, String> getAreaType2TableNameMap() {
        List<AreaTypesEntity> allEntity = crudService.findAllEntity(AreaTypesEntity.class);
        return convert2Map(allEntity);
    }

    private Map<String, String> convert2Map(List<AreaTypesEntity> allEntity) {
        Map<String, String> areaMap = Maps.newHashMap();
        for (AreaTypesEntity i : allEntity) {
            areaMap.put(i.getTypeName().toUpperCase(), i.getAreaDbTable());
        }
        return areaMap;
    }


}
