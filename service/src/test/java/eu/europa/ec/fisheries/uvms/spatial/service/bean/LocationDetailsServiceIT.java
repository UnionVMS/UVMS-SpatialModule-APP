/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import javax.ejb.EJB;
import javax.ejb.EJBException;

import lombok.SneakyThrows;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.service.exception.SpatialServiceException;

@RunWith(Arquillian.class)
public class LocationDetailsServiceIT extends BaseSpatialArquillianTest {
	
	@EJB
	private SpatialService locationDetailsService;
	
	@Test
    @SneakyThrows
    public void getPortDetailsByCoordinates() {
		LocationTypeEntry locationEntry = new LocationTypeEntry();
		locationEntry.setLocationType("port");
		locationEntry.setLatitude(41.0);
		locationEntry.setLongitude(-9.5);
		locationEntry.setCrs(4326);
		LocationDetails locationDetails = locationDetailsService.getLocationDetails(locationEntry);
		assertNotNull(locationDetails.getLocationProperties());
		assertEquals(locationDetails.getLocationProperties().isEmpty(), false);
	}
	
	@Test
	public void getAreaInvalidCoordinate() {
		try {
			LocationTypeEntry locationEntry = new LocationTypeEntry();
			locationEntry.setLocationType("port");
			locationEntry.setLatitude(410.0);
			locationEntry.setLongitude(-90.5);
			locationEntry.setCrs(4326);
			locationDetailsService.getLocationDetails(locationEntry);
		} catch (Exception e) {
		}
	}
	
	@Test
	public void getAreaInvalidCrsCodeTest() {
		try {
			LocationTypeEntry locationEntry = new LocationTypeEntry();
			locationEntry.setLocationType("port");
			locationEntry.setLatitude(41.0);
			locationEntry.setLongitude(-9.5);
			locationEntry.setCrs(43260);
			locationDetailsService.getLocationDetails(locationEntry);
		} catch (ServiceException e) {
		}
	}
	
	/**
	 * Test PORT entity for valid response
	 */
	@Test
    @SneakyThrows
    public void getPortDetailsTest() {
        LocationTypeEntry locationTypeEntry = new LocationTypeEntry();
        locationTypeEntry.setLocationType("PORT");
        locationTypeEntry.setId("1");
        LocationDetails locationDetails = locationDetailsService.getLocationDetails(locationTypeEntry);
		assertNotNull(locationDetails.getLocationProperties());
		assertEquals(locationDetails.getLocationProperties().isEmpty(), false);
	}
	
	/**
	 * Test for invalid input
	 */
	@Test
	public void getLocationDetailsIncorrectIdTest() {
        try{
        	LocationTypeEntry locationTypeEntry = new LocationTypeEntry();
            locationTypeEntry.setLocationType("PORT");
            locationTypeEntry.setId("invalid");
            locationDetailsService.getLocationDetails(locationTypeEntry);
            fail("Test should throw exception");
        } catch (Exception e){
        	assertEquals(5012, ((SpatialServiceException)((EJBException) e).getCausedByException()).getError().getErrorCode().intValue());
        }
    }
	
	
	/**
	 * Test for non existing row in DB
	 */
    @Test
	public void getAreaDetailsInvalidRowTest() {
        try {
        	LocationTypeEntry locationTypeEntry = new LocationTypeEntry();
            locationTypeEntry.setLocationType("PORT");
            locationTypeEntry.setId("1000000");
            locationDetailsService.getLocationDetails(locationTypeEntry);
            fail("Test should throw exception");
        } catch (Exception e){
        	assertEquals(5010, ((SpatialServiceException)((EJBException) e).getCausedByException()).getError().getErrorCode().intValue());
        }
    }
}