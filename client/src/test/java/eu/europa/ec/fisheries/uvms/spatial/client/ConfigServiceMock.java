package eu.europa.ec.fisheries.uvms.spatial.client;

import eu.europa.ec.fisheries.schema.config.types.v1.PullSettingsStatus;
import eu.europa.ec.fisheries.schema.config.types.v1.SettingType;
import eu.europa.ec.fisheries.uvms.config.model.mapper.ModuleResponseMapper;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.Collections;

@MessageDriven(mappedName = "jms/queue/UVMSConfigEvent", activationConfig = {
        @ActivationConfigProperty(propertyName = "messagingType", propertyValue = "javax.jms.MessageListener"), 
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"), 
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "UVMSConfigEvent")})
public class ConfigServiceMock implements MessageListener {

    @Inject
    private SpatialProducer producer;
    
    @Override
    public void onMessage(Message message) {
        try {
            SettingType endpointSetting = new SettingType();
            endpointSetting.setKey("APA");
            endpointSetting.setValue("BEPA");
            endpointSetting.setDescription("From ConfigServiceMock.java");
            String response = ModuleResponseMapper.toPullSettingsResponse(Collections.singletonList(endpointSetting), PullSettingsStatus.OK);
            producer.sendResponseMessageToSender((TextMessage) message, response);
        } catch (JMSException e) {
        }
    }
}
