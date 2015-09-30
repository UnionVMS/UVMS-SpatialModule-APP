package eu.europa.ec.fisheries.uvms.spatial.repository;

import java.util.List;

import com.vividsolutions.jts.geom.Point;

import eu.europa.ec.fisheries.uvms.service.DAO;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.AreaLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.ClosestAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.ClosestLocationDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.MeasurementUnit;

public interface SpatialRepository extends DAO {

    List<Integer> findAreasIdByLocation(Point point, String areaDbTable);

    List<ClosestAreaDto> findClosestArea(Point point, MeasurementUnit unit, String areaDbTable);

    List<ClosestLocationDto> findClosestlocation(Point point, MeasurementUnit unit, String areaDbTable);
    
    List findAreaOrLocationByCoordinates(Point point, String nativeQueryString);
    
    List<AreaLayerDto> findSystemAreaLayerMapping();
}
