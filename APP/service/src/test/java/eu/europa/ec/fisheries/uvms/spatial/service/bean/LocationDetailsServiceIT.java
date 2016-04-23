package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import eu.europa.ec.fisheries.uvms.AbstractArquillianTest;
import javax.ejb.EJB;
import javax.ejb.EJBException;

import eu.europa.ec.fisheries.uvms.spatial.service.SpatialService;
import lombok.SneakyThrows;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;

@RunWith(Arquillian.class)
public class LocationDetailsServiceIT extends AbstractArquillianTest {
	
	@EJB
	private SpatialService locationDetailsService;
	
	@Test
    @SneakyThrows
    public void getPortDetailsByCoordinates() {
		LocationTypeEntry locationEntry = new LocationTypeEntry();
		locationEntry.setLocationType("port");
		locationEntry.setLatitude(41.0);
		locationEntry.setLongitude(-9.5);
		locationEntry.setCrs(4326);
		LocationDetails locationDetails = locationDetailsService.getLocationDetails(locationEntry);
		assertNotNull(locationDetails.getLocationProperties());
		assertEquals(locationDetails.getLocationProperties().isEmpty(), false);
	}
	
	@Test
	public void getAreaInvalidCoordinate() {
		try {
			LocationTypeEntry locationEntry = new LocationTypeEntry();
			locationEntry.setLocationType("port");
			locationEntry.setLatitude(410.0);
			locationEntry.setLongitude(-90.5);
			locationEntry.setCrs(4326);
			locationDetailsService.getLocationDetails(locationEntry);
		} catch (Exception e) {
			assertEquals(5010, ((SpatialServiceException)((EJBException) e).getCausedByException()).getError().getErrorCode().intValue());
		}
	}
	
	@Test
	public void getAreaInvalidCrsCodeTest() {
		try {
			LocationTypeEntry locationEntry = new LocationTypeEntry();
			locationEntry.setLocationType("port");
			locationEntry.setLatitude(41.0);
			locationEntry.setLongitude(-9.5);
			locationEntry.setCrs(43260);
			locationDetailsService.getLocationDetails(locationEntry);
		} catch (Exception e) {
			assertEquals(5002, ((SpatialServiceException)((EJBException) e).getCausedByException()).getError().getErrorCode().intValue());
		}
	}
	
	/**
	 * Test PORT entity for valid response
	 */
	@Test
    @SneakyThrows
    public void getPortDetailsTest() {
        LocationTypeEntry locationTypeEntry = new LocationTypeEntry();
        locationTypeEntry.setLocationType("PORT");
        locationTypeEntry.setId("1");
        LocationDetails locationDetails = locationDetailsService.getLocationDetails(locationTypeEntry);
		assertNotNull(locationDetails.getLocationProperties());
		assertEquals(locationDetails.getLocationProperties().isEmpty(), false);
	}
	
	/**
	 * Test for invalid input
	 */
	@Test
	public void getLocationDetailsIncorrectIdTest() {
        try{
        	LocationTypeEntry locationTypeEntry = new LocationTypeEntry();
            locationTypeEntry.setLocationType("PORT");
            locationTypeEntry.setId("invalid");
            locationDetailsService.getLocationDetails(locationTypeEntry);
            fail("Test should throw exception");
        } catch (Exception e){
            assertEquals(5012, ((SpatialServiceException)((EJBException) e).getCausedByException()).getError().getErrorCode().intValue());
        }
    }
	
	
	/**
	 * Test for non existing row in DB
	 */
    @Test
	public void getAreaDetailsInvalidRowTest() {
        try {
        	LocationTypeEntry locationTypeEntry = new LocationTypeEntry();
            locationTypeEntry.setLocationType("PORT");
            locationTypeEntry.setId("1000000");
            locationDetailsService.getLocationDetails(locationTypeEntry);
            fail("Test should throw exception");
        } catch (Exception e){
            assertEquals(5010, ((SpatialServiceException)((EJBException) e).getCausedByException()).getError().getErrorCode().intValue());
        }
    }
}
