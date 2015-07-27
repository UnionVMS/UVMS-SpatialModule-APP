package eu.europa.ec.fisheries.uvms.spatial.message.consumer;

import javax.ejb.Local;

@Local
public interface MessageConsumer {

    public <T> T getMessage(String correlationId, Class type) throws Exception;

}
