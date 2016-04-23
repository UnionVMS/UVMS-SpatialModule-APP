package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import eu.europa.ec.fisheries.uvms.BaseArquillianTest;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Coordinate;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialService;
import eu.europa.ec.fisheries.uvms.spatial.service.UserAreaService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UserAreaLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.UserAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.geojson.UserAreaGeoJsonDto;
import lombok.SneakyThrows;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class UserAreaServiceIT extends BaseArquillianTest {
	
	@EJB
	private UserAreaService userAreaService;


    @EJB
    private SpatialService spatialService;

    @Test
    @SneakyThrows
    public void testSearchUserAreaByFilter() {
		//Given
		List<UserAreaDto> userAreas = userAreaService.searchUserAreasByCriteria("rep_power", "EC", "area", false);
		
		assertNotNull(userAreas);
		assertFalse(userAreas.isEmpty());
	}
	
	@Test
    @SneakyThrows
	public void testSearchInvalidUserAreaByFilter() {
		//Given
		List<UserAreaDto> userAreas = userAreaService.searchUserAreasByCriteria("rep_power", "EC", "invalid", false);
		
		assertNotNull(userAreas);
		assertTrue(userAreas.isEmpty());
	}
	
	@Test
    @SneakyThrows
    public void testUserAreaDetails() {
		//Given
		Coordinate coordinate = new Coordinate(20.0535983848415, 31.1417484902222, 4326);
		List<UserAreaDto> userAreas = spatialService.getUserAreaDetailsWithExtentByLocation(coordinate, "rep_power");
		
		//Test
		assertNotNull(userAreas);
		assertFalse(userAreas.isEmpty());
		assertNotNull(userAreas.get(0).getExtent());
	}
	
	@Test
    @SneakyThrows
	public void testUserAreaDetailsForInvalidUserNameAndScopeName() {
		//Given
		Coordinate coordinate = new Coordinate(20.0535983848415, 31.1417484902222, 4326);
		List<UserAreaDto> userAreas = spatialService.getUserAreaDetailsWithExtentByLocation(coordinate, "00000");
		
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

	@Test
	public void shouldCreateAndUpdateUserArea() throws ServiceException, ParseException {
		// Given
		UserAreaGeoJsonDto userAreaDto = createUserArea("name", UUID.randomUUID().toString(), "desc", null);

		// When
		Long userAreaId = userAreaService.storeUserArea(userAreaDto, "rep_power");

		// Then
		assertNotNull(userAreaId);


		// Given
		userAreaDto = createUserArea("updated name", "updated-" + UUID.randomUUID().toString(), "updated desc", userAreaId);

		// When
		userAreaId = userAreaService.updateUserArea(userAreaDto, "rep_power", false, null);

		// Then
		assertNotNull(userAreaId);
	}

	private UserAreaGeoJsonDto createUserArea(String name, String datasetName, String desc, Long gid) throws ParseException {
		Geometry geometry = new WKTReader().read("MULTIPOLYGON(((1 1,5 1,5 5,1 5,1 1),(2 2, 3 2, 3 3, 2 3,2 2)),((3 3,6 2,6 4,3 3)))");
		geometry.setSRID(4326);

		UserAreaGeoJsonDto userAreaDto = new UserAreaGeoJsonDto();
		userAreaDto.setGeometry(geometry);
		userAreaDto.setId(gid);
		userAreaDto.setName(name);
		userAreaDto.setDatasetName(datasetName);
		userAreaDto.setDesc(desc);
		return userAreaDto;
	}

}
