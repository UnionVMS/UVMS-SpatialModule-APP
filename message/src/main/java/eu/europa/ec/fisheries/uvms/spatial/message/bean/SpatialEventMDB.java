/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.message.bean;

import eu.europa.ec.fisheries.uvms.commons.message.api.Fault;
import eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils;
import eu.europa.ec.fisheries.uvms.spatial.message.event.AreaByCodeEvent;
import eu.europa.ec.fisheries.uvms.spatial.message.event.DeleteMapConfigurationEvent;
import eu.europa.ec.fisheries.uvms.spatial.message.event.GetAreaByLocationEvent;
import eu.europa.ec.fisheries.uvms.spatial.message.event.GetAreaTypeNamesEvent;
import eu.europa.ec.fisheries.uvms.spatial.message.event.GetClosestAreaEvent;
import eu.europa.ec.fisheries.uvms.spatial.message.event.GetClosestLocationEvent;
import eu.europa.ec.fisheries.uvms.spatial.message.event.GetFilterAreaEvent;
import eu.europa.ec.fisheries.uvms.spatial.message.event.GetGeometryByPortCodeEvent;
import eu.europa.ec.fisheries.uvms.spatial.message.event.GetMapConfigurationEvent;
import eu.europa.ec.fisheries.uvms.spatial.message.event.GetSpatialEnrichmentEvent;
import eu.europa.ec.fisheries.uvms.spatial.message.event.PingEvent;
import eu.europa.ec.fisheries.uvms.spatial.message.event.SaveOrUpdateMapConfigurationEvent;
import eu.europa.ec.fisheries.uvms.spatial.message.event.SpatialMessageEvent;
import eu.europa.ec.fisheries.uvms.spatial.model.enums.FaultCode;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AllAreaTypesRequest;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaByCodeRequest;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaByLocationSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestLocationSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.FilterAreasSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.GeometryByPortCodeRequest;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.PingRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialDeleteMapConfigurationRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialEnrichmentRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialGetMapConfigurationRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialModuleMethod;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialModuleRequest;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialSaveOrUpdateMapConfigurationRQ;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
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

    @Inject
    private SpatialProducer producer;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void onMessage(Message message) {

        TextMessage textMessage = (TextMessage) message;

        try {

            SpatialModuleRequest request = JAXBUtils.unMarshallMessage(textMessage.getText(), SpatialModuleRequest.class);
            SpatialModuleMethod method = request.getMethod();

            switch (method) {
                case GET_AREA_BY_LOCATION:
                    AreaByLocationSpatialRQ byLocationSpatialRQ = JAXBUtils.unMarshallMessage(textMessage.getText(), AreaByLocationSpatialRQ.class);
                    SpatialMessageEvent areaByLocationEvent = new SpatialMessageEvent(textMessage, byLocationSpatialRQ);
                    areaByLocationSpatialEvent.fire(areaByLocationEvent);
                    break;
                case GET_AREA_TYPES:
                    AllAreaTypesRequest allAreaTypesRequest = JAXBUtils.unMarshallMessage(textMessage.getText(), AllAreaTypesRequest.class);
                    SpatialMessageEvent areaTypeNamesEvent = new SpatialMessageEvent(textMessage, allAreaTypesRequest);
                    typeNamesEvent.fire(areaTypeNamesEvent);
                    break;
                case GET_CLOSEST_AREA:
                    ClosestAreaSpatialRQ closestAreaSpatialRQ = JAXBUtils.unMarshallMessage(textMessage.getText(), ClosestAreaSpatialRQ.class);
                    SpatialMessageEvent closestAreaEvent = new SpatialMessageEvent(textMessage, closestAreaSpatialRQ);
                    closestAreaSpatialEvent.fire(closestAreaEvent);
                    break;
                case GET_CLOSEST_LOCATION:
                    ClosestLocationSpatialRQ closestLocationSpatialRQ = JAXBUtils.unMarshallMessage(textMessage.getText(), ClosestLocationSpatialRQ.class);
                    SpatialMessageEvent closestLocationEvent = new SpatialMessageEvent(textMessage, closestLocationSpatialRQ);
                    closestLocationSpatialEvent.fire(closestLocationEvent);
                    break;
                case GET_ENRICHMENT:
                    SpatialEnrichmentRQ spatialEnrichmentRQ = JAXBUtils.unMarshallMessage(textMessage.getText(), SpatialEnrichmentRQ.class);
                    SpatialMessageEvent spatialEnrichmentEvent = new SpatialMessageEvent(textMessage, spatialEnrichmentRQ);
                    enrichmentSpatialEvent.fire(spatialEnrichmentEvent);
                    break;
                case GET_FILTER_AREA:
                    FilterAreasSpatialRQ filterAreasSpatialRQ = JAXBUtils.unMarshallMessage(textMessage.getText(), FilterAreasSpatialRQ.class);
                    SpatialMessageEvent filterAreaEvent = new SpatialMessageEvent(textMessage, filterAreasSpatialRQ);
                    filterAreaSpatialEvent.fire(filterAreaEvent);
                    break;
                case GET_MAP_CONFIGURATION:
                    SpatialGetMapConfigurationRQ spatialGetMapConfigurationRQ = JAXBUtils.unMarshallMessage(textMessage.getText(), SpatialGetMapConfigurationRQ.class);
                    SpatialMessageEvent getMapConfigurationEvent = new SpatialMessageEvent(textMessage, spatialGetMapConfigurationRQ);
                    getMapConfigurationSpatialEvent.fire(getMapConfigurationEvent);
                    break;
                case SAVE_OR_UPDATE_MAP_CONFIGURATION:
                    SpatialSaveOrUpdateMapConfigurationRQ spatialSaveMapConfigurationRQ = JAXBUtils.unMarshallMessage(textMessage.getText(), SpatialSaveOrUpdateMapConfigurationRQ.class);
                    SpatialMessageEvent saveMapConfigurationEvent = new SpatialMessageEvent(textMessage, spatialSaveMapConfigurationRQ);
                    saveOrUpdateMapConfigurationSpatialEvent.fire(saveMapConfigurationEvent);
                    break;
                case DELETE_MAP_CONFIGURATION:
                    SpatialDeleteMapConfigurationRQ mapConfigurationRQ = JAXBUtils.unMarshallMessage(textMessage.getText(), SpatialDeleteMapConfigurationRQ.class);
                    SpatialMessageEvent spatialMessageEvent = new SpatialMessageEvent(textMessage, mapConfigurationRQ);
                    deleteMapConfigurationSpatialEvent.fire(spatialMessageEvent);
                    break;
                case PING:
                    PingRQ pingRQ = JAXBUtils.unMarshallMessage(textMessage.getText(), PingRQ.class);
                    SpatialMessageEvent pingEvent = new SpatialMessageEvent(textMessage, pingRQ);
                    pingSpatialEvent.fire(pingEvent);
                    break;
                case GET_AREA_BY_CODE:
                    AreaByCodeRequest areaByCodeRequest = JAXBUtils.unMarshallMessage(textMessage.getText(), AreaByCodeRequest.class);
                    SpatialMessageEvent areaByCodeEvent = new SpatialMessageEvent(textMessage, areaByCodeRequest);
                    areaByCodeSpatialEvent.fire(areaByCodeEvent);
                    break;
                case GET_GEOMETRY_BY_PORT_CODE:
                    GeometryByPortCodeRequest geometryByPortCodeRequest = JAXBUtils.unMarshallMessage(textMessage.getText(), GeometryByPortCodeRequest.class);
                    SpatialMessageEvent geometryByPortCodeEvent = new SpatialMessageEvent(textMessage, geometryByPortCodeRequest);
                    geometryByPortCodeSpatialEvent.fire(geometryByPortCodeEvent);
                    break;
                default:
                    log.error("[ Not implemented method consumed: {} ]", method);
                    Fault fault = new Fault(FaultCode.SPATIAL_MESSAGE.getCode(), "Method not implemented");
                    producer.sendFault(textMessage,fault);
            }

        } catch (JMSException | JAXBException e) {
            Fault fault = new Fault(FaultCode.SPATIAL_MESSAGE.getCode(), "ERROR OCCURRED IN SPATIAL MDB");
            producer.sendFault(textMessage, fault);
        }
    }
}