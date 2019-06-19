/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service.bean.impl;

import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingModelException;
import eu.europa.ec.fisheries.uvms.reporting.model.mappper.ReportingModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.reporting.model.mappper.ReportingModuleResponseMapper;
import eu.europa.ec.fisheries.uvms.reporting.model.schemas.ReportGetStartAndEndDateRS;
import eu.europa.ec.fisheries.uvms.spatial.message.service.ReportingProducerBean;
import eu.europa.ec.fisheries.uvms.spatial.message.service.SpatialConsumerBean;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.ReportingService;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

@Stateless
@Slf4j
public class ReportingServiceBean implements ReportingService {

    @EJB
    private ReportingProducerBean producer;

    @EJB
    private SpatialConsumerBean consumer;

    @Override
    public ReportGetStartAndEndDateRS getReportDates(Integer reportId, String userName, String scopeName, String timeStamp) throws ServiceException {
        try {
            String request = ReportingModuleRequestMapper.mapToSpatialSaveOrUpdateMapConfigurationRQ(timeStamp, reportId.longValue(), userName, scopeName);
            String correlationId = producer.sendMessage(request, consumer.getDestination());
            Message message = consumer.getMessage(correlationId, TextMessage.class);
            return ReportingModuleResponseMapper.mapToReportGetStartAndEndDateRS(getText(message), correlationId);
        } catch (ReportingModelException | JMSException e) {
            throw new ServiceException("Error in communication with Reporting module", e);
        }
    }

    private TextMessage getText(Message message) throws JMSException {
        if (message instanceof TextMessage) {
            return (TextMessage) message;
        }
        return null;
    }
}