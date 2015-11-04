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
        PointType pointType = new PointType();
        pointType.setLongitude(LONGITUDE);
        pointType.setLatitude(LATITUDE);
        pointType.setCrs(DEFAULT_CRS);
        SpatialEnrichmentRQ request = new SpatialEnrichmentRQ();
        SpatialEnrichmentRQ.AreaTypes areaTypes = new SpatialEnrichmentRQ.AreaTypes();
        areaTypes.getAreaTypes().addAll(newArrayList(AreaType.EEZ));
        request.setAreaTypes(areaTypes);
        SpatialEnrichmentRQ.LocationTypes locationTypes = new SpatialEnrichmentRQ.LocationTypes();
        locationTypes.getLocationTypes().addAll(newArrayList(LocationType.PORT));
        request.setLocationTypes(locationTypes);
        request.setPoint(pointType);
        request.setUnit(UnitType.METERS);

        // when
        SpatialEnrichmentRS response = enrichmentService.getSpatialEnrichment(request);

        //then
        assertNotNull(response);
        assertNotNull(response.getAreasByLocation());
        assertNotNull(response.getClosestAreas());
        assertNotNull(response.getClosestLocations());
        assertFalse(response.getClosestAreas().getClosestAreas().isEmpty());
        assertFalse(response.getClosestLocations().getClosestLocations().isEmpty());

        Area area = response.getClosestAreas().getClosestAreas().get(0);
        assertEquals("231", area.getId());
        assertEquals(0.0, area.getDistance(), 0.01);
        assertEquals(AreaType.EEZ, area.getAreaType());
        assertEquals(UnitType.METERS, area.getUnit());

        Location location = response.getClosestLocations().getClosestLocations().get(0);
        assertEquals("4623", location.getId());
        assertEquals(200508.9634032084, location.getDistance(), 0.01);
        assertEquals(LocationType.PORT, location.getLocationType());
        assertEquals(UnitType.METERS, location.getUnit());
    }

}
