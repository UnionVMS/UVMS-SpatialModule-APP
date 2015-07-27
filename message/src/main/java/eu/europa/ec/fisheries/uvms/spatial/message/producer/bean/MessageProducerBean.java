/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.europa.ec.fisheries.uvms.spatial.message.producer.bean;

import eu.europa.ec.fisheries.uvms.spatial.message.constants.MessageConstants;
import eu.europa.ec.fisheries.uvms.spatial.message.constants.ModuleQueue;
import eu.europa.ec.fisheries.uvms.spatial.message.exception.MovementMessageException;
import eu.europa.ec.fisheries.uvms.spatial.message.producer.MessageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.*;
//import eu.europa.ec.fisheries.uvms.spatial.model.exception.ModelMarshallException;
//import eu.europa.ec.fisheries.uvms.spatial.model.mapper.JAXBMarshaller;

@Stateless
public class MessageProducerBean {

    /*final static Logger LOG = LoggerFactory.getLogger(MessageProducerBean.class);

    @Resource(lookup = MessageConstants.CONNECTION_FACTORY)
    private ConnectionFactory connectionFactory;

    //@Resource(mappedName = MessageConstants.COMPONENT_RESPONSE_QUEUE)
    //private Queue responseQueue;

    //@Resource(mappedName = MessageConstants.QUEUE_MODULE_MOVEMENT)
    //private Queue movementQueue;

    private Connection connection = null;
    private Session session = null;

    @Override
    public String sendModuleMessage(String text, ModuleQueue queue) throws MovementMessageException {

        try {

            connectToQueue();
            TextMessage message = session.createTextMessage();
            //message.setJMSReplyTo(responseQueue);
            message.setText(text);

            switch (queue) {
                case MOVEMENT:
                    //session.createProducer(movementQueue).send(message);
                    break;
            }
            return message.getJMSMessageID();

        } catch (JMSException e) {
            LOG.error("[ Error when sending message. ] {}", e.getMessage());
            throw new MovementMessageException("[ Error when sending message. ]", e);
        } finally {
            try {
                connection.stop();
                connection.close();
            } catch (JMSException e) {
                LOG.error("[ Error when closing JMS connection ] {}", e.getStackTrace());
                throw new MovementMessageException("[ Error when sending message. ]", e);
            }
        }
    }

    private void connectToQueue() throws JMSException {
        connection = connectionFactory.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        connection.start();
    }*/
}
