package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.service.CommonGenericDAO;

import java.util.List;

/**
 * Created by Michal Kopyczok on 21-Aug-15.
 */
public interface SpatialRepository extends CommonGenericDAO {
    List<Integer> findAreasIdByLocation(Point point, String areaDbTable);
}
