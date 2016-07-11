/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.model.mapper;

import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMarshallException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SpatialModuleRequestMapperTest {

    private static final double LATITUDE = 45.11557, LONGITUDE = -7.14925;
    private static final int CRS = 3857;

    @Test
    public void testMapToCreateAreaByLocationRequest() throws JAXBException {
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("areaByLocationSpatialRQ.xml");
        JAXBContext jaxbContext = JAXBContext.newInstance(AreaByLocationSpatialRQ.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        AreaByLocationSpatialRQ request = (AreaByLocationSpatialRQ) jaxbUnmarshaller.unmarshal(resourceAsStream);

        PointType point = new PointType();
        point.setLongitude(LONGITUDE);
        point.setLatitude(LATITUDE);
        point.setCrs(CRS);

        try {
            String requestString = SpatialModuleRequestMapper.mapToCreateAreaByLocationRequest(point);
            StringReader reader = new StringReader(requestString);
            AreaByLocationSpatialRQ result = (AreaByLocationSpatialRQ) jaxbUnmarshaller.unmarshal(reader);

            assertEquals(result.getPoint().getCrs(), request.getPoint().getCrs());
            assertEquals(result.getPoint().getLatitude(), request.getPoint().getLatitude(), 0.01);
            assertEquals(result.getPoint().getLongitude(), request.getPoint().getLongitude(), 0.01);
            assertEquals(result.getMethod(), request.getMethod());

        } catch (SpatialModelMarshallException e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testMapToCreateAllAreaTypesRequest() throws JAXBException {

        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("allAreaTypesRequest.xml");
        JAXBContext jaxbContext = JAXBContext.newInstance(AllAreaTypesRequest.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        AllAreaTypesRequest request = (AllAreaTypesRequest) jaxbUnmarshaller.unmarshal(resourceAsStream);

        try {
            String requestString = SpatialModuleRequestMapper.mapToCreateAllAreaTypesRequest();
            StringReader reader = new StringReader(requestString);
            AllAreaTypesRequest result = (AllAreaTypesRequest) jaxbUnmarshaller.unmarshal(reader);
            assertEquals(result.getMethod(), request.getMethod());
        } catch (SpatialModelMarshallException e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testMapToCreateClosestAreaRequest() throws JAXBException {

        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("closestAreaSpatialRQ.xml");
        JAXBContext jaxbContext = JAXBContext.newInstance(ClosestAreaSpatialRQ.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        ClosestAreaSpatialRQ request = (ClosestAreaSpatialRQ) jaxbUnmarshaller.unmarshal(resourceAsStream);

        PointType point = new PointType();
        point.setLongitude(LONGITUDE);
        point.setLatitude(LATITUDE);
        point.setCrs(CRS);

        try {
            String requestString = SpatialModuleRequestMapper.mapToCreateClosestAreaRequest(point, UnitType.METERS, Arrays.asList(AreaType.EEZ));
            StringReader reader = new StringReader(requestString);
            ClosestAreaSpatialRQ result = (ClosestAreaSpatialRQ) jaxbUnmarshaller.unmarshal(reader);
            assertEquals(result.getPoint().getCrs(), request.getPoint().getCrs());
            assertEquals(result.getPoint().getLatitude(), request.getPoint().getLatitude(), 0.01);
            assertEquals(result.getPoint().getLongitude(), request.getPoint().getLongitude(), 0.01);
            assertEquals(result.getMethod(), request.getMethod());
            assertEquals(result.getUnit(), request.getUnit());
            assertEquals(result.getAreaTypes().getAreaTypes().get(0), request.getAreaTypes().getAreaTypes().get(0));
        } catch (SpatialModelMarshallException e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testMapToCreateClosestLocationRequest() throws JAXBException {

        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("closestLocationSpatialRQ.xml");
        JAXBContext jaxbContext = JAXBContext.newInstance(ClosestLocationSpatialRQ.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        ClosestLocationSpatialRQ request = (ClosestLocationSpatialRQ) jaxbUnmarshaller.unmarshal(resourceAsStream);

        PointType point = new PointType();
        point.setLongitude(LONGITUDE);
        point.setLatitude(LATITUDE);
        point.setCrs(CRS);

        try {
            String requestString = SpatialModuleRequestMapper.mapToCreateClosestLocationRequest(point, UnitType.METERS, Arrays.asList(LocationType.PORT));
            StringReader reader = new StringReader(requestString);
            ClosestLocationSpatialRQ result = (ClosestLocationSpatialRQ) jaxbUnmarshaller.unmarshal(reader);
            assertEquals(result.getPoint().getCrs(), request.getPoint().getCrs());
            assertEquals(result.getPoint().getLatitude(), request.getPoint().getLatitude(), 0.01);
            assertEquals(result.getPoint().getLongitude(), request.getPoint().getLongitude(), 0.01);
            assertEquals(result.getMethod(), request.getMethod());
            assertEquals(result.getUnit(), request.getUnit());
            assertEquals(result.getLocationTypes().getLocationTypes().get(0), request.getLocationTypes().getLocationTypes().get(0));
        } catch (SpatialModelMarshallException e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testMapToCreateSpatialEnrichmentRequest() throws JAXBException {

        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("spatialEnrichmentRQ.xml");
        JAXBContext jaxbContext = JAXBContext.newInstance(SpatialEnrichmentRQ.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        SpatialEnrichmentRQ request = (SpatialEnrichmentRQ) jaxbUnmarshaller.unmarshal(resourceAsStream);

        PointType point = new PointType();
        point.setLongitude(LONGITUDE);
        point.setLatitude(LATITUDE);
        point.setCrs(CRS);

        try {
            String requestString = SpatialModuleRequestMapper.mapToCreateSpatialEnrichmentRequest(point, UnitType.METERS, Arrays.asList(LocationType.PORT), Arrays.asList(AreaType.EEZ));
            StringReader reader = new StringReader(requestString);
            SpatialEnrichmentRQ result = (SpatialEnrichmentRQ) jaxbUnmarshaller.unmarshal(reader);
            assertEquals(result.getPoint().getCrs(), request.getPoint().getCrs());
            assertEquals(result.getPoint().getLatitude(), request.getPoint().getLatitude(), 0.01);
            assertEquals(result.getPoint().getLongitude(), request.getPoint().getLongitude(), 0.01);
            assertEquals(result.getMethod(), request.getMethod());
            assertEquals(result.getUnit(), request.getUnit());
            assertEquals(result.getLocationTypes().getLocationTypes().get(0), request.getLocationTypes().getLocationTypes().get(0));
            assertEquals(result.getAreaTypes().getAreaTypes().get(0), request.getAreaTypes().getAreaTypes().get(0));
        } catch (SpatialModelMarshallException e) {
            fail("Should not throw exception");
        }
    }

}