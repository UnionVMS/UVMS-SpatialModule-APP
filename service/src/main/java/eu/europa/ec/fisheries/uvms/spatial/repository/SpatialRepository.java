package eu.europa.ec.fisheries.uvms.spatial.repository;

import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.service.DAO;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.*;

import java.util.List;
import java.util.Map;

public interface SpatialRepository extends DAO {

    List<Integer> findAreasIdByLocation(Point point, String areaDbTable);

    List<ClosestAreaDto> findClosestArea(Point point, MeasurementUnit unit, String areaDbTable);

    List<ClosestLocationDto> findClosestlocation(Point point, MeasurementUnit unit, String areaDbTable);

    List findAreaOrLocationByCoordinates(Point point, String nativeQueryString);

    List<AreaLayerDto> findSystemAreaLayerMapping();

    List<Map<String, String>> findAreaByFilter(String areaType, String filter);

    List<Map<String, String>> findSelectedAreaColumns(String namedQueryString, Number gid);

    String filterAreas(List<String> userAreaTables, List<String> userAreaIds, List<String> scopeAreaTables, List<String> scopeAreaIds);
}
