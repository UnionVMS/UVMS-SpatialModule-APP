package eu.europa.ec.fisheries.uvms.spatial.service.bean;


import eu.europa.ec.fisheries.uvms.spatial.message.bean.SpatialEventQ;
import eu.europa.ec.fisheries.uvms.spatial.message.event.SpatialMessageEvent;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMarshallException;
import eu.europa.ec.fisheries.uvms.spatial.model.mapper.SpatialModuleResponseMapper;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.service.SpatialEventService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.enterprise.event.Event;
import javax.jms.TextMessage;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SpatialEventServiceTest {

    @InjectMocks
    private SpatialEventService service = new SpatialEventServiceBean();

    @Mock
    private AreaByLocationService areaByLocationService;

    @Mock
    private ClosestAreaService closestAreaService;

    @Mock
    private ClosestLocationService closestLocationService;

    @Mock
    private SpatialEnrichmentService enrichmentService;

    @Mock
    private AreaTypeNamesService areaTypeNamesService;

    @Mock
    private SpatialEventQ messageProducer;

    @Mock
    Event<SpatialMessageEvent> spatialErrorEvent;

    @Mock
    private SpatialModuleResponseMapper mapper;

    @Test
    public void testGetAreaByLocation(){

        AreaByLocationSpatialRQ request = new AreaByLocationSpatialRQ();
        TextMessage mock = Mockito.mock(TextMessage.class);
        SpatialMessageEvent message = new SpatialMessageEvent(mock, request);

        service.getAreaByLocation(message);

        verify(areaByLocationService, times(1)).getAreaTypesByLocation(request);
        verify(messageProducer, times(1)).sendModuleResponseMessage(mock, null);
        verify(spatialErrorEvent, times(0)).fire(message);

    }

    @Test
    public void testGetClosestArea(){

        ClosestAreaSpatialRQ request = new ClosestAreaSpatialRQ();
        TextMessage mock = Mockito.mock(TextMessage.class);
        SpatialMessageEvent message = new SpatialMessageEvent(mock, request);

        service.getClosestArea(message);

        verify(closestAreaService, times(1)).getClosestAreas(request);
        verify(messageProducer, times(1)).sendModuleResponseMessage(mock, null);
        verify(spatialErrorEvent, times(0)).fire(message);

    }

    @Test
    public void testGetClosestLocation(){

        ClosestLocationSpatialRQ request = new ClosestLocationSpatialRQ();
        TextMessage mock = Mockito.mock(TextMessage.class);
        SpatialMessageEvent message = new SpatialMessageEvent(mock, request);

        service.getClosestLocation(message);

        verify(closestLocationService, times(1)).getClosestLocations(request);
        verify(messageProducer, times(1)).sendModuleResponseMessage(mock, null);
        verify(spatialErrorEvent, times(0)).fire(message);

    }

    @Test
    public void testGetAreaTypeNames(){

        AllAreaTypesRequest request = new AllAreaTypesRequest();
        TextMessage mock = Mockito.mock(TextMessage.class);
        SpatialMessageEvent message = new SpatialMessageEvent(mock, request);

        service.getAreaTypeNames(message);

        verify(areaTypeNamesService, times(1)).listAllAreaTypeNames();
        verify(messageProducer, times(1)).sendModuleResponseMessage(mock, null);
        verify(spatialErrorEvent, times(0)).fire(message);

    }

    @Test
    public void testGetSpatialEnrichment(){

        SpatialEnrichmentRQ request = new SpatialEnrichmentRQ();
        TextMessage mock = Mockito.mock(TextMessage.class);
        SpatialMessageEvent message = new SpatialMessageEvent(mock, request);

        service.getSpatialEnrichment(message);

        verify(enrichmentService, times(1)).getSpatialEnrichment(request);
        verify(messageProducer, times(1)).sendModuleResponseMessage(mock, null);
        verify(spatialErrorEvent, times(0)).fire(message);

    }

    @Test
    public void testGetSpatialEnrichmentError() throws SpatialModelMarshallException {

        SpatialEnrichmentRQ request = new SpatialEnrichmentRQ();
        TextMessage mock = Mockito.mock(TextMessage.class);
        SpatialMessageEvent message = new SpatialMessageEvent(mock, request);

        when(mapper.mapEnrichmentResponse(any(SpatialEnrichmentRS.class))).thenThrow(SpatialModelMarshallException.class);

        service.getSpatialEnrichment(message);
        verify(enrichmentService, times(1)).getSpatialEnrichment(request);
        verify(messageProducer, times(0)).sendModuleResponseMessage(mock, null);
        verify(spatialErrorEvent, times(1)).fire(any(SpatialMessageEvent.class));
    }
}
