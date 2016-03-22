package eu.europa.ec.fisheries.uvms.spatial.message.service;

import eu.europa.ec.fisheries.uvms.message.AbstractConsumer;
import eu.europa.ec.fisheries.uvms.message.MessageConstants;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.jms.Destination;

/**
 * Created by padhyad on 3/21/2016.
 */
@Stateless
@Local
public class SpatialModuleReceiverBean extends AbstractConsumer {

    @Resource(mappedName = MessageConstants.QUEUE_SPATIAL)
    private Destination destination;

    @Override
    public Destination getDestination() {
        return destination;
    }
}
