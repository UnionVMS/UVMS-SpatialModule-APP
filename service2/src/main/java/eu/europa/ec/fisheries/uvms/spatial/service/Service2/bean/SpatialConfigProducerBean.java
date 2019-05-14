/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.spatial.service.Service2.bean;

import eu.europa.ec.fisheries.uvms.commons.message.api.MessageConstants;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.commons.message.impl.AbstractProducer;
import eu.europa.ec.fisheries.uvms.commons.message.impl.JMSUtils;
import eu.europa.ec.fisheries.uvms.config.message.ConfigMessageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.jms.Queue;

@Stateless
public class SpatialConfigProducerBean extends AbstractProducer implements ConfigMessageProducer {

    private static final Logger LOG = LoggerFactory.getLogger(SpatialConfigProducerBean.class);

    private Queue spatialINQueue;

    @PostConstruct
    public void init(){
        spatialINQueue = JMSUtils.lookupQueue(MessageConstants.QUEUE_SPATIAL);
    }

    @Override
    public String sendConfigMessage(String textMsg) {
        try {
            return sendModuleMessage(textMsg, spatialINQueue);
        } catch (MessageException e) {
            LOG.error("[ERROR] Error while trying to send message to Config! Check SpatialConfigProducerBeanImpl..");
        }
        return "";
    }

    @Override
    public String getDestinationName() {
        return MessageConstants.QUEUE_CONFIG;
    }
}

