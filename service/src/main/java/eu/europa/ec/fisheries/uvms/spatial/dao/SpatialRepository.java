package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaEntry;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.UnitType;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.ClosestAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.MeasurementUnit;

import java.util.List;

/**
 * Created by Michal Kopyczok on 21-Aug-15.
 */
public interface SpatialRepository {
    List<Integer> findAreasIdByLocation(Point point, String areaDbTable);

    List<ClosestAreaDto> findClosestAreas(Point point, MeasurementUnit unit, String areaDbTable);
}
