package eu.europa.ec.fisheries.uvms.spatial.message.bean;

import eu.europa.ec.fisheries.uvms.message.AbstractMessageService;
import eu.europa.ec.fisheries.uvms.spatial.message.SpatialConstants;
import eu.europa.ec.fisheries.uvms.spatial.message.event.SpatialMessageErrorEvent;
import eu.europa.ec.fisheries.uvms.spatial.message.event.SpatialMessageEvent;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMarshallException;
import eu.europa.ec.fisheries.uvms.spatial.model.mapper.JAXBMarshaller;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Observes;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;

import static eu.europa.ec.fisheries.uvms.message.MessageConstants.CONNECTION_FACTORY;
import static eu.europa.ec.fisheries.uvms.message.MessageConstants.QUEUE_MODULE_SPATIAL;

@Stateless
@LocalBean
@Slf4j
public class SpatialMessageServiceBean extends AbstractMessageService {

    @Resource(mappedName = QUEUE_MODULE_SPATIAL)
    private Destination request;

    @Resource(lookup = CONNECTION_FACTORY)
    private ConnectionFactory connectionFactory;

    @Override
    public ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    @Override
    protected Destination getEventDestination() {
        return request;
    }

    @Override
    protected Destination getResponseDestination() {
        return null;
    }

    @Override
    public String getModuleName() {
        return SpatialConstants.MODULE_NAME;
    }

    @Override
    public long getMilliseconds() {
        return 10000L;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void sendModuleErrorResponseMessage(@Observes @SpatialMessageErrorEvent SpatialMessageEvent message){
        try {
            log.info("Sending message back to recipient from SpatialModule with correlationId {} on queue: {}", message.getMessage().getJMSMessageID(),
                    message.getMessage().getJMSReplyTo());
            Session session = connectToQueue();
            String data = JAXBMarshaller.marshall(message.getFault());
            TextMessage response = session.createTextMessage(data);
            response.setJMSCorrelationID(message.getMessage().getJMSMessageID());
            session.createProducer(message.getMessage().getJMSReplyTo()).send(response);
        } catch (JMSException | SpatialModelMarshallException e) {
            log.error("[ Error when returning module spatial request. ] {} {}", e.getMessage(), e.getStackTrace());
        } finally {
            disconnectQueue();
        }
    }

}
