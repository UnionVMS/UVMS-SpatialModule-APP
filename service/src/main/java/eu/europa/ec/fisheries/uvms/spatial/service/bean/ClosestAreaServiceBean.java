package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.google.common.collect.Maps;
import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.service.CrudService;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Area;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.repository.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.ClosestAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.MeasurementUnit;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import eu.europa.ec.fisheries.uvms.util.SpatialUtils;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
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
    public List<Area> getClosestAreas(ClosestAreaSpatialRQ request) {
        Point point = convertToPointInWGS84(request.getPoint());
        MeasurementUnit measurementUnit = MeasurementUnit.getMeasurement(request.getUnit().name());

        Map<String, String> areaType2TableName = getAreaType2TableNameMap();
        List<Area> closestAreas = newArrayList();
        for (AreaType areaType : request.getAreaTypes().getAreaType()) {
            String areaDbTable = areaType2TableName.get(areaType.value());

            List<ClosestAreaDto> closestAreaList = repository.findClosestArea(point, measurementUnit, areaDbTable);
            validateResponse(closestAreaList);

            ClosestAreaDto closestAreaDto = closestAreaList.get(0);
            if (closestAreaDto != null) {
                Area closestAreaEntry = new Area();
                closestAreaEntry.setId(closestAreaDto.getId());
                closestAreaEntry.setDistance(closestAreaDto.getDistance());
                closestAreaEntry.setUnit(request.getUnit());
                closestAreaEntry.setAreaType(areaType);
                closestAreas.add(closestAreaEntry);
            }
        }

        return closestAreas;
    }

    @Override
    public List<ClosestAreaDto> getClosestAreas(double lat, double lon, int crs, String unit, List<String> types) {
        Point point = SpatialUtils.convertToPointInWGS84(lon, lat, crs);
        MeasurementUnit measurementUnit = MeasurementUnit.getMeasurement(unit);

        List<String> areaTypes = SpatialUtils.toUpperCase(types);
        Map<String, String> areaType2TableName = getAreaType2TableNameMap();
        List<ClosestAreaDto> closestAreas = newArrayList();
        for (String areaType : areaTypes) {
            String areaDbTable = areaType2TableName.get(areaType);
            validateAreaType(areaType, areaDbTable);

            List<ClosestAreaDto> closestAreaList = repository.findClosestArea(point, measurementUnit, areaDbTable);
            validateResponse(closestAreaList);

            ClosestAreaDto closestAreaDto = closestAreaList.get(0);
            if (closestAreaDto != null) {
                closestAreaDto.setAreaType(areaType);
                closestAreaDto.setUnit(measurementUnit.name());
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
        List<AreaLocationTypesEntity> areas = crudService.findEntityByNamedQuery(AreaLocationTypesEntity.class, QueryNameConstants.FIND_ALL_AREAS);
        Map<String, String> areaMap = Maps.newHashMap();
        for (AreaLocationTypesEntity area : areas) {
            areaMap.put(area.getTypeName().toUpperCase(), area.getAreaDbTable());
        }
        return areaMap;
    }

    private void validateResponse(List<ClosestAreaDto> closestAreaList) {
        if (closestAreaList.size() > 1) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR);
        }
    }

}
