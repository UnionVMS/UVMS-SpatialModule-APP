/*
﻿Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
© European Union, 2015-2016.
This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
redistribute it and/or modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.europa.ec.fisheries.uvms.spatial.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@MessageDriven(mappedName = "jms/queue/UVMSUserEvent", activationConfig = {
        @ActivationConfigProperty(propertyName = "messagingType", propertyValue = "javax.jms.MessageListener"), 
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"), 
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "UVMSUserEvent")})
public class UserModuleMock implements MessageListener {

    final static Logger LOG = LoggerFactory.getLogger(UserModuleMock.class);
    
    /*@Inject
    SpatialProducer messageProducer;*/
    
    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
           /* UserBaseRequest request = JAXBMarshaller.unmarshallTextMessage(textMessage, UserBaseRequest.class);
            switch (request.getMethod()) {
                case GET_USER_CONTEXT:
                    UserContext userContext = getUserContext();
                    String responseString;
                    responseString = UserModuleResponseMapper.mapToGetUserContextResponse(userContext);
                    
                    messageProducer.sendResponseMessageToSender((TextMessage) message, responseString);
                    break;
                case GET_DEPLOYMENT_DESCRIPTOR:
                    Application application = new Application();
                    application.setDescription("Spatial");
                    application.setName("Spatial");
                    Option option = new Option();
                    option.setDataType("TestDataType");
                    option.setDefaultValue("");
                    option.setDescription("Test Option");
                    option.setName("DEFAULT_CONFIG");
                    application.getOption().add(option);
                
                    String responseString2 = UserModuleResponseMapper.mapToGetApplicationResponse(application);
                    messageProducer.sendResponseMessageToSender((TextMessage) message, responseString2);
                    break;
                default:
                    messageProducer.sendResponseMessageToSender((TextMessage) message, null);
                    break;
            }*/
        } catch (Exception e) {
            LOG.error("UserModuleMock Error", e);
        }
    }
    
   /* private UserContext getUserContext() throws JsonProcessingException {
        UserContext userContext = new UserContext();
        userContext.setContextSet(new ContextSet());
        Context context = new Context();
        Role role = new Role();
        role.setRoleName("testRole");
        context.setRole(role);
        Preferences preferences = new Preferences();
        Preference preference = new Preference();
        preference.setApplicationName("Spatial");
        preference.setOptionName("USER_CONFIG");
        preference.setOptionValue(getConfigurationDtoAsJson());
        preferences.getPreference().add(preference);
        context.setPreferences(preferences);
        Scope scope = new Scope();
        scope.setScopeName("testScope");
        Dataset dataset = new Dataset();
        dataset.setCategory("APA");
        scope.getDataset().add(dataset);
        context.setScope(scope);
        addFeature(UnionVMSFeature.manageManualMovements, userContext, context);
        addFeature(UnionVMSFeature.viewMovements, userContext, context);
        addFeature(UnionVMSFeature.viewManualMovements, userContext, context);
        addFeature(UnionVMSFeature.manageAlarmsHoldingTable, userContext, context);
        addFeature(UnionVMSFeature.viewAlarmsHoldingTable, userContext, context);
        userContext.getContextSet().getContexts().add(context);
        return userContext;
    }

    private void addFeature(UnionVMSFeature unionVMSFeature, UserContext userContext, Context context) {
        Feature feature = new Feature();
        feature.setName(unionVMSFeature.name());
        context.getRole().getFeature().add(feature);
        userContext.getContextSet().getContexts().add(context);
    }
    
    private String getConfigurationDtoAsJson() throws JsonProcessingException {
        ConfigurationDto dto = new ConfigurationDto();
        MapSettingsDto mapSettings = new MapSettingsDto();
        mapSettings.setCoordinatesFormat("apa");
        mapSettings.setMapProjectionId(1);
        dto.setMapSettings(mapSettings);
        LayerSettingsDto layerSettings = new LayerSettingsDto();
        List<LayersDto> layers = new ArrayList<>();
        layers.add(new LayersDto("1", 1l));
        layerSettings.setBaseLayers(layers);
        dto.setLayerSettings(layerSettings);
        dto.setReferenceData(new HashMap<>());
        
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(dto);
    }*/
}
