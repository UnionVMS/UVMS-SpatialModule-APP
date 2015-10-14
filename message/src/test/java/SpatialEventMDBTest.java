import eu.europa.ec.fisheries.uvms.spatial.message.bean.SpatialEventMDB;
import eu.europa.ec.fisheries.uvms.spatial.message.event.*;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMarshallException;
import eu.europa.ec.fisheries.uvms.spatial.model.mapper.SpatialJAXBMarshaller;
import eu.europa.ec.fisheries.uvms.spatial.model.mapper.SpatialModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.spatial.model.mapper.SpatialModuleResponseMapper;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.TextMessage;

import java.util.Arrays;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SpatialEventMDBTest {

    private static final double LATITUDE = 45.11557, LONGITUDE = -7.14925;
    private static final int CRS = 3857;

    @InjectMocks
    private SpatialEventMDB mdb = new SpatialEventMDB();

    @Mock
    private SpatialJAXBMarshaller marshaller;

    @Mock
    private Event<SpatialMessageEvent> areaByLocationSpatialEvent;

    @Mock
    private Event<SpatialMessageEvent> typeNamesEvent;

    @Mock
    private Event<SpatialMessageEvent> closestAreaSpatialEvent;

    @Mock
    private Event<SpatialMessageEvent> enrichmentSpatialEvent;

    @Mock
    private Event<SpatialMessageEvent> closestLocationSpatialEvent;
    
    @Mock
    private Event<SpatialMessageEvent> filterAreaSpatialEvent;

    @Mock
    private Event<SpatialMessageEvent> spatialErrorEvent;

    @Mock
    private SpatialModuleResponseMapper responseMapper;

    private SpatialModuleRequestMapper requestMapper = new SpatialModuleRequestMapper();

    @Test
    public void testOnMessageWithGetAreaByLocation() throws SpatialModelMarshallException, JMSException {

        PointType point = new PointType();
        point.setLongitude(LONGITUDE);
        point.setLatitude(LATITUDE);
        point.setCrs(CRS);
        String requestString = requestMapper.mapToCreateAreaByLocationRequest(point);

        TextMessage mock = Mockito.mock(TextMessage.class);
        when(mock.getText()).thenReturn(requestString);

        SpatialModuleRequest request = new AreaByLocationSpatialRQ();
        request.setMethod(SpatialModuleMethod.GET_AREA_BY_LOCATION);
        when(marshaller.unmarshall(mock, SpatialModuleRequest.class)).thenReturn(request);

        mdb.onMessage(mock);

        verify(areaByLocationSpatialEvent, times(1)).fire(any(SpatialMessageEvent.class));
        verify(spatialErrorEvent, times(0)).fire(any(SpatialMessageEvent.class));

    }

    @Test
    public void testOnMessageWithAllAreaTypesRequest() throws SpatialModelMarshallException, JMSException {

        String requestString = requestMapper.mapToCreateAllAreaTypesRequest();
        TextMessage mock = Mockito.mock(TextMessage.class);
        when(mock.getText()).thenReturn(requestString);

        SpatialModuleRequest request = new AreaByLocationSpatialRQ();
        request.setMethod(SpatialModuleMethod.GET_AREA_TYPES);
        when(marshaller.unmarshall(mock, SpatialModuleRequest.class)).thenReturn(request);

        mdb.onMessage(mock);

        verify(typeNamesEvent, times(1)).fire(any(SpatialMessageEvent.class));
        verify(spatialErrorEvent, times(0)).fire(any(SpatialMessageEvent.class));

    }

    @Test
    public void testOnMessageWithClosestAreaRequest() throws SpatialModelMarshallException, JMSException {

        PointType point = new PointType();
        point.setLongitude(LONGITUDE);
        point.setLatitude(LATITUDE);
        point.setCrs(CRS);

        String requestString = requestMapper.mapToCreateClosestAreaRequest(point, UnitType.METERS, Arrays.asList(AreaType.EEZ));
        TextMessage mock = Mockito.mock(TextMessage.class);
        when(mock.getText()).thenReturn(requestString);

        SpatialModuleRequest request = new ClosestAreaSpatialRQ();
        request.setMethod(SpatialModuleMethod.GET_CLOSEST_AREA);
        when(marshaller.unmarshall(mock, SpatialModuleRequest.class)).thenReturn(request);

        mdb.onMessage(mock);

        verify(closestAreaSpatialEvent, times(1)).fire(any(SpatialMessageEvent.class));
        verify(spatialErrorEvent, times(0)).fire(any(SpatialMessageEvent.class));

    }

    @Test
    public void testOnMessageWithClosestLocationRequest() throws SpatialModelMarshallException, JMSException {

        PointType point = new PointType();
        point.setLongitude(LONGITUDE);
        point.setLatitude(LATITUDE);
        point.setCrs(CRS);

        String requestString = requestMapper.mapToCreateClosestLocationRequest(point, UnitType.METERS, Arrays.asList(LocationType.PORT));
        TextMessage mock = Mockito.mock(TextMessage.class);
        when(mock.getText()).thenReturn(requestString);

        SpatialModuleRequest request = new ClosestLocationSpatialRQ();
        request.setMethod(SpatialModuleMethod.GET_CLOSEST_LOCATION);
        when(marshaller.unmarshall(mock, SpatialModuleRequest.class)).thenReturn(request);

        mdb.onMessage(mock);

        verify(closestLocationSpatialEvent, times(1)).fire(any(SpatialMessageEvent.class));
        verify(spatialErrorEvent, times(0)).fire(any(SpatialMessageEvent.class));

    }

    @Test
    public void testOnMessageWithEnrichmentRequest() throws SpatialModelMarshallException, JMSException {

        PointType point = new PointType();
        point.setLongitude(LONGITUDE);
        point.setLatitude(LATITUDE);
        point.setCrs(CRS);

        String requestString = requestMapper.mapToCreateSpatialEnrichmentRequest(point, UnitType.METERS, Arrays.asList(LocationType.PORT), Arrays.asList(AreaType.EEZ));
        TextMessage mock = Mockito.mock(TextMessage.class);
        when(mock.getText()).thenReturn(requestString);

        SpatialModuleRequest request = new SpatialEnrichmentRQ();
        request.setMethod(SpatialModuleMethod.GET_ENRICHMENT);
        when(marshaller.unmarshall(mock, SpatialModuleRequest.class)).thenReturn(request);

        mdb.onMessage(mock);

        verify(enrichmentSpatialEvent, times(1)).fire(any(SpatialMessageEvent.class));
        verify(spatialErrorEvent, times(0)).fire(any(SpatialMessageEvent.class));

    }

    @Test
    public void testOnMessageWithUnimplementedMethod() throws SpatialModelMarshallException, JMSException {

        PointType point = new PointType();
        point.setLongitude(LONGITUDE);
        point.setLatitude(LATITUDE);
        point.setCrs(CRS);

        String requestString = requestMapper.mapToCreateSpatialEnrichmentRequest(point, UnitType.METERS, Arrays.asList(LocationType.PORT), Arrays.asList(AreaType.EEZ));
        TextMessage mock = Mockito.mock(TextMessage.class);
        when(mock.getText()).thenReturn(requestString);

        SpatialModuleRequest request = new SpatialEnrichmentRQ();
        request.setMethod(SpatialModuleMethod.GET_BUFFER_GEOM);
        when(marshaller.unmarshall(mock, SpatialModuleRequest.class)).thenReturn(request);

        mdb.onMessage(mock);

        verify(spatialErrorEvent, times(1)).fire(any(SpatialMessageEvent.class));

    }
    
    @Test
    public void testOnMessageWithFilterAreasMethod() throws SpatialModelMarshallException, JMSException {
    	AreaIdentifierType areaType = new AreaIdentifierType();
    	areaType.setAreaType("EEZ");
    	areaType.setId("1");
    	
    	String requestString = requestMapper.mapToFilterAreaSpatialRequest(Arrays.asList(areaType), Arrays.asList(areaType));
    	TextMessage mock = Mockito.mock(TextMessage.class);
    	when(mock.getText()).thenReturn(requestString);
    	
    	FilterAreasSpatialRQ request = new FilterAreasSpatialRQ();
    	request.setMethod(SpatialModuleMethod.GET_FILTER_AREA);
    	when(marshaller.unmarshall(mock, SpatialModuleRequest.class)).thenReturn(request);
    	mdb.onMessage(mock);
    	
    	verify(filterAreaSpatialEvent, times(1)).fire(any(SpatialMessageEvent.class));
        verify(spatialErrorEvent, times(0)).fire(any(SpatialMessageEvent.class));
    }

}
