package eu.europa.ec.fisheries.uvms.spatial.model.mapper;

import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMarshallException;
import lombok.extern.slf4j.Slf4j;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

@Slf4j
public final class JAXBMarshaller {

    private JAXBMarshaller() {}

    /**
     * Marshalls a JAXB Object to a XML String representation
     *
     * @param <T>
     * @param data
     * @return
     * @throws SpatialModelMarshallException
     */
    public static <T> String marshall(T data) throws SpatialModelMarshallException {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(new Class[]{data.getClass()});
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty("jaxb.formatted.output", Boolean.TRUE);
            StringWriter sw = new StringWriter();
            marshaller.marshal(data, sw);
            return sw.toString();
        } catch (JAXBException e) {
            log.error("[ Error when marshalling object to string. ]", e.getMessage());
            throw new SpatialModelMarshallException("Error when marshalling " , e);
        }
    }

    /**
     * Unmarshalls A textMessage to the desired Object. The object must be the
     * root object of the unmarshalled message!
     *
     * @param <R>
     * @param textMessage
     * @param clazz       clazz
     * @return
     * @throws SpatialModelMarshallException
     */
    public static <R> R unmarshall(TextMessage textMessage, Class clazz) throws SpatialModelMarshallException {
        try {
            JAXBContext jc = JAXBContext.newInstance(new Class[]{clazz});
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            StringReader sr = new StringReader(textMessage.getText());
            return (R) unmarshaller.unmarshal(sr);
        } catch (JMSException | JAXBException e) {
            log.error("[ Error when unmarshalling Text message to object. ]", e);
            throw new SpatialModelMarshallException("Error when unmarshalling response in ResponseMapper: ", e);
        }
    }

}
