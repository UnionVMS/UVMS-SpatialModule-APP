package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.message.MessageException;
import eu.europa.ec.fisheries.uvms.reporting.model.exception.ReportingModelException;
import eu.europa.ec.fisheries.uvms.reporting.model.mappper.ReportingModuleRequestMapper;
import eu.europa.ec.fisheries.uvms.reporting.model.mappper.ReportingModuleResponseMapper;
import eu.europa.ec.fisheries.uvms.reporting.model.schemas.ReportGetStartAndEndDateRS;
import eu.europa.ec.fisheries.uvms.spatial.message.service.ReportingProducerBean;
import eu.europa.ec.fisheries.uvms.spatial.message.service.SpatialConsumerBean;
import eu.europa.ec.fisheries.uvms.spatial.service.ReportingService;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

/**
 * Created by padhyad on 3/21/2016.
 */
@Stateless
@Local(ReportingService.class)
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
            String correlationId = producer.sendModuleMessage(request, consumer.getDestination());
            Message message = consumer.getMessage(correlationId, TextMessage.class);
            return ReportingModuleResponseMapper.mapToReportGetStartAndEndDateRS(getText(message), correlationId);
        } catch (ReportingModelException | MessageException | JMSException e) {
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
