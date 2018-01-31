/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package eu.europa.ec.fisheries.uvms.spatial.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eu.europa.ec.fisheries.uvms.BaseUnitilsTest;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.AreaService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.PortEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.exception.SpatialServiceException;
import lombok.SneakyThrows;
import org.geotools.geometry.jts.GeometryBuilder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
@Ignore("WILL FIX LATER")
public class LocationDetailsServiceTest extends BaseUnitilsTest {

	@Mock
	private SpatialRepository repository;
	
	@InjectMocks
	private AreaService areaService;

	@Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }
	
	/**
	 * Test Port by coordinates
	 */
	@Test
    @SneakyThrows
    public void getPortDetailsByCoordinates() {
		setMocks(getMockedPortsEntity());
		mockEntityByCoordinate(Arrays.asList(getMockedPortsEntity()));
		LocationTypeEntry locationEntry = new LocationTypeEntry();
		locationEntry.setLocationType("port");
		locationEntry.setLatitude(41.0);
		locationEntry.setLongitude(-9.5);
		locationEntry.setCrs(4326);
		//LocationDetails locationDetails = locationDetailsServiceBean.getLocationDetails(locationEntry);
		//assertNotNull(locationDetails.getLocationProperties());
		//assertEquals(locationDetails.getLocationProperties().isEmpty(), false);
	}
	
	/**
	 * Invalid coordinate test
	 */
	@Test
    @SneakyThrows
	public void shouldReturnEmptyResponseWhenNoAreaFound() {
		// given
		LocationTypeEntry locationEntry = createLocationTypeEntry();

		// when
		//LocationDetails locationDetails = locationDetailsServiceBean.getLocationDetails(locationEntry);

		// then
		//assertNotNull(locationDetails);
		//assertEquals("port", locationDetails.getLocationType().getLocationType());
		//assertEquals(0, locationDetails.getLocationProperties().size());
	}

	private LocationTypeEntry createLocationTypeEntry() {
		setMocks(getMockedPortsEntity());
		mockEntityByCoordinate(new ArrayList());
		LocationTypeEntry locationEntry = new LocationTypeEntry();
		locationEntry.setLocationType("port");
		locationEntry.setLatitude(410.0);
		locationEntry.setLongitude(-90.5);
		locationEntry.setCrs(4326);
		return locationEntry;
	}

	/**
	 * Test Port entity for valid response
	 */
	@Test
    @SneakyThrows
    public void getPortDetailsTest() {
		setMocks(getMockedPortsEntity());	
		LocationTypeEntry locationEntry = new LocationTypeEntry();
		locationEntry.setId("1");
		locationEntry.setLocationType("PORT");        
        //LocationDetails locationDetails = locationDetailsServiceBean.getLocationDetails(locationEntry);
        //assertNotNull(locationDetails);
		//List<LocationProperty> list = locationDetails.getLocationProperties();
		//assertEquals(list.isEmpty(), false);
	}	
	
	/**
	 * Test for invalid Entry in input
	 */
	@Test(expected=SpatialServiceException.class)
    @SneakyThrows
    public void invalidRowTest() {
		setMocks(getMockedPortsEntity());		
		LocationTypeEntry locationEntry = new LocationTypeEntry();
		locationEntry.setId("INVALID_ROW");
		locationEntry.setLocationType("PORT");
		areaService.getLocationDetails(locationEntry);
	}	
	
	/**
	 * Test for non existing row in DB
	 */
    @SneakyThrows
    @Test(expected=SpatialServiceException.class)
	public void nonExistingRowTest() {
		setMocks(null);	
		LocationTypeEntry locationEntry = new LocationTypeEntry();
		locationEntry.setId("100000");
		locationEntry.setLocationType("PORT");
		areaService.getLocationDetails(locationEntry);
	}
	
	/**
	 * Test for non existing row in DB
	 */
	@Test(expected=SpatialServiceException.class)
    @SneakyThrows
    public void invalidTypeTest() {
		LocationTypeEntry locationEntry = new LocationTypeEntry();
		locationEntry.setId("1");
		locationEntry.setLocationType("INVALID_PORT");
		areaService.getLocationDetails(locationEntry);
	}
	
	private void setMocks(PortEntity portsEntity) {
		List<AreaLocationTypesEntity> locationEntities = new ArrayList<AreaLocationTypesEntity>();
		locationEntities.add(getMockAreaTypeEntity(LocationType.PORT.value()));
		mockCrudServiceBean(locationEntities, portsEntity);
	}
	
	@SuppressWarnings("unchecked")
    @SneakyThrows
	private void mockCrudServiceBean(List<AreaLocationTypesEntity> returnList, Object entity) {
		//Mockito.when(repository.findEntityByNamedQuery(Mockito.any(Class.class), Mockito.any(String.class), Mockito.any(Map.class), Mockito.any(Integer.class))).thenReturn(returnList);
		//Mockito.when(repository.findEntityById(Mockito.any(Class.class), Mockito.any(Object.class))).thenReturn(entity);
	}
	
	private void mockEntityByCoordinate(List list) {
		//Mockito.when(repository.findAreaByCoordinates(Mockito.any(Point.class), Mockito.any(String.class))).thenReturn(list);
	}
	
	private AreaLocationTypesEntity getMockAreaTypeEntity(String typeName) {
		AreaLocationTypesEntity areaEntity = Mockito.mock(AreaLocationTypesEntity.class);
		Mockito.when(areaEntity.getTypeName()).thenReturn(typeName.toUpperCase());
		return areaEntity;
	}
	
	private PortEntity getMockedPortsEntity() {
		PortEntity portsEntity = new PortEntity();
		portsEntity.setName("TEST");
		portsEntity.setCode("Code");
		portsEntity.setGeom(new GeometryBuilder().point());
		return portsEntity;
	}
}