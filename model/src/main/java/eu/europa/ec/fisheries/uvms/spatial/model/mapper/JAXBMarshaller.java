package eu.europa.ec.fisheries.uvms.spatial.model.mapper;

import eu.europa.ec.fisheries.uvms.message.JAXBMarshallerUtils;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMarshallException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBException;

public class JAXBMarshaller {

    private static Logger LOG = LoggerFactory.getLogger(JAXBMarshaller.class);

    /**
     * Marshalls a JAXB Object to a XML String representation
     *
     * @param <T>
     * @param data
     * @return
     * @throws SpatialModelMarshallException
     */
    public static <T> String marshallJaxBObjectToString(T data) throws SpatialModelMarshallException {
        try {
            return JAXBMarshallerUtils.marshallJaxBObjectToString(data);
        } catch (JAXBException e) {
            LOG.error("[ Error when marshalling data. ] {}", e.getMessage());
            throw new SpatialModelMarshallException("Error when marshalling " + data.getClass().getName() + " to String");
        }
    }

    /**
     * Unmarshalls A textMessage to the desired Object. The object must be the
     * root object of the unmarshalled message!
     *
     * @param <R>
     * @param textMessage
     * @param clazz clazz
     * @return
     * @throws SpatialModelMarshallException
     */
    public static <R> R unmarshallTextMessage(TextMessage textMessage, Class clazz) throws SpatialModelMarshallException {
        try {
            return JAXBMarshallerUtils.unmarshallTextMessage(textMessage, clazz);
        } catch (JMSException | JAXBException e) {
            LOG.error("[ Error when unmarshalling data. ] {}", e.getMessage());
            throw new SpatialModelMarshallException("Error when unmarshalling response in ResponseMapper: " + e.getMessage());
        }
    }

}
