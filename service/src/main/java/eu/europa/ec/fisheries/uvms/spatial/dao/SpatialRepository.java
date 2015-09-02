package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.vividsolutions.jts.geom.Point;

import java.util.List;

/**
 * Created by Michal Kopyczok on 21-Aug-15.
 */
public interface SpatialRepository {
    List<Integer> findAreasIdByLocation(Point point, String areaDbTable);

    List<Integer> findClosestAreas(Point point, String areaDbTable);
}
