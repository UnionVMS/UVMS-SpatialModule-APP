package eu.europa.ec.fisheries.uvms.spatial.message.consumer.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.exception.ModelMapperException;
import eu.europa.ec.fisheries.uvms.spatial.model.mapper.JAXBMarshaller;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/*
@MessageDriven(mappedName = MessageConstants.COMPONENT_MESSAGE_IN_QUEUE, activationConfig = {
    @ActivationConfigProperty(propertyName = "messagingType", propertyValue = MessageConstants.CONNECTION_TYPE),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = MessageConstants.DESTINATION_TYPE_QUEUE),
    @ActivationConfigProperty(propertyName = "destination", propertyValue = MessageConstants.COMPONENT_MESSAGE_IN_QUEUE_NAME)
})*/
public class MessageConsumerBean  {

    final static Logger LOG = LoggerFactory.getLogger(MessageConsumerBean.class);
/*
    @Inject
    @CreateMovementEvent
    Event<EventMessage> createMovementEvent;

    @Inject
    @ErrorEvent
    Event<EventMessage> errorEvent;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            LOG.info("Message received in spatial");

            MovementBaseRequest request = JAXBMarshaller.unmarshallTextMessage(textMessage, MovementBaseRequest.class);

            if (request.getMethod() == null) {
                LOG.error("[ Request method is null ]");
                errorEvent.fire(new EventMessage(textMessage, "Error when receiving message in spatial: "));
            }

            switch (request.getMethod()) {
                case CREATE:
                    createMovementEvent.fire(new EventMessage(textMessage));
                    break;
                case GET_SEGMENT_BY_ID:
                case GET_TRIP_BY_ID:
                default:
                    LOG.error("[ Request method {} is not implemented ]", request.getMethod().name());
                    errorEvent.fire(new EventMessage(textMessage, "[ Request method " + request.getMethod().name() + "  is not implemented ]"));
            }

        } catch (ModelMapperException | NullPointerException e) {
            LOG.error("[ Error when receiving message in spatial: ] {}", e.getStackTrace());
            errorEvent.fire(new EventMessage(textMessage, "Error when receiving message in spatial: " + e.getMessage()));
        }
    }*/

}
