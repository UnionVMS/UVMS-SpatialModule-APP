package eu.europa.ec.fisheries.uvms.spatial.dao;

import eu.europa.ec.fisheries.uvms.service.CommonGenericDAO;

import java.util.List;

/**
 * Created by Michal Kopyczok on 21-Aug-15.
 */
public interface SpatialRepository extends CommonGenericDAO {
    List<Integer> findAreasIdByLocation(double lat, double lon, int crs, String areaDbTable);
}
