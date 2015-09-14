package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetailsSpatialRequest;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import javax.ejb.EJBException;

import static org.junit.Assert.*;

/**
 * @author padhyad
 * Arquillian integration test for GetAreaDetails API
 */
@RunWith(Arquillian.class)
public class AreaDetailsServiceIT extends AbstractArquillianIT {
	
	@EJB
	private AreaDetailsService areaDetailsService;
	
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
