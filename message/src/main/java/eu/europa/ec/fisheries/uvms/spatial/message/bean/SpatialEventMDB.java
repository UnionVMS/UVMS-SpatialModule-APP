package eu.europa.ec.fisheries.uvms.spatial.message.bean;

import eu.europa.ec.fisheries.uvms.spatial.message.event.*;
import eu.europa.ec.fisheries.uvms.spatial.model.FaultCode;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMapperException;
import eu.europa.ec.fisheries.uvms.spatial.model.mapper.JAXBMarshaller;
import eu.europa.ec.fisheries.uvms.spatial.model.mapper.SpatialModuleResponseMapper;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import static eu.europa.ec.fisheries.uvms.message.MessageConstants.*;

@MessageDriven(mappedName = QUEUE_MODULE_SPATIAL, activationConfig = {
        @ActivationConfigProperty(propertyName = "messagingType", propertyValue = CONNECTION_TYPE),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = DESTINATION_TYPE_QUEUE),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = QUEUE_MODULE_SPATIAL_NAME)
})
@Slf4j
public class SpatialEventMDB implements MessageListener {

    @Inject
    @GetAreaByLocationEvent
    private Event<SpatialMessageEvent> areaByLocationSpatialEvent;

    @Inject
    @GetAreaTypeNamesEvent
    private Event<SpatialMessageEvent> typeNamesEvent;

    @Inject
    @GetClosestAreaEvent
    private Event<SpatialMessageEvent> closestAreaSpatialEvent;

    @Inject
    @GetSpatialEnrichmentEvent
    private Event<SpatialMessageEvent> enrichmentSpatialEvent;

    @Inject
    @GetClosestLocationEvent
    private Event<SpatialMessageEvent> closestLocationSpatialEvent;

    @Inject
    @GetFilterAreaEvent
    private Event<SpatialMessageEvent> filterAreaSpatialEvent;

    @Inject
    @SaveMapConfigurationEvent
    private Event<SpatialMessageEvent> saveMapConfigurationSpatialEvent;

    @Inject
    @GetMapConfigurationEvent
    private Event<SpatialMessageEvent> getMapConfigurationSpatialEvent;

    @Inject
    @PingEvent
    private Event<SpatialMessageEvent> pingSpatialEvent;

    @Inject
    @SpatialMessageErrorEvent
    private Event<SpatialMessageEvent> spatialErrorEvent;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void onMessage(Message message) {

        TextMessage textMessage = (TextMessage) message;

        try {

            SpatialModuleRequest request = JAXBMarshaller.unmarshall(textMessage, SpatialModuleRequest.class);
            SpatialModuleMethod method = request.getMethod();

            switch (method) {
                case GET_AREA_BY_LOCATION:
                    AreaByLocationSpatialRQ byLocationSpatialRQ = JAXBMarshaller.unmarshall(textMessage, AreaByLocationSpatialRQ.class);
                    SpatialMessageEvent areaByLocationEvent = new SpatialMessageEvent(textMessage, byLocationSpatialRQ);
                    areaByLocationSpatialEvent.fire(areaByLocationEvent);
                    break;
                case GET_AREA_TYPES:
                    AllAreaTypesRequest allAreaTypesRequest = JAXBMarshaller.unmarshall(textMessage, AllAreaTypesRequest.class);
                    SpatialMessageEvent areaTypeNamesEvent = new SpatialMessageEvent(textMessage, allAreaTypesRequest);
                    typeNamesEvent.fire(areaTypeNamesEvent);
                    break;
                case GET_CLOSEST_AREA:
                    ClosestAreaSpatialRQ closestAreaSpatialRQ = JAXBMarshaller.unmarshall(textMessage, ClosestAreaSpatialRQ.class);
                    SpatialMessageEvent closestAreaEvent = new SpatialMessageEvent(textMessage, closestAreaSpatialRQ);
                    closestAreaSpatialEvent.fire(closestAreaEvent);
                    break;
                case GET_CLOSEST_LOCATION:
                    ClosestLocationSpatialRQ closestLocationSpatialRQ = JAXBMarshaller.unmarshall(textMessage, ClosestLocationSpatialRQ.class);
                    SpatialMessageEvent closestLocationEvent = new SpatialMessageEvent(textMessage, closestLocationSpatialRQ);
                    closestLocationSpatialEvent.fire(closestLocationEvent);
                    break;
                case GET_ENRICHMENT:
                    SpatialEnrichmentRQ spatialEnrichmentRQ = JAXBMarshaller.unmarshall(textMessage, SpatialEnrichmentRQ.class);
                    SpatialMessageEvent spatialEnrichmentEvent = new SpatialMessageEvent(textMessage, spatialEnrichmentRQ);
                    enrichmentSpatialEvent.fire(spatialEnrichmentEvent);
                    break;
                case GET_FILTER_AREA:
                    FilterAreasSpatialRQ filterAreasSpatialRQ = JAXBMarshaller.unmarshall(textMessage, FilterAreasSpatialRQ.class);
                    SpatialMessageEvent filterAreaEvent = new SpatialMessageEvent(textMessage, filterAreasSpatialRQ);
                    filterAreaSpatialEvent.fire(filterAreaEvent);
                    break;
                case GET_MAP_CONFIGURATION:
                    SpatialGetMapConfigurationRQ spatialGetMapConfigurationRQ = JAXBMarshaller.unmarshall(textMessage, SpatialGetMapConfigurationRQ.class);
                    SpatialMessageEvent getMapConfigurationEvent = new SpatialMessageEvent(textMessage, spatialGetMapConfigurationRQ);
                    saveMapConfigurationSpatialEvent.fire(getMapConfigurationEvent);
                    break;
                case SAVE_OR_UPDATE_MAP_CONFIGURATION:
                    SpatialSaveOrUpdateMapConfigurationRQ spatialSaveMapConfigurationRQ = JAXBMarshaller.unmarshall(textMessage, SpatialSaveOrUpdateMapConfigurationRQ.class);
                    SpatialMessageEvent saveMapConfigurationEvent = new SpatialMessageEvent(textMessage, spatialSaveMapConfigurationRQ);
                    saveMapConfigurationSpatialEvent.fire(saveMapConfigurationEvent);
                    break;
                case PING:
                    PingRQ pingRQ = JAXBMarshaller.unmarshall(textMessage, PingRQ.class);
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
