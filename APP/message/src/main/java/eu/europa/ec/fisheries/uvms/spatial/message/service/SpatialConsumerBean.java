package eu.europa.ec.fisheries.uvms.spatial.message.service;

import eu.europa.ec.fisheries.uvms.message.AbstractConsumer;
import eu.europa.ec.fisheries.uvms.message.MessageConstants;
import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.jms.Destination;

@Stateless
@Local
public class SpatialConsumerBean extends AbstractConsumer {

    @Resource(mappedName = MessageConstants.QUEUE_SPATIAL)
    private Destination destination;

    @Override
    public Destination getDestination() {
        return destination;
    }
}
