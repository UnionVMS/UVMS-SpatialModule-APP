package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.Maps;
import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.service.CrudService;
import eu.europa.ec.fisheries.uvms.spatial.dao.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.ClosestAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.MeasurementUnit;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.handler.ExceptionHandlerInterceptor;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.handler.SpatialExceptionHandler;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
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
    @Interceptors(value = ExceptionHandlerInterceptor.class)
    public ClosestAreaSpatialRS getClosestAreas(ClosestAreaSpatialRQ request) {
        Map<String, String> areaType2TableName = getAreaType2TableNameMap();
        Point point = convertToPointInWGS84(request.getPoint());
        MeasurementUnit measurementUnit = MeasurementUnit.valueOf(request.getUnit().name());

        List<ClosestAreaEntry> closestAreas = newArrayList();
        for (AreaType areaType : request.getAreaTypes().getAreaType()) {
            String areaDbTable = areaType2TableName.get(areaType.value());

            List<ClosestAreaEntry> closestAreaList = repository.findClosestAreas(point, measurementUnit, areaDbTable);
            validateResponse(closestAreaList);

            ClosestAreaEntry closestAreaEntry = closestAreaList.get(0);
            if (closestAreaEntry != null) {
                closestAreaEntry.setAreaTypeName(areaType);
                closestAreaEntry.setUnit(request.getUnit());
                closestAreas.add(closestAreaEntry);
            }
        }

        return createSuccessClosestAreaResponse(new ClosestAreasType(closestAreas));
    }

    @Override
    public List<ClosestAreaDto> getClosestAreasRest(double lat, double lon, int crs, String unit, List<String> areaTypes) {
        Map<String, String> areaType2TableName = getAreaType2TableNameMap();
        Point point = convertToPointInWGS84(lon, lat, crs);
        MeasurementUnit measurementUnit = MeasurementUnit.getMeasurement(unit);

        List<ClosestAreaDto> closestAreas = newArrayList();
        for (String areaType : areaTypes) {
            String areaDbTable = areaType2TableName.get(areaType);
            validateAreaType(areaType, areaDbTable);

            List<ClosestAreaEntry> closestAreaList = repository.findClosestAreas(point, measurementUnit, areaDbTable);
            validateResponse(closestAreaList);

            ClosestAreaEntry area = closestAreaList.get(0);
            if (area != null) {
                ClosestAreaDto closestAreaDto = new ClosestAreaDto(area.getId(), area.getAreaTypeName().name(), area.getDistance(), unit);
                closestAreas.add(closestAreaDto);
            }
        }

        return closestAreas;
    }

    private void validateAreaType(String areaType, String areaDbTable) {
        if (areaDbTable == null) {
            throw new SpatialServiceException(SpatialServiceErrors.WRONG_AREA_TYPE, areaType);
        }
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
