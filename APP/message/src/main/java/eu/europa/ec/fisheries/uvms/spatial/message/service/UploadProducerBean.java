package eu.europa.ec.fisheries.uvms.spatial.message.service;

import eu.europa.ec.fisheries.uvms.message.AbstractProducer;
import eu.europa.ec.fisheries.uvms.message.MessageConstants;
import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.jms.Destination;

@Stateless
@Local
public class UploadProducerBean extends AbstractProducer {

    @Resource(mappedName = MessageConstants.UPLOAD_EVENT_QUEUE)
    private Destination destination;

    @Override
    protected Destination getDestination() {
        return destination;
    }
}
