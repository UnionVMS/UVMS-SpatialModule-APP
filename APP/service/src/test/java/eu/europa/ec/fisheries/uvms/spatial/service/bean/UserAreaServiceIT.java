package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.ejb.EJB;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Coordinate;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.UserAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UserAreaLayerDto;

@RunWith(Arquillian.class)
public class UserAreaServiceIT extends AbstractArquillianIT {
	
	@EJB
	private UserAreaService userAreaService;
	
	@Test
	public void testSearchUserAreaByFilter() {
		//Given
		List<UserAreaDto> userAreas = userAreaService.searchUserAreasByCriteria("rep_power", "EC", "area", false);
		
		assertNotNull(userAreas);
		assertFalse(userAreas.isEmpty());
	}
	
	@Test
	public void testSearchInvalidUserAreaByFilter() {
		//Given
		List<UserAreaDto> userAreas = userAreaService.searchUserAreasByCriteria("rep_power", "EC", "invalid", false);
		
		assertNotNull(userAreas);
		assertTrue(userAreas.isEmpty());
	}
	
	@Test
	public void testUserAreaDetails() {
		//Given
		Coordinate coordinate = new Coordinate(20.0535983848415, 31.1417484902222, 4326);
		List<UserAreaDto> userAreas = userAreaService.getUserAreaDetailsWithExtentByLocation(coordinate, "rep_power");
		
		//Test
		assertNotNull(userAreas);
		assertFalse(userAreas.isEmpty());
		assertNotNull(userAreas.get(0).getExtent());
	}
	
	@Test
	public void testUserAreaDetailsForInvalidUserNameAndScopeName() {
		//Given
		Coordinate coordinate = new Coordinate(20.0535983848415, 31.1417484902222, 4326);
		List<UserAreaDto> userAreas = userAreaService.getUserAreaDetailsWithExtentByLocation(coordinate, "00000");
		
		//Test
		assertNotNull(userAreas);
		assertTrue(userAreas.isEmpty());
	}
	
	@Test
	public void testUserAreaLayerMapping() {
		//Given
		UserAreaLayerDto userAreaLayer = userAreaService.getUserAreaLayerDefination("rep_power", "EC");
		
		//Test
		assertNotNull(userAreaLayer);
		assertFalse(userAreaLayer.getIdList().isEmpty());
	}

	@Test
	public void testUserAreaLayerMappingForInvalidUserNameAndScopeName() {
		//Given
		UserAreaLayerDto userAreaLayer = userAreaService.getUserAreaLayerDefination("00000", "00000");
		
		//Test
		assertNotNull(userAreaLayer);
		assertTrue(userAreaLayer.getIdList().isEmpty());
	}
}
