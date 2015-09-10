package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import static com.google.common.collect.Lists.newArrayList;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Created by Michal Kopyczok on 09-Sep-15.
 */
@RunWith(Arquillian.class)
public class SpatialEnrichmentServiceIT extends AbstractArquillianIT {
    private static final double LATITUDE = 32.85615;
    private static final double LONGITUDE = -10.74118;
    private static final int DEFAULT_CRS = 4326;
    @EJB
    private SpatialEnrichmentService enrichmentService;

    @Test
    public void shouldGetClosestArea() {
        // given
        PointType point = new PointType(LONGITUDE, LATITUDE, DEFAULT_CRS);
        SpatialEnrichmentRQ.AreaTypes areas = new SpatialEnrichmentRQ.AreaTypes(newArrayList(AreaType.EEZ));
        SpatialEnrichmentRQ.LocationTypes locations = new SpatialEnrichmentRQ.LocationTypes(newArrayList(LocationType.PORT));
        SpatialEnrichmentRQ request = new SpatialEnrichmentRQ(point, areas, locations, UnitType.METERS);

        // when
        SpatialEnrichmentRS response = enrichmentService.getSpatialEnrichment(request);

        //then
        assertNotNull(response);
        assertNotNull(response.getResponseMessage().getSuccess());
        assertNotNull(response.getAreasByLocation());
        assertNotNull(response.getClosestAreas());
        assertNotNull(response.getClosestLocations());
        assertFalse(response.getClosestAreas().getClosestArea().isEmpty());
        assertFalse(response.getClosestLocations().getClosestLocations().isEmpty());

        ClosestAreaEntry closestAreaEntry = response.getClosestAreas().getClosestArea().get(0);
        assertEquals("231", closestAreaEntry.getId());
        assertEquals(0.0, closestAreaEntry.getDistance(), 0.01);
        assertEquals(AreaType.EEZ, closestAreaEntry.getAreaType());
        assertEquals(UnitType.METERS, closestAreaEntry.getUnit());

        ClosestLocationEntry closestLocationEntry = response.getClosestLocations().getClosestLocations().get(0);
        assertEquals("231", closestLocationEntry.getId());
        assertEquals(0.0, closestLocationEntry.getDistance(), 0.01);
        assertEquals(LocationType.PORT, closestLocationEntry.getLocationType());
        assertEquals(UnitType.METERS, closestLocationEntry.getUnit());
    }

}
