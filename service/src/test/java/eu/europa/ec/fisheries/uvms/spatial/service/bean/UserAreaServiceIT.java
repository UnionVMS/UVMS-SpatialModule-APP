/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Coordinate;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialService;
import eu.europa.ec.fisheries.uvms.spatial.service.UserAreaService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.UserAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.geojson.UserAreaGeoJsonDto;
import lombok.SneakyThrows;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
public class UserAreaServiceIT extends BaseSpatialArquillianTest {
	
	@EJB
	private UserAreaService userAreaService;

    @EJB
    private SpatialService spatialService;
	
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