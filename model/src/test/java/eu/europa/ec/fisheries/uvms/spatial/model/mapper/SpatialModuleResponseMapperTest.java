/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.model.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMapperException;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMarshallException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Area;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaByLocationSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaExtendedIdentifierType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeNamesSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreasByLocationType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreasType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestLocationSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestLocationsType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.Location;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialEnrichmentRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.UnitType;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

public class SpatialModuleResponseMapperTest {

    @Mock
    private TextMessage textMessage;

    @Test
    public void testMapToAreasByLocationTypeFromResponseWithResponseNull() {
        try {
            SpatialModuleResponseMapper.mapToAreasByLocationTypeFromResponse(null, "123245");
            fail("test should fail");
        } catch (SpatialModelMapperException e) {
            assertEquals("Error when validating response in ResponseMapper: Response is Null", e.getMessage());
        }
    }

    @Test
    public void testMapToAreasByLocationTypeFromResponseWithCorrelationIdNull() {
        try {
            TextMessage mock = Mockito.mock(TextMessage.class);
            SpatialModuleResponseMapper.mapToAreasByLocationTypeFromResponse(mock, null);
            fail("test should fail");
        } catch (SpatialModelMapperException e) {
            assertEquals("No correlationId in response (Null) . Expected was: null", e.getMessage());
        }
    }

    @Test
    public void testMapToAreasByLocationTypeFromResponse() {
        try {

            InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("areaByLocationSpatialRS.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(AreaByLocationSpatialRS.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            AreaByLocationSpatialRS response = (AreaByLocationSpatialRS) jaxbUnmarshaller.unmarshal(resourceAsStream);

            jaxbContext = JAXBContext.newInstance(response.getClass());
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter sw = new StringWriter();
            marshaller.marshal(response, sw);

            TextMessage mock = Mockito.mock(TextMessage.class);
            when(mock.getJMSCorrelationID()).thenReturn("666");
            when(mock.getText()).thenReturn(sw.toString());

            AreasByLocationType areasByLocationType = SpatialModuleResponseMapper.mapToAreasByLocationTypeFromResponse(mock, "666");
            assertEquals("2", areasByLocationType.getAreas().get(0).getId());
            assertEquals("EEZ", areasByLocationType.getAreas().get(0).getAreaType().toString());
        } catch (SpatialModelMapperException | JMSException | JAXBException e) {
            fail("test should not throw these exceptions");
        }
    }

    @Test
    public void testMapToAreasByLocationTypeFromResponseWithWrongCorrelationId() {
        try {
            TextMessage mock = Mockito.mock(TextMessage.class);
            when(mock.getJMSCorrelationID()).thenReturn("555");
            SpatialModuleResponseMapper.mapToAreasByLocationTypeFromResponse(mock, "666");
            fail("test should fail");
        } catch (SpatialModelMapperException e) {
            assertEquals("Wrong correlationId in response. Expected was: 666 But actual was: 555", e.getMessage());
        } catch (JMSException e) {
            fail("test should not throw jmsexception");
        }
    }

    @Test
    public void testMapAreaByLocationResponse() throws JAXBException {

        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("areaByLocationSpatialRS.xml");
        JAXBContext jaxbContext = JAXBContext.newInstance(AreaByLocationSpatialRS.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        AreaByLocationSpatialRS response = (AreaByLocationSpatialRS) jaxbUnmarshaller.unmarshal(resourceAsStream);

        List<AreaExtendedIdentifierType> entryList = new ArrayList<>();
        AreaExtendedIdentifierType entry = new AreaExtendedIdentifierType();
        entry.setAreaType(AreaType.EEZ);
        entry.setId("2");
        entry.setName("EEZ name");
        entry.setCode("ECD");
        entryList.add(entry);
        try {
            String responseString = SpatialModuleResponseMapper.mapAreaByLocationResponse(entryList);
            StringReader reader = new StringReader(responseString);
            AreaByLocationSpatialRS result = (AreaByLocationSpatialRS) jaxbUnmarshaller.unmarshal(reader);
            assertEquals(result.getAreasByLocation().getAreas().get(0).getAreaType(), response.getAreasByLocation().getAreas().get(0).getAreaType());
            assertEquals(result.getAreasByLocation().getAreas().get(0).getId(), response.getAreasByLocation().getAreas().get(0).getId());
        } catch (SpatialModelMarshallException e) {
            fail("should not throw error");
        }
    }

    @Test
    public void testMapAreaTypeNamesResponse() throws JAXBException {

        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("areaTypeNamesSpatialRS.xml");
        JAXBContext jaxbContext = JAXBContext.newInstance(AreaTypeNamesSpatialRS.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        AreaTypeNamesSpatialRS response = (AreaTypeNamesSpatialRS) jaxbUnmarshaller.unmarshal(resourceAsStream);

        List<String> names = new ArrayList<>();
        names.add("EEZ");

        try {
            String responseString = SpatialModuleResponseMapper.mapAreaTypeNamesResponse(names);
            StringReader reader = new StringReader(responseString);
            AreaTypeNamesSpatialRS result = (AreaTypeNamesSpatialRS) jaxbUnmarshaller.unmarshal(reader);
            assertEquals(result.getAreaTypes().getAreaTypes().get(0), response.getAreaTypes().getAreaTypes().get(0));
        } catch (SpatialModelMarshallException e) {
            fail("should not throw error");
        }
    }

    @Test
    public void testMapClosestLocationResponse() throws JAXBException {

        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("closestLocationSpatialRS.xml");
        JAXBContext jaxbContext = JAXBContext.newInstance(ClosestLocationSpatialRS.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        ClosestLocationSpatialRS response = (ClosestLocationSpatialRS) jaxbUnmarshaller.unmarshal(resourceAsStream);

        List<Location> locations = new ArrayList<>();
        Location location = new Location();
        location.setDistance(12.0);
        location.setId("2");
        location.setUnit(UnitType.METERS);
        location.setLocationType(LocationType.PORT);
        locations.add(location);

        try {
            String responseString = SpatialModuleResponseMapper.mapClosestLocationResponse(locations);
            StringReader reader = new StringReader(responseString);
            ClosestLocationSpatialRS result = (ClosestLocationSpatialRS) jaxbUnmarshaller.unmarshal(reader);
            assertEquals(result.getClosestLocations().getClosestLocations().get(0).getId(), response.getClosestLocations().getClosestLocations().get(0).getId());
            assertEquals(result.getClosestLocations().getClosestLocations().get(0).getDistance(), response.getClosestLocations().getClosestLocations().get(0).getDistance(), 0.01);
            assertEquals(result.getClosestLocations().getClosestLocations().get(0).getUnit(), response.getClosestLocations().getClosestLocations().get(0).getUnit());
            assertEquals(result.getClosestLocations().getClosestLocations().get(0).getLocationType(), response.getClosestLocations().getClosestLocations().get(0).getLocationType());
        } catch (SpatialModelMarshallException e) {
            fail("should not throw error");
        }
    }

    @Test
    public void testMapClosestAreaResponse() throws JAXBException {

        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("closestAreaSpatialRS.xml");
        JAXBContext jaxbContext = JAXBContext.newInstance(ClosestAreaSpatialRS.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        ClosestAreaSpatialRS response = (ClosestAreaSpatialRS) jaxbUnmarshaller.unmarshal(resourceAsStream);

        List<Area> areas = new ArrayList<>();
        Area area = new Area();
        area.setDistance(12.0);
        area.setId("2");
        area.setUnit(UnitType.METERS);
        area.setAreaType(AreaType.EEZ);
        areas.add(area);

        try {
            String responseString = SpatialModuleResponseMapper.mapClosestAreaResponse(areas);
            StringReader reader = new StringReader(responseString);
            ClosestAreaSpatialRS result = (ClosestAreaSpatialRS) jaxbUnmarshaller.unmarshal(reader);
            assertEquals(result.getClosestArea().getClosestAreas().get(0).getId(), response.getClosestArea().getClosestAreas().get(0).getId());
            assertEquals(result.getClosestArea().getClosestAreas().get(0).getDistance(), response.getClosestArea().getClosestAreas().get(0).getDistance(), 0.01);
            assertEquals(result.getClosestArea().getClosestAreas().get(0).getUnit(), response.getClosestArea().getClosestAreas().get(0).getUnit());
            assertEquals(result.getClosestArea().getClosestAreas().get(0).getAreaType(), response.getClosestArea().getClosestAreas().get(0).getAreaType());
        } catch (SpatialModelMarshallException e) {
            fail("should not throw error");
        }
    }

    @Test
    public void testMapEnrichmentResponse() throws JAXBException {

        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("spatialEnrichmentRS.xml");
        JAXBContext jaxbContext = JAXBContext.newInstance(SpatialEnrichmentRS.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        SpatialEnrichmentRS response = (SpatialEnrichmentRS) jaxbUnmarshaller.unmarshal(resourceAsStream);

        List<Area> areas = new ArrayList<>();
        Area area = new Area();
        area.setDistance(12.0);
        area.setId("2");
        area.setUnit(UnitType.METERS);
        area.setAreaType(AreaType.EEZ);
        areas.add(area);

        List<Location> locations = new ArrayList<>();
        Location location = new Location();
        location.setDistance(12.0);
        location.setId("2");
        location.setUnit(UnitType.METERS);
        location.setLocationType(LocationType.PORT);
        locations.add(location);

        SpatialEnrichmentRS enrichment = new SpatialEnrichmentRS();
        ClosestAreasType closestAreasType = new ClosestAreasType();
        closestAreasType.getClosestAreas().addAll(areas);
        enrichment.setClosestAreas(closestAreasType);
        ClosestLocationsType closestLocationsType = new ClosestLocationsType();
        closestLocationsType.getClosestLocations().addAll(locations);
        enrichment.setClosestLocations(closestLocationsType);


        try {
            String responseString = SpatialModuleResponseMapper.mapEnrichmentResponse(enrichment);
            StringReader reader = new StringReader(responseString);
            SpatialEnrichmentRS result = (SpatialEnrichmentRS) jaxbUnmarshaller.unmarshal(reader);
            assertEquals(result.getClosestAreas().getClosestAreas().get(0).getId(), response.getClosestAreas().getClosestAreas().get(0).getId());
            assertEquals(result.getClosestAreas().getClosestAreas().get(0).getAreaType(), response.getClosestAreas().getClosestAreas().get(0).getAreaType());
            assertEquals(result.getClosestAreas().getClosestAreas().get(0).getDistance(), response.getClosestAreas().getClosestAreas().get(0).getDistance(), 0.01);
            assertEquals(result.getClosestAreas().getClosestAreas().get(0).getUnit(), response.getClosestAreas().getClosestAreas().get(0).getUnit());
            assertEquals(result.getClosestLocations().getClosestLocations().get(0).getId(), response.getClosestLocations().getClosestLocations().get(0).getId());
            assertEquals(result.getClosestLocations().getClosestLocations().get(0).getDistance(), response.getClosestLocations().getClosestLocations().get(0).getDistance(), 0.01);
            assertEquals(result.getClosestLocations().getClosestLocations().get(0).getUnit(), response.getClosestLocations().getClosestLocations().get(0).getUnit());
            assertEquals(result.getClosestLocations().getClosestLocations().get(0).getLocationType(), response.getClosestLocations().getClosestLocations().get(0).getLocationType());
        } catch (SpatialModelMarshallException e) {
            fail("should not throw error");
        }
    }

}