package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialRQ.AreaTypes;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

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
    private ClosestAreaService closestAreaService;

    @Test
    public void shouldGetClosestArea() {
        // given
        PointType point = new PointType(LONGITUDE, LATITUDE, DEFAULT_CRS);
        AreaTypes areas = new AreaTypes(newArrayList(AreaType.EEZ));
        ClosestAreaSpatialRQ request = new ClosestAreaSpatialRQ(point, areas, UnitType.METER);

        // when
        ClosestAreaSpatialRS response = closestAreaService.getClosestAreas(request);

        //then
        assertNotNull(response);
        assertNotNull(response.getResponseMessage().getSuccess());
        assertFalse(response.getClosestArea().getClosestArea().isEmpty());

        ClosestAreaEntry closestAreaEntry = response.getClosestArea().getClosestArea().get(0);
        assertEquals("231", closestAreaEntry.getId());
        assertEquals(0.0, closestAreaEntry.getDistance(), 0.01);
        assertEquals(AreaType.EEZ, closestAreaEntry.getAreaType());
        assertEquals(UnitType.METER, closestAreaEntry.getUnit());
    }

    @Test
    public void shouldGetClosestAreaWithCrsTransform() {
        // given
        PointType point = new PointType(LONGITUDE_2, LATITUDE_2, CRS);
        AreaTypes areas = new AreaTypes(newArrayList(AreaType.COUNTRY));
        ClosestAreaSpatialRQ request = new ClosestAreaSpatialRQ(point, areas, UnitType.MILE);

        // when
        ClosestAreaSpatialRS response = closestAreaService.getClosestAreas(request);

        //then
        assertNotNull(response);
        assertNotNull(response.getResponseMessage().getSuccess());
        assertFalse(response.getClosestArea().getClosestArea().isEmpty());

        ClosestAreaEntry closestAreaEntry = response.getClosestArea().getClosestArea().get(0);
        assertEquals("189", closestAreaEntry.getId());
        assertEquals(367.705022199885, closestAreaEntry.getDistance(), 0.01);
        assertEquals(AreaType.COUNTRY, closestAreaEntry.getAreaType());
        assertEquals(UnitType.MILE, closestAreaEntry.getUnit());
    }

}
