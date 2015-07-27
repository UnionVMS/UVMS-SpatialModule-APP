package eu.europa.ec.fisheries.uvms.spatial.message.producer;

import eu.europa.ec.fisheries.uvms.spatial.message.constants.ModuleQueue;
import eu.europa.ec.fisheries.uvms.spatial.message.exception.MovementMessageException;

import javax.ejb.Local;

@Local
public interface MessageProducer {

    public String sendModuleMessage(String text, ModuleQueue queue) throws MovementMessageException;

}
