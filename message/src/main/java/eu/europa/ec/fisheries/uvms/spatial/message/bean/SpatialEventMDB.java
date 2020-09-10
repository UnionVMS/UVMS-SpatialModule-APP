/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.message.bean;

import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils;
import eu.europa.ec.fisheries.uvms.spatial.message.event.*;
import eu.europa.ec.fisheries.uvms.spatial.message.event.carrier.EventMessage;
import eu.europa.ec.fisheries.uvms.spatial.model.enums.FaultCode;
import eu.europa.ec.fisheries.uvms.spatial.model.mapper.SpatialModuleResponseMapper;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBException;

import static eu.europa.ec.fisheries.uvms.commons.message.api.MessageConstants.CONNECTION_TYPE;
import static eu.europa.ec.fisheries.uvms.commons.message.api.MessageConstants.DESTINATION_TYPE_QUEUE;
import static eu.europa.ec.fisheries.uvms.commons.message.api.MessageConstants.QUEUE_MODULE_SPATIAL;
import static eu.europa.ec.fisheries.uvms.commons.message.api.MessageConstants.QUEUE_MODULE_SPATIAL_NAME;

@MessageDriven(mappedName = QUEUE_MODULE_SPATIAL, activationConfig = {
        @ActivationConfigProperty(propertyName = "messagingType", propertyValue = CONNECTION_TYPE),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = DESTINATION_TYPE_QUEUE),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = QUEUE_MODULE_SPATIAL_NAME)
})
@Slf4j
public class SpatialEventMDB implements MessageListener {
	
	@Inject
    @SpatialMessageErrorEvent
    private Event<EventMessage> errorEvent;

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
    @GetSpatialBatchEnrichmentEvent
    private Event<SpatialMessageEvent> batchEnrichmentSpatialEvent;

    @Inject
    @GetUserAreaSpatialEnrichmentEvent
    private Event<SpatialMessageEvent> getUserAreaSpatialEnrichmentEvent;

    @Inject
    @GetUserAreaSpatialEnrichmentEventByWkt
    private Event<SpatialMessageEvent> getUserAreaSpatialEnrichmentEventByWkt;

    @Inject
    @GetUserAreaSpatialBatchEnrichmentEvent
    private Event<SpatialMessageEvent> getUserAreaSpatialBatchEnrichmentEvent;

    @Inject
    @GetClosestLocationEvent
    private Event<SpatialMessageEvent> closestLocationSpatialEvent;

    @Inject
    @GetFilterAreaEvent
    private Event<SpatialMessageEvent> filterAreaSpatialEvent;

    @Inject
    @SaveOrUpdateMapConfigurationEvent
    private Event<SpatialMessageEvent> saveOrUpdateMapConfigurationSpatialEvent;

    @Inject
    @DeleteMapConfigurationEvent
    private Event<SpatialMessageEvent> deleteMapConfigurationSpatialEvent;

    @Inject
    @GetMapConfigurationEvent
    private Event<SpatialMessageEvent> getMapConfigurationSpatialEvent;

    @Inject
    @PingEvent
    private Event<SpatialMessageEvent> pingSpatialEvent;

    @Inject
    @AreaByCodeEvent
    private Event<SpatialMessageEvent> areaByCodeSpatialEvent;

    @Inject
    @GetGeometryByPortCodeEvent
    private Event<SpatialMessageEvent> geometryByPortCodeSpatialEvent;

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            SpatialModuleRequest request = JAXBUtils.unMarshallMessage(textMessage.getText(), SpatialModuleRequest.class);
            SpatialModuleMethod method = request.getMethod();
            switch (method) {
                case GET_AREA_BY_LOCATION:
                    SpatialMessageEvent areaByLocationEvent = new SpatialMessageEvent(textMessage,request);
                    areaByLocationSpatialEvent.fire(areaByLocationEvent);
                    break;
                case GET_AREA_TYPES:
                    SpatialMessageEvent areaTypeNamesEvent = new SpatialMessageEvent(textMessage,request);
                    typeNamesEvent.fire(areaTypeNamesEvent);
                    break;
                case GET_CLOSEST_AREA:
                    SpatialMessageEvent closestAreaEvent = new SpatialMessageEvent(textMessage,request);
                    closestAreaSpatialEvent.fire(closestAreaEvent);
                    break;
                case GET_CLOSEST_LOCATION:
                    SpatialMessageEvent closestLocationEvent = new SpatialMessageEvent(textMessage,request);
                    closestLocationSpatialEvent.fire(closestLocationEvent);
                    break;
                case GET_ENRICHMENT:
                    SpatialMessageEvent spatialEnrichmentEvent = new SpatialMessageEvent(textMessage,request);
                    enrichmentSpatialEvent.fire(spatialEnrichmentEvent);
                    break;
                case GET_ENRICHMENT_BATCH:
                    SpatialMessageEvent spatialBatchEnrichmentEvent = new SpatialMessageEvent(textMessage,request);
                    batchEnrichmentSpatialEvent.fire(spatialBatchEnrichmentEvent);
                    break;
                case GET_USER_AREA_ENRICHMENT:
                    SpatialMessageEvent userAreaSpatialEnrichment = new SpatialMessageEvent(textMessage,request);
                    getUserAreaSpatialEnrichmentEvent.fire(userAreaSpatialEnrichment);
                    break;
                case GET_USER_AREA_ENRICHMENT_BY_WKT:
                    SpatialMessageEvent userAreaSpatialEnrichmentByWkt = new SpatialMessageEvent(textMessage,request);
                    getUserAreaSpatialEnrichmentEventByWkt.fire(userAreaSpatialEnrichmentByWkt);
                    break;
                case GET_USER_AREA_ENRICHMENT_BATCH:
                    SpatialMessageEvent userAreaBatchSpatialEnrichment = new SpatialMessageEvent(textMessage,request);
                    getUserAreaSpatialBatchEnrichmentEvent.fire(userAreaBatchSpatialEnrichment);
                    break;
                case GET_FILTER_AREA:
                    SpatialMessageEvent filterAreaEvent = new SpatialMessageEvent(textMessage,request);
                    filterAreaSpatialEvent.fire(filterAreaEvent);
                    break;
                case GET_MAP_CONFIGURATION:
                    SpatialMessageEvent getMapConfigurationEvent = new SpatialMessageEvent(textMessage,request);
                    getMapConfigurationSpatialEvent.fire(getMapConfigurationEvent);
                    break;
                case SAVE_OR_UPDATE_MAP_CONFIGURATION:
                    SpatialMessageEvent saveMapConfigurationEvent = new SpatialMessageEvent(textMessage,request);
                    saveOrUpdateMapConfigurationSpatialEvent.fire(saveMapConfigurationEvent);
                    break;
                case DELETE_MAP_CONFIGURATION:
                    SpatialMessageEvent spatialMessageEvent = new SpatialMessageEvent(textMessage,request);
                    deleteMapConfigurationSpatialEvent.fire(spatialMessageEvent);
                    break;
                case PING:
                    SpatialMessageEvent pingEvent = new SpatialMessageEvent(textMessage, request);
                    pingSpatialEvent.fire(pingEvent);
                    break;
                case GET_AREA_BY_CODE:
                    SpatialMessageEvent areaByCodeEvent = new SpatialMessageEvent(textMessage, request);
                    areaByCodeSpatialEvent.fire(areaByCodeEvent);
                    break;
                case GET_GEOMETRY_BY_PORT_CODE:
                    SpatialMessageEvent geometryByPortCodeEvent = new SpatialMessageEvent(textMessage, request);
                    geometryByPortCodeSpatialEvent.fire(geometryByPortCodeEvent);
                    break;
                default:
                    log.error("[ Not implemented method consumed: {} ]", method);
                    throw new MessageException(method + " Method not implemented");
            }
        } catch (JMSException | JAXBException | MessageException e) {
            errorEvent.fire(new EventMessage(textMessage, SpatialModuleResponseMapper.createFaultMessage(FaultCode.SPATIAL_MESSAGE, "Exception in spatial [ " + e.getMessage())));
        }
    }
}
