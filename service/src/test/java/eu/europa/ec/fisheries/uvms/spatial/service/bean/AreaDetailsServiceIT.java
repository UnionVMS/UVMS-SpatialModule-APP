package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.EJBException;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetailsSpatialRequest;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;

/**
 * @author padhyad
 * Arquillian integration test for GetAreaDetails API
 */
@RunWith(Arquillian.class)
public class AreaDetailsServiceIT extends AbstractArquillianIT {
	
	@EJB
	private AreaDetailsService areaDetailsService;
	

	@Test
	public void getEezAreaDetailsByCoordinates() {
		AreaTypeEntry areaCoordinate = new AreaTypeEntry();
		areaCoordinate.setAreaType("eez");
		areaCoordinate.setLatitude(41.0);
		areaCoordinate.setLongitude(-9.5);
		areaCoordinate.setCrs(4326);
		List<AreaDetails> areaDetails = areaDetailsService.getAreaDetailsByLocation(areaCoordinate);
		assertFalse(areaDetails.isEmpty());
		assertNotNull(areaDetails.get(0).getAreaProperty());
		assertEquals(areaDetails.get(0).getAreaProperty().isEmpty(), false);
	}
	
	@Test
	public void getAreaInvalidCoordinate() {
		try {
			AreaTypeEntry areaCoordinate = new AreaTypeEntry();
			areaCoordinate.setAreaType("eez");
			areaCoordinate.setLatitude(410.0);
			areaCoordinate.setLongitude(-90.5);
			areaCoordinate.setCrs(4326);
			areaDetailsService.getAreaDetailsByLocation(areaCoordinate);
		} catch (Exception e) {
			assertEquals(5010, ((SpatialServiceException)((EJBException) e).getCausedByException()).getError().getErrorCode().intValue());
		}
	}
	
	@Test
	public void getAreaInvalidCrsCodeTest() {
		try {
			AreaTypeEntry areaCoordinate = new AreaTypeEntry();
			areaCoordinate.setAreaType("eez");
			areaCoordinate.setLatitude(41.0);
			areaCoordinate.setLongitude(-9.5);
			areaCoordinate.setCrs(43260);
			areaDetailsService.getAreaDetailsByLocation(areaCoordinate);
		} catch (Exception e) {
			assertEquals(5002, ((SpatialServiceException)((EJBException) e).getCausedByException()).getError().getErrorCode().intValue());
		}
	}
	
	/**
	 * Test EEZ entity for valid response
	 */
	@Test
	public void getEezAreaDetailsTest() {
        AreaDetailsSpatialRequest areaDetailsSpatialRequest = new AreaDetailsSpatialRequest();
        AreaTypeEntry areaTypeEntry = new AreaTypeEntry();
        areaTypeEntry.setAreaType("eez");
        areaTypeEntry.setId("1");
        areaDetailsSpatialRequest.setAreaType(areaTypeEntry);
        AreaDetails areaDetails = areaDetailsService.getAreaDetails(areaDetailsSpatialRequest);
		assertNotNull(areaDetails.getAreaProperty());
		assertEquals(areaDetails.getAreaProperty().isEmpty(), false);
	}
	
	/**
	 * Test RFMO entity for valid response
	 */
	@Test
	public void getRfmoAreaDetailsTest() {
        AreaDetailsSpatialRequest areaDetailsSpatialRequest = new AreaDetailsSpatialRequest();
        AreaTypeEntry areaTypeEntry = new AreaTypeEntry();
        areaTypeEntry.setAreaType("rfmo");
        areaTypeEntry.setId("1");
        areaDetailsSpatialRequest.setAreaType(areaTypeEntry);
        AreaDetails areaDetails = areaDetailsService.getAreaDetails(areaDetailsSpatialRequest);
        assertNotNull(areaDetails.getAreaProperty());
		assertEquals(areaDetails.getAreaProperty().isEmpty(), false);
	}
	

	/**
	 * Test for invalid input
	 */
	@Test
	public void getAreaDetailsIncorrectIdTest() {
        try{
            AreaDetailsSpatialRequest areaDetailsSpatialRequest = new AreaDetailsSpatialRequest();
            AreaTypeEntry areaTypeEntry = new AreaTypeEntry();
            areaTypeEntry.setAreaType("eez");
            areaTypeEntry.setId("invalid");
            areaDetailsSpatialRequest.setAreaType(areaTypeEntry);
            areaDetailsService.getAreaDetails(areaDetailsSpatialRequest);
            fail("Test should throw exception");
        } catch (Exception e){
            assertEquals(5012, ((SpatialServiceException)((EJBException) e).getCausedByException()).getError().getErrorCode().intValue());
        }
    }
	

	/**
	 * Test for Invalid entity in the DB
	 */
    @Test
	public void getAreaDetailsInvalidEntityTest() {
        try {
            AreaDetailsSpatialRequest areaDetailsSpatialRequest = new AreaDetailsSpatialRequest();
            AreaTypeEntry areaTypeEntry = new AreaTypeEntry();
            areaTypeEntry.setAreaType("Invalid");
            areaTypeEntry.setId("1");
            areaDetailsSpatialRequest.setAreaType(areaTypeEntry);
            areaDetailsService.getAreaDetails(areaDetailsSpatialRequest);
            fail("Test should throw exception");
        } catch (Exception e){
            assertEquals(5009, ((SpatialServiceException)((EJBException) e).getCausedByException()).getError().getErrorCode().intValue());
        }

	}
	
	/**
	 * Test for non existing row in DB
	 */
    @Test
	public void GetAreaDetailsInvalidRowTest() {
        try {
            AreaDetailsSpatialRequest areaDetailsSpatialRequest = new AreaDetailsSpatialRequest();
            AreaTypeEntry areaTypeEntry = new AreaTypeEntry();
            areaTypeEntry.setAreaType("eez");
            areaTypeEntry.setId("10000000");
            areaDetailsSpatialRequest.setAreaType(areaTypeEntry);
            areaDetailsService.getAreaDetails(areaDetailsSpatialRequest);
            fail("Test should throw exception");
        } catch (Exception e){
            assertEquals(5010, ((SpatialServiceException)((EJBException) e).getCausedByException()).getError().getErrorCode().intValue());
        }
    }
	
}
