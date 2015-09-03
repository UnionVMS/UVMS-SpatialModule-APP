package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.Maps;
import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.service.CrudService;
import eu.europa.ec.fisheries.uvms.spatial.dao.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.MeasurementUnit;
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
        MeasurementUnit unit = MeasurementUnit.valueOf(request.getUnit().name());

        List<ClosestAreaEntry> closestAreas = newArrayList();
        for (AreaType areaType : request.getAreaTypes().getAreaType()) {
            String areaDbTable = areaType2TableName.get(areaType.value());

            List<ClosestAreaEntry> closestAreaList = repository.findClosestAreas(point, unit, areaDbTable);
            validateResponse(closestAreaList);

            ClosestAreaEntry closestAreaEntry = closestAreaList.get(0);
            if (closestAreaEntry != null) {
                closestAreaEntry.setAreaTypeName(areaType);
                closestAreaEntry.setUnits(request.getUnit());
                closestAreas.add(closestAreaEntry);
            }
        }

        return createSuccessClosestAreaResponse(new ClosestAreasType(closestAreas));
    }

    private Map<String, String> getAreaType2TableNameMap() {
        List<AreaTypesEntity> allEntity = crudService.findAllEntity(AreaTypesEntity.class);
        Map<String, String> areaMap = Maps.newHashMap();
        for (AreaTypesEntity i : allEntity) {
            areaMap.put(i.getTypeName().toUpperCase(), i.getAreaDbTable());
        }
        return areaMap;
    }

    private void validateResponse(List<ClosestAreaEntry> closestAreaList) {
        if (closestAreaList.size() > 1) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR);
        }
    }

    private ClosestAreaSpatialRS createSuccessClosestAreaResponse(ClosestAreasType closestAreasType) {
        return new ClosestAreaSpatialRS(createSuccessResponseMessage(), closestAreasType);
    }


}
