package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import java.util.List;
import java.util.Map;

import javax.ejb.EJB;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;

@RunWith(Arquillian.class)
public class SearchAreaServiceIT extends AbstractArquillianIT {
	
	@EJB
	private SearchAreaService searchAreaService;
	
	@Test
	public void shouldGetAreaByFilter() {
		List<Map<String, String>>  areaListMap = searchAreaService.getAreasByFilter("eez", "island");
		
		assertNotNull(areaListMap);
		assertFalse(areaListMap.isEmpty());
	}
	
	@Test
	public void shouldGetNoResult() {
		List<Map<String, String>>  areaListMap = searchAreaService.getAreasByFilter("eez", "INVALID_DATA");
		
		assertNotNull(areaListMap);
		assertFalse(!areaListMap.isEmpty());
	}
	
	@Test
	public void shouldThrowException() {
		try {
			List<Map<String, String>>  areaListMap = searchAreaService.getAreasByFilter("invalid", "island");
		
			assertNotNull(areaListMap);
			assertFalse(!areaListMap.isEmpty());
		} catch (Exception e) {
			assertNotNull(e);
		}
	}
}
