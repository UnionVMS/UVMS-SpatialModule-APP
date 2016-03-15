package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
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
public class ClosestLocationServiceIT extends AbstractArquillianIT {

    private static final double LATITUDE = 32.85615;
    private static final double LATITUDE_2 = 45.11557;
    private static final double LONGITUDE = -10.74118;
    private static final double LONGITUDE_2 = -7.14925;

    private static final int DEFAULT_CRS = 4326;
    private static final int CRS = 3857;
    @EJB
    private SpatialService closestLocationService;

    @Test
    @SneakyThrows
    public void shouldGetClosestLocation() {
        // given
        PointType point = new PointType();
        point.setLongitude(LONGITUDE);
        point.setLatitude(LATITUDE);
        point.setCrs(DEFAULT_CRS);
        ClosestLocationSpatialRQ request = new ClosestLocationSpatialRQ();
        ClosestLocationSpatialRQ.LocationTypes locationTypes = new ClosestLocationSpatialRQ.LocationTypes();
        locationTypes.getLocationTypes().addAll(newArrayList(LocationType.PORT));
        request.setLocationTypes(locationTypes);
        request.setPoint(point);
        request.setUnit(UnitType.METERS);

        // when
        List<Location> closestLocations = closestLocationService.getClosestLocationByLocationType(request);

        //then
        assertNotNull(closestLocations);
        assertFalse(closestLocations.isEmpty());

        Location location = closestLocations.get(0);
        assertEquals("4627", location.getId());
        assertEquals(200508.9634032084, location.getDistance(), 0.01);
        assertEquals("MAJFL", location.getCode());
        assertEquals("Jorf Lasfar", location.getName());
        assertEquals(LocationType.PORT, location.getLocationType());
        assertEquals(UnitType.METERS, location.getUnit());
    }

    @Test
    @SneakyThrows
    public void shouldGetClosestAreaWithCrsTransform() {
        // given
        PointType point = new PointType();
        point.setLongitude(LONGITUDE_2);
        point.setLatitude(LATITUDE_2);
        point.setCrs(CRS);
        ClosestLocationSpatialRQ request = new ClosestLocationSpatialRQ();
        ClosestLocationSpatialRQ.LocationTypes locationTypes = new ClosestLocationSpatialRQ.LocationTypes();
        locationTypes.getLocationTypes().addAll(newArrayList(LocationType.PORT));
        request.setLocationTypes(locationTypes);
        request.setPoint(point);
        request.setUnit(UnitType.METERS);

        // when
        List<Location> closestLocations = closestLocationService.getClosestLocationByLocationType(request);

        //then
        assertNotNull(closestLocations);
        assertFalse(closestLocations.isEmpty());

        Location location = closestLocations.get(0);
        assertEquals("3751", location.getId());
        assertEquals(626489.5020191947, location.getDistance(), 0.01);
        assertEquals("GHTEM", location.getCode());
        assertEquals("Tema", location.getName());
        assertEquals(LocationType.PORT, location.getLocationType());
        assertEquals(UnitType.METERS, location.getUnit());
    }

}

