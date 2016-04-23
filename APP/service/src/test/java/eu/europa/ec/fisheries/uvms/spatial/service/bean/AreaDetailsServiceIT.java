package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import eu.europa.ec.fisheries.uvms.AbstractArquillianTest;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.EJBException;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.service.AreaService;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialService;
import lombok.SneakyThrows;
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
public class AreaDetailsServiceIT extends AbstractArquillianTest {
	
	@EJB
	private AreaService areaDetailsService;
    @EJB
    private SpatialService spatialService;


    @Test
    @SneakyThrows
	public void getEezAreaDetailsByCoordinates() {
		AreaTypeEntry areaCoordinate = new AreaTypeEntry();
		areaCoordinate.setAreaType(AreaType.EEZ);
		areaCoordinate.setLatitude(41.0);
		areaCoordinate.setLongitude(-9.5);
		areaCoordinate.setCrs(4326);
		List<AreaDetails> areaDetails = spatialService.getAreaDetailsByLocation(areaCoordinate);
		assertFalse(areaDetails.isEmpty());
		assertNotNull(areaDetails.get(0).getAreaProperties());
		assertEquals(areaDetails.get(0).getAreaProperties().isEmpty(), false);
	}
	
	@Test
	public void getAreaInvalidCoordinate() {
		try {
			AreaTypeEntry areaCoordinate = new AreaTypeEntry();
			areaCoordinate.setAreaType(AreaType.EEZ);
			areaCoordinate.setLatitude(410.0);
			areaCoordinate.setLongitude(-90.5);
			areaCoordinate.setCrs(4326);
            spatialService.getAreaDetailsByLocation(areaCoordinate);
		} catch (Exception e) {
			assertEquals(5010, ((SpatialServiceException)((EJBException) e).getCausedByException()).getError().getErrorCode().intValue());
		}
	}
	
	@Test
	public void getAreaInvalidCrsCodeTest() {
		try {
			AreaTypeEntry areaCoordinate = new AreaTypeEntry();
			areaCoordinate.setAreaType(AreaType.EEZ);
			areaCoordinate.setLatitude(41.0);
			areaCoordinate.setLongitude(-9.5);
			areaCoordinate.setCrs(43260);
            spatialService.getAreaDetailsByLocation(areaCoordinate);
		} catch (Exception e) {
			assertEquals(5002, ((SpatialServiceException)((EJBException) e).getCausedByException()).getError().getErrorCode().intValue());
		}
	}
	
	/**
	 * Test EEZ entity for valid response
	 */
	@Test
    @SneakyThrows
    public void getEezAreaDetailsTest() {
        AreaDetailsSpatialRequest areaDetailsSpatialRequest = new AreaDetailsSpatialRequest();
        AreaTypeEntry areaTypeEntry = new AreaTypeEntry();
        areaTypeEntry.setAreaType(AreaType.EEZ);
        areaTypeEntry.setId("1");
        areaDetailsSpatialRequest.setAreaType(areaTypeEntry);
        AreaDetails areaDetails = areaDetailsService.getAreaDetailsById(areaDetailsSpatialRequest.getAreaType());
		assertNotNull(areaDetails.getAreaProperties());
		assertEquals(areaDetails.getAreaProperties().isEmpty(), false);
	}
	
	/**
	 * Test RFMO entity for valid response
	 */
	@Test
    @SneakyThrows
    public void getRfmoAreaDetailsTest() {
        AreaDetailsSpatialRequest areaDetailsSpatialRequest = new AreaDetailsSpatialRequest();
        AreaTypeEntry areaTypeEntry = new AreaTypeEntry();
        areaTypeEntry.setAreaType(AreaType.RFMO);
        areaTypeEntry.setId("1");
        areaDetailsSpatialRequest.setAreaType(areaTypeEntry);
        AreaDetails areaDetails = areaDetailsService.getAreaDetailsById(areaDetailsSpatialRequest.getAreaType());
        assertNotNull(areaDetails.getAreaProperties());
		assertEquals(areaDetails.getAreaProperties().isEmpty(), false);
	}
	

	/**
	 * Test for invalid input
	 */
	@Test
	public void getAreaDetailsIncorrectIdTest() {
        try{
            AreaDetailsSpatialRequest areaDetailsSpatialRequest = new AreaDetailsSpatialRequest();
            AreaTypeEntry areaTypeEntry = new AreaTypeEntry();
            areaTypeEntry.setAreaType(AreaType.EEZ);
            areaTypeEntry.setId("invalid");
            areaDetailsSpatialRequest.setAreaType(areaTypeEntry);
            areaDetailsService.getAreaDetailsById(areaDetailsSpatialRequest.getAreaType());
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
            areaTypeEntry.setAreaType(null);
            areaTypeEntry.setId("1");
            areaDetailsSpatialRequest.setAreaType(areaTypeEntry);
            areaDetailsService.getAreaDetailsById(areaDetailsSpatialRequest.getAreaType());
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
            areaTypeEntry.setAreaType(AreaType.EEZ);
            areaTypeEntry.setId("10000000");
            areaDetailsSpatialRequest.setAreaType(areaTypeEntry);
            areaDetailsService.getAreaDetailsById(areaDetailsSpatialRequest.getAreaType());
            fail("Test should throw exception");
        } catch (Exception e){
            assertEquals(5010, ((SpatialServiceException)((EJBException) e).getCausedByException()).getError().getErrorCode().intValue());
        }
    }
	
}
