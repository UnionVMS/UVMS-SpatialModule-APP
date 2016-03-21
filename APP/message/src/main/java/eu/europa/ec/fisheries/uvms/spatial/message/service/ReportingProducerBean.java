package eu.europa.ec.fisheries.uvms.spatial.message.service;

import eu.europa.ec.fisheries.uvms.message.AbstractProducer;

import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.jms.Destination;

/**
 * Created by padhyad on 3/21/2016.
 */
@Stateless
@Local
public class ReportingProducerBean extends AbstractProducer {

    @Resource(mappedName = "java:/jms/queue/UVMSReportingEvent")
    private Destination destination;

    @Override
    protected Destination getDestination() {
        return destination;
    }
}
