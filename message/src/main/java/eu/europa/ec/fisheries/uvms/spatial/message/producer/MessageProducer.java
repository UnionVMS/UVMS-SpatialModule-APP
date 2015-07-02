/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.europa.ec.fisheries.uvms.spatial.message.producer;

import eu.europa.ec.fisheries.uvms.spatial.message.constants.DataSourceQueue;
import eu.europa.ec.fisheries.uvms.spatial.message.constants.ModuleQueue;
import eu.europa.ec.fisheries.uvms.spatial.message.event.ErrorEvent;
import eu.europa.ec.fisheries.uvms.spatial.message.event.carrier.EventMessage;
import eu.europa.ec.fisheries.uvms.spatial.message.exception.MovementMessageException;
import javax.ejb.Local;
import javax.enterprise.event.Observes;
import javax.jms.TextMessage;

/**
 *
 * @author jojoha
 */
@Local
public interface MessageProducer {

    public String sendDataSourceMessage(String text, DataSourceQueue queue) throws MovementMessageException;
    
    public String sendModuleMessage(String text, ModuleQueue queue) throws MovementMessageException;

    public void sendErrorMessageBackToRecipient(@Observes @ErrorEvent EventMessage message) throws MovementMessageException;

    public void sendMessageBackToRecipient(TextMessage requestMessage, String returnMessage) throws MovementMessageException;

}
