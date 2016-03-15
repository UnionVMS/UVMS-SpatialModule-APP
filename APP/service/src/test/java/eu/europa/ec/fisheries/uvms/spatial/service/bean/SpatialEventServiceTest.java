package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.message.bean.SpatialMessageServiceBean;
import eu.europa.ec.fisheries.uvms.spatial.message.event.SpatialMessageEvent;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMarshallException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AllAreaTypesRequest;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.FilterAreasSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.FilterAreasSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.PingRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialEnrichmentRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialEnrichmentRS;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.enterprise.event.Event;
import javax.jms.TextMessage;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SpatialEventServiceTest {

    @Mock
    Event<SpatialMessageEvent> spatialErrorEvent;
    @InjectMocks
    private SpatialEventService service = new SpatialEventServiceBean();
    @Mock
    private SpatialService areaByLocationService;
    @Mock
    private SpatialService closestAreaService;
    @Mock
    private SpatialService closestLocationService;
    @Mock
    private SpatialEnrichmentService enrichmentService;
    @Mock
    private AreaTypeNamesService areaTypeNamesService;
    @Mock
    private SpatialMessageServiceBean messageProducer;
    @Mock
    private SpatialService filterAreasService;
    @Mock
    private TextMessage textMessage;

    @Test
    public void testGetAreaTypeNames() {
        SpatialMessageEvent message = new SpatialMessageEvent(textMessage, new AllAreaTypesRequest());

        service.getAreaTypeNames(message);

        verify(areaTypeNamesService, times(1)).listAllAreaTypeNames();
        verify(messageProducer, times(1)).sendModuleResponseMessage(eq(textMessage), anyString());
        verify(spatialErrorEvent, times(0)).fire(message);
    }

    @Test
    @SneakyThrows
    public void testGetSpatialEnrichment() {
        SpatialMessageEvent message = new SpatialMessageEvent(textMessage, new SpatialEnrichmentRQ());
        when(enrichmentService.getSpatialEnrichment(any(SpatialEnrichmentRQ.class))).thenReturn(new SpatialEnrichmentRS());

        service.getSpatialEnrichment(message);

        verify(enrichmentService, times(1)).getSpatialEnrichment(any(SpatialEnrichmentRQ.class));
        verify(messageProducer, times(1)).sendModuleResponseMessage(eq(textMessage), anyString());
        verify(spatialErrorEvent, times(0)).fire(message);
    }

    @Test
    @SneakyThrows
    public void testGetSpatialEnrichmentError() throws SpatialModelMarshallException {
        SpatialMessageEvent message = new SpatialMessageEvent(textMessage, new SpatialEnrichmentRQ());
        when(enrichmentService.getSpatialEnrichment(any(SpatialEnrichmentRQ.class))).thenThrow(SpatialServiceException.class);

        service.getSpatialEnrichment(message);

        verify(enrichmentService, times(1)).getSpatialEnrichment(any(SpatialEnrichmentRQ.class));
        verify(messageProducer, times(0)).sendModuleResponseMessage(eq(textMessage), anyString());
        verify(spatialErrorEvent, times(1)).fire(any(SpatialMessageEvent.class));
    }

    @Test
    public void testPing() {
        SpatialMessageEvent message = new SpatialMessageEvent(textMessage, new PingRQ());

        service.ping(message);

        verify(messageProducer, times(1)).sendModuleResponseMessage(eq(textMessage), anyString());
        verify(spatialErrorEvent, times(0)).fire(message);
    }
}
