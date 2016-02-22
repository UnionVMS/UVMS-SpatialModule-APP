package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;

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
	
	@Test
	public void shouldGetSelectedColumns() {
		AreaTypeEntry areaTypeEntry = new AreaTypeEntry();
		areaTypeEntry.setAreaType(AreaType.EEZ);
		areaTypeEntry.setId("1");
		List<Map<String, String>> columnListMap = searchAreaService.getSelectedAreaColumns(Arrays.asList(areaTypeEntry));
		assertNotNull(columnListMap);
		assertFalse(columnListMap.isEmpty());
	}
	
	@Test
	public void shouldNotGetUnknownColumnsMap() {
		try {
			AreaTypeEntry areaTypeEntry = new AreaTypeEntry();
			areaTypeEntry.setAreaType(AreaType.EEZ);
			areaTypeEntry.setId("100000000000000");
			searchAreaService.getSelectedAreaColumns(Arrays.asList(areaTypeEntry));			
		} catch (Exception e) {
			assertNotNull(e);
		}
	}
	
	@Test
	public void shouldNotGetInvalidColumnsMap() {
		try {
			AreaTypeEntry areaTypeEntry = new AreaTypeEntry();
			areaTypeEntry.setAreaType(null);
			areaTypeEntry.setId("1");
			searchAreaService.getSelectedAreaColumns(Arrays.asList(areaTypeEntry));			
		} catch (Exception e) {
			assertNotNull(e);
		}
	}
}
