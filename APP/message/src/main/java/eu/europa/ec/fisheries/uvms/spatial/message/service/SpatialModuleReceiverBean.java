package eu.europa.ec.fisheries.uvms.spatial.message.service;

import eu.europa.ec.fisheries.uvms.message.AbstractConsumer;

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

    @Resource(mappedName = "java:/jms/queue/UVMSSpatial")
    private Destination destination;

    @Override
    public Destination getDestination() {
        return destination;
    }
}
