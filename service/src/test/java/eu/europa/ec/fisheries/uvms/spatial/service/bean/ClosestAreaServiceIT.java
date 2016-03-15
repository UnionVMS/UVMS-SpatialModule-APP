package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialRQ.AreaTypes;
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
public class ClosestAreaServiceIT extends AbstractArquillianIT {

    private static final double LATITUDE = -10.85615;
    private static final double LATITUDE_2 = 45.11557;
    private static final double LONGITUDE = 4.74118;
    private static final double LONGITUDE_2 = -7.14925;
    private static final int DEFAULT_CRS = 4326;
    private static final int CRS = 3857;
    @EJB
    private AreaService closestAreaService;

    @Test
    public void shouldGetClosestArea() {
        PointType pointType = new PointType();
        pointType.setLongitude(LONGITUDE);
        pointType.setLatitude(LATITUDE);
        pointType.setCrs(DEFAULT_CRS);
        PointType point = pointType;
        AreaTypes areas = new AreaTypes();
        areas.getAreaTypes().addAll((newArrayList(AreaType.EEZ)));

        ClosestAreaSpatialRQ closestAreaSpatialRQ = new ClosestAreaSpatialRQ();
        closestAreaSpatialRQ.setAreaTypes(areas);
        closestAreaSpatialRQ.setPoint(point);
        closestAreaSpatialRQ.setUnit(UnitType.METERS);
        ClosestAreaSpatialRQ request = closestAreaSpatialRQ;

        List<Area> closestAreas = closestAreaService.getClosestAreas(request);

        assertNotNull(closestAreas);
        assertFalse(closestAreas.isEmpty());

        Area area = closestAreas.get(0);
        assertEquals("163", area.getId());
        assertEquals(524023.77798891655, area.getDistance(), 0.01);
        assertEquals("AGO", area.getCode());
        assertEquals("Angolan Exclusive Economic Zone", area.getName());
        assertEquals(AreaType.EEZ, area.getAreaType());
        assertEquals(UnitType.METERS, area.getUnit());
    }

    @Test
    public void shouldGetClosestAreaWithCrsTransform() {
        PointType pointType = new PointType();
        pointType.setLongitude(LONGITUDE_2);
        pointType.setLatitude(LATITUDE_2);
        pointType.setCrs(CRS);
        PointType point = pointType;
        AreaTypes areas = new AreaTypes();
        areas.getAreaTypes().addAll((newArrayList(AreaType.COUNTRY)));

        ClosestAreaSpatialRQ closestAreaSpatialRQ = new ClosestAreaSpatialRQ();
        closestAreaSpatialRQ.setAreaTypes(areas);
        closestAreaSpatialRQ.setPoint(point);
        closestAreaSpatialRQ.setUnit(UnitType.MILES);
        ClosestAreaSpatialRQ request = closestAreaSpatialRQ;

        // when
        List<Area> closestAreas = closestAreaService.getClosestAreas(request);

        //then
        assertNotNull(closestAreas);
        assertFalse(closestAreas.isEmpty());

        Area area = closestAreas.get(0);
        assertEquals("94", area.getId());
        assertEquals(354.0293920161582, area.getDistance(), 0.01);
        assertEquals("GHA", area.getCode());
        assertEquals("Ghana", area.getName());
        assertEquals(AreaType.COUNTRY, area.getAreaType());
        assertEquals(UnitType.MILES, area.getUnit());
    }

}
