package eu.europa.ec.fisheries.uvms.spatial.service.bean;

/**
 * Created by Michal Kopyczok on 09-Sep-15.
 */

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import static com.google.common.collect.Lists.newArrayList;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class ClosestLocationServiceIT extends AbstractArquillianIT {

    private static final double LATITUDE = 32.85615;
    private static final double LATITUDE_2 = 45.11557;
    private static final double LONGITUDE = -10.74118;
    private static final double LONGITUDE_2 = -7.14925;
    private static final int DEFAULT_CRS = 4326;
    private static final int CRS = 3857;
    @EJB
    private ClosestLocationService closestLocationService;

    @Test
    public void shouldGetClosestLocation() {
        // given
        PointType point = new PointType(LONGITUDE, LATITUDE, DEFAULT_CRS);
        ClosestLocationSpatialRQ.LocationTypes locations = new ClosestLocationSpatialRQ.LocationTypes(newArrayList(LocationType.PORT));
        ClosestLocationSpatialRQ request = new ClosestLocationSpatialRQ(point, locations, UnitType.METER);

        // when
        ClosestLocationSpatialRS response = closestLocationService.getClosestLocations(request);

        //then
        assertNotNull(response);
        assertNotNull(response.getResponseMessage().getSuccess());
        assertFalse(response.getClosestLocations().getClosestLocations().isEmpty());

        ClosestLocationEntry closestLocationEntry = response.getClosestLocations().getClosestLocations().get(0);
        assertEquals("231", closestLocationEntry.getId());
        assertEquals(0.0, closestLocationEntry.getDistance(), 0.01);
        assertEquals(LocationType.PORT, closestLocationEntry.getLocationType());
        assertEquals(UnitType.METER, closestLocationEntry.getUnit());
    }

    @Test
    public void shouldGetClosestAreaWithCrsTransform() {
        // given
        PointType point = new PointType(LONGITUDE_2, LATITUDE_2, CRS);
        ClosestLocationSpatialRQ.LocationTypes locations = new ClosestLocationSpatialRQ.LocationTypes(newArrayList(LocationType.PORT));
        ClosestLocationSpatialRQ request = new ClosestLocationSpatialRQ(point, locations, UnitType.MILE);

        // when
        ClosestLocationSpatialRS response = closestLocationService.getClosestLocations(request);

        //then
        assertNotNull(response);
        assertNotNull(response.getResponseMessage().getSuccess());
        assertFalse(response.getClosestLocations().getClosestLocations().isEmpty());

        ClosestLocationEntry closestLocationEntry = response.getClosestLocations().getClosestLocations().get(0);
        assertEquals("189", closestLocationEntry.getId());
        assertEquals(367.705022199885, closestLocationEntry.getDistance(), 0.01);
        assertEquals(LocationType.PORT, closestLocationEntry.getLocationType());
        assertEquals(UnitType.MILE, closestLocationEntry.getUnit());
    }

}

