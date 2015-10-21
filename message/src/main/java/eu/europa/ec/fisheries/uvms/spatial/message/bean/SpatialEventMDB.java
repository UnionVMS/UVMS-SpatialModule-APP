package eu.europa.ec.fisheries.uvms.spatial.message.bean;

import static eu.europa.ec.fisheries.uvms.message.MessageConstants.*;
import eu.europa.ec.fisheries.uvms.spatial.message.event.*;
import eu.europa.ec.fisheries.uvms.spatial.model.FaultCode;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMapperException;
import eu.europa.ec.fisheries.uvms.spatial.model.mapper.SpatialJAXBMarshaller;
import eu.europa.ec.fisheries.uvms.spatial.model.mapper.SpatialModuleResponseMapper;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@MessageDriven(mappedName = QUEUE_MODULE_SPATIAL, activationConfig = {
        @ActivationConfigProperty(propertyName = "messagingType", propertyValue = CONNECTION_TYPE),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = DESTINATION_TYPE_QUEUE),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = QUEUE_MODULE_SPATIAL_NAME)
})
@Slf4j
public class SpatialEventMDB implements MessageListener {

    @Inject
    @GetAreaByLocationEvent
    Event<SpatialMessageEvent> areaByLocationSpatialEvent;

    @Inject
    @GetAreaTypeNamesEvent
    Event<SpatialMessageEvent> typeNamesEvent;

    @Inject
    @GetClosestAreaEvent
    Event<SpatialMessageEvent> closestAreaSpatialEvent;

    @Inject
    @GetSpatialEnrichmentEvent
    Event<SpatialMessageEvent> enrichmentSpatialEvent;

    @Inject
    @GetClosestLocationEvent
    Event<SpatialMessageEvent> closestLocationSpatialEvent;
    
    @Inject
    @GetFilterAreaEvent
    Event<SpatialMessageEvent> filterAreaSpatialEvent;

    @Inject
    @PingEvent
    Event<SpatialMessageEvent> pingSpatialEvent;

    @Inject
    @SpatialMessageErrorEvent
    Event<SpatialMessageEvent> spatialErrorEvent;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void onMessage(Message message) {

        TextMessage textMessage = (TextMessage) message;

        try {

            SpatialModuleRequest request = SpatialJAXBMarshaller.unmarshall(textMessage, SpatialModuleRequest.class);
            SpatialModuleMethod method = request.getMethod();

            switch (method) {
                case GET_AREA_BY_LOCATION:
                    AreaByLocationSpatialRQ byLocationSpatialRQ = SpatialJAXBMarshaller.unmarshall(textMessage, AreaByLocationSpatialRQ.class);
                    SpatialMessageEvent areaByLocationEvent = new SpatialMessageEvent(textMessage, byLocationSpatialRQ);
                    areaByLocationSpatialEvent.fire(areaByLocationEvent);
                    break;
                case GET_AREA_TYPES:
                    AllAreaTypesRequest allAreaTypesRequest = SpatialJAXBMarshaller.unmarshall(textMessage, AllAreaTypesRequest.class);
                    SpatialMessageEvent areaTypeNamesEvent = new SpatialMessageEvent(textMessage, allAreaTypesRequest);
                    typeNamesEvent.fire(areaTypeNamesEvent);
                    break;
                case GET_CLOSEST_AREA:
                    ClosestAreaSpatialRQ closestAreaSpatialRQ = SpatialJAXBMarshaller.unmarshall(textMessage, ClosestAreaSpatialRQ.class);
                    SpatialMessageEvent closestAreaEvent = new SpatialMessageEvent(textMessage, closestAreaSpatialRQ);
                    closestAreaSpatialEvent.fire(closestAreaEvent);
                    break;
                case GET_CLOSEST_LOCATION:
                    ClosestLocationSpatialRQ closestLocationSpatialRQ = SpatialJAXBMarshaller.unmarshall(textMessage, ClosestLocationSpatialRQ.class);
                    SpatialMessageEvent closestLocationEvent = new SpatialMessageEvent(textMessage, closestLocationSpatialRQ);
                    closestLocationSpatialEvent.fire(closestLocationEvent);
                    break;
                case GET_ENRICHMENT:
                    SpatialEnrichmentRQ spatialEnrichmentRQ = SpatialJAXBMarshaller.unmarshall(textMessage, SpatialEnrichmentRQ.class);
                    SpatialMessageEvent spatialEnrichmentEvent = new SpatialMessageEvent(textMessage, spatialEnrichmentRQ);
                    enrichmentSpatialEvent.fire(spatialEnrichmentEvent);
                    break;
                    
                case GET_FILTER_AREA:
                	FilterAreasSpatialRQ filterAreasSpatialRQ = SpatialJAXBMarshaller.unmarshall(textMessage, FilterAreasSpatialRQ.class);
                	SpatialMessageEvent filterAreaEvent = new SpatialMessageEvent(textMessage, filterAreasSpatialRQ);
                	filterAreaSpatialEvent.fire(filterAreaEvent);
                	break;
                case PING:
                    PingRQ pingRQ = SpatialJAXBMarshaller.unmarshall(textMessage, PingRQ.class);
                    SpatialMessageEvent pingEvent = new SpatialMessageEvent(textMessage, pingRQ);
                    pingSpatialEvent.fire(pingEvent);
                    break;
                default:
                    log.error("[ Not implemented method consumed: {} ]", method);
                    spatialErrorEvent.fire(new SpatialMessageEvent(textMessage, SpatialModuleResponseMapper.createFaultMessage(FaultCode.SPATIAL_MESSAGE, "Method not implemented")));
            }

        } catch (SpatialModelMapperException e) {
            log.error("[ Error when receiving message in SpatialModule. ]", e);
            spatialErrorEvent.fire(new SpatialMessageEvent(textMessage, SpatialModuleResponseMapper.createFaultMessage(FaultCode.SPATIAL_MESSAGE, "Method not implemented")));
        }
    }
}
