package eu.europa.ec.fisheries.uvms.spatial.message.bean;

import eu.europa.ec.fisheries.uvms.message.AbstractProducer;
import eu.europa.ec.fisheries.uvms.message.MessageConstants;
import eu.europa.ec.fisheries.uvms.spatial.message.SpatialConstants;
import eu.europa.ec.fisheries.uvms.spatial.message.SpatialMessageConstants;
import eu.europa.ec.fisheries.uvms.spatial.message.event.SpatialMessageErrorEvent;
import eu.europa.ec.fisheries.uvms.spatial.message.event.SpatialMessageEvent;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMarshallException;
import eu.europa.ec.fisheries.uvms.spatial.model.mapper.JAXBMarshaller;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.TextMessage;

@Stateless
@LocalBean
@Slf4j
public class SpatialEventQ extends AbstractProducer {

    @Resource(mappedName = SpatialMessageConstants.QUEUE_MODULE_SPATIAL)
    private Destination eventQueue;

    @Resource(lookup = MessageConstants.CONNECTION_FACTORY)
    private ConnectionFactory connectionFactory;

    @Override
    public ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    @Override
    public String getModuleName() {
        return SpatialConstants.MODULE_NAME;
    }

    @Override
    public Destination getDestination() {
        return eventQueue;
    }

    public void sendModuleErrorResponseMessage(@Observes @SpatialMessageErrorEvent SpatialMessageEvent message){
        try {
            log.info("Sending message back to recipient from SpatialModule with correlationId {} on queue: {}", message.getMessage().getJMSMessageID(),
                    message.getMessage().getJMSReplyTo());
            connectQueue();
            String data = JAXBMarshaller.marshallJaxBObjectToString(message.getFault());
            TextMessage response = getSession().createTextMessage(data);
            response.setJMSCorrelationID(message.getMessage().getJMSMessageID());
            getSession().createProducer(message.getMessage().getJMSReplyTo()).send(response);
        } catch (JMSException | SpatialModelMarshallException e) {
            log.error("[ Error when returning module spatial request. ] {} {}", e.getMessage(), e.getStackTrace());
        } finally {
            disconnectQueue();
        }
    }

}
