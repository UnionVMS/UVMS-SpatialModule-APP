package eu.europa.ec.fisheries.uvms.spatial.model.mapper;

import eu.europa.ec.fisheries.uvms.spatial.model.FaultCode;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMapperException;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMarshallException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.xml.bind.*;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

public class SpatialModuleResponseMapperTest {

    private SpatialModuleResponseMapper mapper;

    @Mock
    private TextMessage textMessage;

    @Before
    public void init(){
        mapper = new SpatialModuleResponseMapper();
    }

    @Test
    public void testMapToAreasByLocationTypeFromResponseWithResponseNull(){
        try {
            mapper.mapToAreasByLocationTypeFromResponse(null, "123245");
            fail("test should fail");
        } catch (SpatialModelMapperException e) {
            assertEquals("Error when validating response in ResponseMapper: Response is Null", e.getMessage());
        }
    }

    @Test
    public void testMapToAreasByLocationTypeFromResponseWithCorrelationIdNull(){
        try {
            TextMessage mock = Mockito.mock(TextMessage.class);
            mapper.mapToAreasByLocationTypeFromResponse(mock, null);
            fail("test should fail");
        } catch (SpatialModelMapperException e) {
            assertEquals("No correlationId in response (Null) . Expected was: null", e.getMessage());
        }
    }

    @Test
    public void testMapToAreasByLocationTypeFromResponseWithSpatialMessageFault(){
        try {

            SpatialFault error = mapper.createFaultMessage(FaultCode.SPATIAL_MESSAGE, FaultCode.SPATIAL_MESSAGE.toString());

            JAXBContext jaxbContext = JAXBContext.newInstance(error.getClass());
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter sw = new StringWriter();
            marshaller.marshal(error, sw);

            TextMessage mock = Mockito.mock(TextMessage.class);
            when(mock.getJMSCorrelationID()).thenReturn("666");
            when(mock.getText()).thenReturn(sw.toString());

            mapper.mapToAreasByLocationTypeFromResponse(mock, "666");
            fail("test should fail");
        } catch (SpatialModelMapperException e) {
            assertEquals("1700 : SPATIAL_MESSAGE", e.getMessage());
        } catch (JMSException | JAXBException e) {
            fail("test should not throw the exceptions");
        }
    }

    @Test
    public void testMapToAreasByLocationTypeFromResponse(){
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

            AreasByLocationType areasByLocationType = mapper.mapToAreasByLocationTypeFromResponse(mock, "666");
            assertEquals("2", areasByLocationType.getArea().get(0).getId());
            assertEquals("EEZ", areasByLocationType.getArea().get(0).getAreaType());
        } catch (SpatialModelMapperException | JMSException | JAXBException e) {
            fail("test should not throw these exceptions");
        }
    }

    @Test
    public void testMapToAreasByLocationTypeFromResponseWithWrongCorrelationId(){
        try {
            TextMessage mock = Mockito.mock(TextMessage.class);
            when(mock.getJMSCorrelationID()).thenReturn("555");
            mapper.mapToAreasByLocationTypeFromResponse(mock, "666");
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

        List<AreaTypeEntry> entryList = new ArrayList<>();
        AreaTypeEntry entry = new AreaTypeEntry();
        entry.setAreaType("EEZ");
        entry.setId("2");
        entryList.add(entry);
        try {
            String responseString = mapper.mapAreaByLocationResponse(entryList);
            StringReader reader = new StringReader(responseString);
            AreaByLocationSpatialRS result = (AreaByLocationSpatialRS) jaxbUnmarshaller.unmarshal(reader);
            assertEquals(result.getAreasByLocation().getArea().get(0).getAreaType(), response.getAreasByLocation().getArea().get(0).getAreaType());
            assertEquals(result.getAreasByLocation().getArea().get(0).getId(), response.getAreasByLocation().getArea().get(0).getId());
        } catch (SpatialModelMarshallException e) {
            fail("should not throw error");
        }
    }

    @Test
    public void testMapAreaByLocationResponseException() throws JAXBException {
        try {
            List<AreaTypeEntry> entryList = new ArrayList<>();
            AreaTypeEntry entry = new AreaTypeEntry();
            entry.setAreaType("EEZ");
            entry.setId("2");
            entryList.add(entry);

            mapper = new SpatialModuleResponseMapper(){
                protected <T> String marshallJaxBObjectToString(final T data) throws JAXBException {
                    throw new JAXBException("error");
                }
            };
            mapper.mapAreaByLocationResponse(entryList);
            fail("Should throw exception");
        } catch (SpatialModelMarshallException e) {
            assertEquals("Error when marshalling java.util.ArrayList to String", e.getMessage());
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
            String responseString = mapper.mapAreaTypeNamesResponse(names);
            StringReader reader = new StringReader(responseString);
            AreaTypeNamesSpatialRS result = (AreaTypeNamesSpatialRS) jaxbUnmarshaller.unmarshal(reader);
            assertEquals(result.getAreaTypes().getAreaType().get(0), response.getAreaTypes().getAreaType().get(0));
        } catch (SpatialModelMarshallException e) {
            fail("should not throw error");
        }
    }

    @Test
    public void testMapAreaTypeNamesResponseException() throws JAXBException {
        try {
            List<String> entryList = new ArrayList<>();
            entryList.add("EEZ");

            mapper = new SpatialModuleResponseMapper(){
                protected <T> String marshallJaxBObjectToString(final T data) throws JAXBException {
                    throw new JAXBException("error");
                }
            };
            mapper.mapAreaTypeNamesResponse(entryList);
            fail("Should throw exception");
        } catch (SpatialModelMarshallException e) {
            assertEquals("Error when marshalling java.util.ArrayList to String", e.getMessage());
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
            String responseString = mapper.mapClosestLocationResponse(locations);
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
    public void testMapClosestLocationResponseException() throws JAXBException {
        try {

            List<Location> locations = new ArrayList<>();
            Location location = new Location();
            location.setDistance(12.0);
            location.setId("2");
            location.setUnit(UnitType.METERS);
            location.setLocationType(LocationType.PORT);
            locations.add(location);

            mapper = new SpatialModuleResponseMapper(){
                protected <T> String marshallJaxBObjectToString(final T data) throws JAXBException {
                    throw new JAXBException("error");
                }
            };
            mapper.mapClosestLocationResponse(locations);
            fail("Should throw exception");
        } catch (SpatialModelMarshallException e) {
            assertEquals("Error when marshalling java.util.ArrayList to String", e.getMessage());
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
            String responseString = mapper.mapClosestAreaResponse(areas);
            StringReader reader = new StringReader(responseString);
            ClosestAreaSpatialRS result = (ClosestAreaSpatialRS) jaxbUnmarshaller.unmarshal(reader);
            assertEquals(result.getClosestArea().getClosestArea().get(0).getId(), response.getClosestArea().getClosestArea().get(0).getId());
            assertEquals(result.getClosestArea().getClosestArea().get(0).getDistance(), response.getClosestArea().getClosestArea().get(0).getDistance(), 0.01);
            assertEquals(result.getClosestArea().getClosestArea().get(0).getUnit(), response.getClosestArea().getClosestArea().get(0).getUnit());
            assertEquals(result.getClosestArea().getClosestArea().get(0).getAreaType(), response.getClosestArea().getClosestArea().get(0).getAreaType());
        } catch (SpatialModelMarshallException e) {
            fail("should not throw error");
        }
    }

    @Test
    public void testMapClosestAreaResponseException() throws JAXBException {
        try {

            List<Area> areas = new ArrayList<>();
            Area area = new Area();
            area.setDistance(12.0);
            area.setId("2");
            area.setUnit(UnitType.METERS);
            area.setAreaType(AreaType.EEZ);
            areas.add(area);

            mapper = new SpatialModuleResponseMapper(){
                protected <T> String marshallJaxBObjectToString(final T data) throws JAXBException {
                    throw new JAXBException("error");
                }
            };
            mapper.mapClosestAreaResponse(areas);
            fail("Should throw exception");
        } catch (SpatialModelMarshallException e) {
            assertEquals("Error when marshalling java.util.ArrayList to String", e.getMessage());
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
        closestAreasType.getClosestArea().addAll(areas);
        enrichment.setClosestAreas(closestAreasType);
        ClosestLocationsType closestLocationsType = new ClosestLocationsType();
        closestLocationsType.getClosestLocations().addAll(locations);
        enrichment.setClosestLocations(closestLocationsType);


        try {
            String responseString = mapper.mapEnrichmentResponse(enrichment);
            StringReader reader = new StringReader(responseString);
            SpatialEnrichmentRS result = (SpatialEnrichmentRS) jaxbUnmarshaller.unmarshal(reader);
            assertEquals(result.getClosestAreas().getClosestArea().get(0).getId(), response.getClosestAreas().getClosestArea().get(0).getId());
            assertEquals(result.getClosestAreas().getClosestArea().get(0).getAreaType(), response.getClosestAreas().getClosestArea().get(0).getAreaType());
            assertEquals(result.getClosestAreas().getClosestArea().get(0).getDistance(), response.getClosestAreas().getClosestArea().get(0).getDistance(), 0.01);
            assertEquals(result.getClosestAreas().getClosestArea().get(0).getUnit(), response.getClosestAreas().getClosestArea().get(0).getUnit());
            assertEquals(result.getClosestLocations().getClosestLocations().get(0).getId(), response.getClosestLocations().getClosestLocations().get(0).getId());
            assertEquals(result.getClosestLocations().getClosestLocations().get(0).getDistance(), response.getClosestLocations().getClosestLocations().get(0).getDistance(), 0.01);
            assertEquals(result.getClosestLocations().getClosestLocations().get(0).getUnit(), response.getClosestLocations().getClosestLocations().get(0).getUnit());
            assertEquals(result.getClosestLocations().getClosestLocations().get(0).getLocationType(), response.getClosestLocations().getClosestLocations().get(0).getLocationType());
        } catch (SpatialModelMarshallException e) {
            fail("should not throw error");
        }
    }

    @Test
    public void testMapEnrichmentResponseException() throws JAXBException {
        try {

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
            closestAreasType.getClosestArea().addAll(areas);
            enrichment.setClosestAreas(closestAreasType);
            ClosestLocationsType closestLocationsType = new ClosestLocationsType();
            closestLocationsType.getClosestLocations().addAll(locations);
            enrichment.setClosestLocations(closestLocationsType);

            mapper = new SpatialModuleResponseMapper(){
                protected <T> String marshallJaxBObjectToString(final T data) throws JAXBException {
                    throw new JAXBException("error");
                }
            };
            mapper.mapEnrichmentResponse(enrichment);
            fail("Should throw exception");
        } catch (SpatialModelMarshallException e) {
            assertEquals("Error when marshalling eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialEnrichmentRS to String", e.getMessage());
        }
    }

}
