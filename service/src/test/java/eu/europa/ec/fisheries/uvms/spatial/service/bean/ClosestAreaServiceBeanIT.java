package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialRQ.AreaTypes;
import lombok.SneakyThrows;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class ClosestAreaServiceBeanIT extends AbstractArquillianIT {

    private static final double LATITUDE = 32.85615;
    private static final double LATITUDE_2 = 45.11557;
    private static final double LONGITUDE = -10.74118;
    private static final double LONGITUDE_2 = -7.14925;
    private static final int DEFAULT_CRS = 4326;
    private static final int CRS = 3857;
    @EJB
    private SpatialService closestAreaService;

    @Test
    @SneakyThrows
    public void testGetClosestAreas() {
        PointType pointType = new PointType();
        pointType.setLongitude(LONGITUDE);
        pointType.setLatitude(LATITUDE);
        pointType.setCrs(DEFAULT_CRS);
        PointType point = pointType;
        AreaTypes areaTypes = new AreaTypes();
        areaTypes.getAreaTypes().addAll(newArrayList(AreaType.EEZ));
        AreaTypes areas = areaTypes;
        ClosestAreaSpatialRQ closestAreaSpatialRQ = new ClosestAreaSpatialRQ();
        closestAreaSpatialRQ.setPoint(point);
        closestAreaSpatialRQ.setUnit(UnitType.METERS);
        closestAreaSpatialRQ.setAreaTypes(areas);
        ClosestAreaSpatialRQ request = closestAreaSpatialRQ;
        List<Area> closestAreas = closestAreaService.getClosestAreasToPointByType(request);
        assertNotNull(closestAreas);
        assertFalse(closestAreas.isEmpty());
        Area area = closestAreas.get(0);
        assertEquals("231", area.getId());
        assertEquals(0.0, area.getDistance(), 0.01);
        assertEquals("MAR", area.getCode());
        assertEquals(AreaType.EEZ, area.getAreaType());
        assertEquals(UnitType.METERS, area.getUnit());
    }

    @Test
    @SneakyThrows
    public void testGetClosestAreasWithTransform() {
        PointType pointType = new PointType();
        pointType.setCrs(CRS);
        pointType.setLatitude(LATITUDE_2);
        pointType.setLongitude(LONGITUDE_2);
        AreaTypes areaTypes = new AreaTypes();
        areaTypes.getAreaTypes().addAll(newArrayList(AreaType.COUNTRY));
        AreaTypes areas = areaTypes;
        ClosestAreaSpatialRQ closestAreaSpatialRQ = new ClosestAreaSpatialRQ();
        closestAreaSpatialRQ.setPoint(pointType);
        closestAreaSpatialRQ.setUnit(UnitType.MILES);
        closestAreaSpatialRQ.setAreaTypes(areas);
        ClosestAreaSpatialRQ request = closestAreaSpatialRQ;
        List<Area> closestAreas = closestAreaService.getClosestAreasToPointByType(request);
        assertNotNull(closestAreas);
        assertFalse(closestAreas.isEmpty());
        Area area = closestAreas.get(0);
        assertEquals("94", area.getId());
        assertEquals(354.0293920161582, area.getDistance(), 0.01);
        assertEquals("GHA", area.getCode());
        assertEquals(AreaType.COUNTRY, area.getAreaType());
        assertEquals(UnitType.MILES, area.getUnit());
    }

}
