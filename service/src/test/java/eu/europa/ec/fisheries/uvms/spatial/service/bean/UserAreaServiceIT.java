package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UserAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UserAreaLayerDto;

@RunWith(Arquillian.class)
public class UserAreaServiceIT extends AbstractArquillianIT {
	
	@EJB
	private UserAreaService userAreaService;
	
	@Test
	public void testUserAreaDetails() {
		//Given
		List<UserAreaDto> userAreas = userAreaService.getUserAreaDetails("12345", "98732");
		
		//Test
		assertNotNull(userAreas);
		assertFalse(userAreas.isEmpty());
		assertNotNull(userAreas.get(0).getExtent());
	}
	
	@Test
	public void testUserAreaDetailsForInvalidUserNameAndScopeName() {
		//Given
		List<UserAreaDto> userAreas = userAreaService.getUserAreaDetails("00000", "00000");
		
		//Test
		assertNotNull(userAreas);
		assertTrue(userAreas.isEmpty());
	}
	
	@Test
	public void testUserAreaLayerMapping() {
		//Given
		UserAreaLayerDto userAreaLayer = userAreaService.getUserAreaLayerDefination("12345", "98732");
		
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
