package eu.europa.ec.fisheries.uvms.spatial.repository;

import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.service.AbstractCrudService;
import eu.europa.ec.fisheries.uvms.service.CrudService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.ClosestAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.ClosestLocationDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.MeasurementUnit;

import java.util.List;
import java.util.Map;

public interface SpatialRepository extends CrudService {

    List<Integer> findAreasIdByLocation(Point point, String areaDbTable);

    List<ClosestAreaDto> findClosestArea(Point point, MeasurementUnit unit, String areaDbTable);

    List<ClosestLocationDto> findClosestlocation(Point point, MeasurementUnit unit, String areaDbTable);
    
    List findAreaOrLocationByCoordinates(Point point, String nativeQueryString);
}
