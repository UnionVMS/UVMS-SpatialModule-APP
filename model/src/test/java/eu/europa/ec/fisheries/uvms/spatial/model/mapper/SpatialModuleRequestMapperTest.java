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

    private SpatialModuleRequestMapper mapper;

    @Before
    public void init(){
        mapper = new SpatialModuleRequestMapper();
    }

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
            String requestString = mapper.mapToCreateAreaByLocationRequest(point);
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
    public void testMapToCreateAreaByLocationRequestException() throws JAXBException {

        PointType point = new PointType();
        point.setLongitude(LONGITUDE);
        point.setLatitude(LATITUDE);
        point.setCrs(CRS);
        try {
            mapper = new SpatialModuleRequestMapper(){
                protected <T> String marshallJaxBObjectToString(final T data) throws JAXBException {
                    throw new JAXBException("succes");
                }
            };
            mapper.mapToCreateAreaByLocationRequest(point);
            fail("Should throw exception");
        } catch (SpatialModelMarshallException e) {
           assertEquals("succes" , e.getCause().getMessage());
        }
    }

    @Test
    public void testMapToCreateAllAreaTypesRequest() throws JAXBException {

        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("allAreaTypesRequest.xml");
        JAXBContext jaxbContext = JAXBContext.newInstance(AllAreaTypesRequest.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        AllAreaTypesRequest request = (AllAreaTypesRequest) jaxbUnmarshaller.unmarshal(resourceAsStream);

        try {
            String requestString = mapper.mapToCreateAllAreaTypesRequest();
            StringReader reader = new StringReader(requestString);
            AllAreaTypesRequest result = (AllAreaTypesRequest) jaxbUnmarshaller.unmarshal(reader);
            assertEquals(result.getMethod(), request.getMethod());
        } catch (SpatialModelMarshallException e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testMapToCreateAllAreaTypesRequestException() throws JAXBException {

        try {
            mapper = new SpatialModuleRequestMapper(){
                protected <T> String marshallJaxBObjectToString(final T data) throws JAXBException {
                    throw new JAXBException("succes");
                }
            };
            mapper.mapToCreateAllAreaTypesRequest();
            fail("Should throw exception");
        } catch (SpatialModelMarshallException e) {
            assertEquals("succes" , e.getCause().getMessage());
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
            String requestString = mapper.mapToCreateClosestAreaRequest(point, UnitType.METERS, Arrays.asList(AreaType.EEZ));
            StringReader reader = new StringReader(requestString);
            ClosestAreaSpatialRQ result = (ClosestAreaSpatialRQ) jaxbUnmarshaller.unmarshal(reader);
            assertEquals(result.getPoint().getCrs(), request.getPoint().getCrs());
            assertEquals(result.getPoint().getLatitude(), request.getPoint().getLatitude(), 0.01);
            assertEquals(result.getPoint().getLongitude(), request.getPoint().getLongitude(), 0.01);
            assertEquals(result.getMethod(), request.getMethod());
            assertEquals(result.getUnit(), request.getUnit());
            assertEquals(result.getAreaTypes().getAreaType().get(0), request.getAreaTypes().getAreaType().get(0));
        } catch (SpatialModelMarshallException e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testMapToCreateClosestAreaRequestException() throws JAXBException {

        try {
            mapper = new SpatialModuleRequestMapper(){
                protected <T> String marshallJaxBObjectToString(final T data) throws JAXBException {
                    throw new JAXBException("succes");
                }
            };
            mapper.mapToCreateClosestAreaRequest(null, null, null);
            fail("Should throw exception");
        } catch (SpatialModelMarshallException e) {
            assertEquals("succes" , e.getCause().getMessage());
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
            String requestString = mapper.mapToCreateClosestLocationRequest(point, UnitType.METERS, Arrays.asList(LocationType.PORT));
            StringReader reader = new StringReader(requestString);
            ClosestLocationSpatialRQ result = (ClosestLocationSpatialRQ) jaxbUnmarshaller.unmarshal(reader);
            assertEquals(result.getPoint().getCrs(), request.getPoint().getCrs());
            assertEquals(result.getPoint().getLatitude(), request.getPoint().getLatitude(), 0.01);
            assertEquals(result.getPoint().getLongitude(), request.getPoint().getLongitude(), 0.01);
            assertEquals(result.getMethod(), request.getMethod());
            assertEquals(result.getUnit(), request.getUnit());
            assertEquals(result.getLocationTypes().getLocationType().get(0), request.getLocationTypes().getLocationType().get(0));
        } catch (SpatialModelMarshallException e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testMapToCreateClosestLocationRequestException() throws JAXBException {

        try {
            mapper = new SpatialModuleRequestMapper(){
                protected <T> String marshallJaxBObjectToString(final T data) throws JAXBException {
                    throw new JAXBException("succes");
                }
            };
            mapper.mapToCreateClosestLocationRequest(null, null, null);
            fail("Should throw exception");
        } catch (SpatialModelMarshallException e) {
            assertEquals("succes" , e.getCause().getMessage());
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
            String requestString = mapper.mapToCreateSpatialEnrichmentRequest(point, UnitType.METERS, Arrays.asList(LocationType.PORT), Arrays.asList(AreaType.EEZ));
            StringReader reader = new StringReader(requestString);
            SpatialEnrichmentRQ result = (SpatialEnrichmentRQ) jaxbUnmarshaller.unmarshal(reader);
            assertEquals(result.getPoint().getCrs(), request.getPoint().getCrs());
            assertEquals(result.getPoint().getLatitude(), request.getPoint().getLatitude(), 0.01);
            assertEquals(result.getPoint().getLongitude(), request.getPoint().getLongitude(), 0.01);
            assertEquals(result.getMethod(), request.getMethod());
            assertEquals(result.getUnit(), request.getUnit());
            assertEquals(result.getLocationTypes().getLocationType().get(0), request.getLocationTypes().getLocationType().get(0));
            assertEquals(result.getAreaTypes().getAreaType().get(0), request.getAreaTypes().getAreaType().get(0));
        } catch (SpatialModelMarshallException e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testMapToCreateSpatialEnrichmentRequestException() throws JAXBException {

        try {
            mapper = new SpatialModuleRequestMapper(){
                protected <T> String marshallJaxBObjectToString(final T data) throws JAXBException {
                    throw new JAXBException("succes");
                }
            };
            mapper.mapToCreateSpatialEnrichmentRequest(null, null, null, null);
            fail("Should throw exception");
        } catch (SpatialModelMarshallException e) {
            assertEquals("succes" , e.getCause().getMessage());
        }
    }

}
