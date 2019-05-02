/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.spatial.service.bean.impl;

import eu.europa.ec.fisheries.uvms.commons.message.api.Fault;
import eu.europa.ec.fisheries.uvms.commons.message.api.MessageException;
import eu.europa.ec.fisheries.uvms.commons.message.impl.JAXBUtils;
import eu.europa.ec.fisheries.uvms.spatial.message.service.SpatialConsumerBean;
import eu.europa.ec.fisheries.uvms.spatial.message.service.UserProducerBean;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMapperException;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelMarshallException;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.SpatialModelValidationException;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.usm.USMSpatial;
import eu.europa.ec.fisheries.uvms.spatial.service.exception.DataSetNotFoundException;
import eu.europa.ec.fisheries.uvms.user.model.exception.ModelMarshallException;
import eu.europa.ec.fisheries.uvms.user.model.mapper.UserModuleRequestMapper;
import eu.europa.ec.fisheries.wsdl.user.module.CreateDatasetResponse;
import eu.europa.ec.fisheries.wsdl.user.module.DeleteDatasetResponse;
import eu.europa.ec.fisheries.wsdl.user.module.FilterDatasetResponse;
import eu.europa.ec.fisheries.wsdl.user.types.DatasetExtension;
import eu.europa.ec.fisheries.wsdl.user.types.DatasetFilter;
import eu.europa.ec.fisheries.wsdl.user.types.DatasetList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBException;

import static eu.europa.ec.fisheries.uvms.spatial.service.bean.impl.UserAreaServiceBean.NOK;

@Stateless
@LocalBean
@Slf4j
public class SpatialUserServiceBean {

    private static final String ERROR_WHEN_MARSHALLING_DATA = "[ Error when marshalling data. ] {}";
    private static final String ERROR_WHEN_MARSHALLING_OBJECT_TO_STRING = "Error when marshalling object to String";

    @EJB
    private UserProducerBean userProducer;

    @EJB
    private SpatialConsumerBean consumer;

    public DatasetList listDatasets() throws ModelMarshallException, MessageException, SpatialModelMapperException {
        DatasetFilter filter = new DatasetFilter();
        filter.setApplicationName(USMSpatial.APPLICATION_NAME);
        filter.setCategory(USMSpatial.USM_DATASET_CATEGORY);
        String request = UserModuleRequestMapper.mapToFindDatasetRequest(filter);
        String correlationId = userProducer.sendModuleMessage(request, consumer.getDestination());
        TextMessage message = consumer.getMessage(correlationId, TextMessage.class);
        return mapToFindDataSetResponse(message, correlationId);
    }

    public void persistDataSetInUSM(String dataSetName, String discriminator) throws ModelMarshallException, MessageException, SpatialModelMapperException {
        DatasetExtension dataSet = new DatasetExtension();
        dataSet.setApplicationName(USMSpatial.APPLICATION_NAME);
        dataSet.setDiscriminator(discriminator);
        dataSet.setName(dataSetName);
        dataSet.setCategory(USMSpatial.USM_DATASET_CATEGORY);
        dataSet.setDescription(USMSpatial.USM_DATASET_DESCRIPTION);
        String payload = UserModuleRequestMapper.mapToCreateDatasetRequest(dataSet);
        String correlationId = userProducer.sendModuleMessage(payload, consumer.getDestination());
        TextMessage message = consumer.getMessage(correlationId, TextMessage.class);
        mapToCreateDatasetResponse(message, correlationId);
    }

    public String deleteDataSetNameFromUSM(String previousDataSetName, String applicationName, String discriminator) throws DataSetNotFoundException {
        String result = NOK;
        if (StringUtils.isNotBlank(previousDataSetName)) {
            DatasetExtension extension = new DatasetExtension();
            extension.setName(previousDataSetName);
            extension.setDiscriminator(discriminator);
            extension.setApplicationName(applicationName);

            try {
                String request = UserModuleRequestMapper.mapToDeleteDatasetRequest(extension);
                String correlationId = userProducer.sendModuleMessage(request, consumer.getDestination());
                TextMessage message = consumer.getMessage(correlationId, TextMessage.class, 4000L);
                validateResponse(message, correlationId);
                DeleteDatasetResponse deleteDatasetResponse = JAXBUtils.unMarshallMessage(message.getText(), DeleteDatasetResponse.class);
                return deleteDatasetResponse.getResponse();
            }
            catch (MessageException e){
                log.warn(e.getMessage(), e);
                throw new DataSetNotFoundException();
            } catch (SpatialModelMapperException | JMSException | JAXBException | ModelMarshallException e) {
                log.error(e.getMessage(), e);
            }
        }

        return result;
    }

    private DatasetList mapToFindDataSetResponse(TextMessage response, String correlationId) throws SpatialModelMapperException {
        try {
            validateResponse(response, correlationId);
            FilterDatasetResponse createDataSetResponse = JAXBUtils.unMarshallMessage(response.getText(), FilterDatasetResponse.class);
            return createDataSetResponse.getDatasetList();
        } catch (JMSException | JAXBException e) {
            log.error(ERROR_WHEN_MARSHALLING_DATA, e);
            throw new SpatialModelMarshallException(ERROR_WHEN_MARSHALLING_OBJECT_TO_STRING, e);
        }
    }

    private String mapToCreateDatasetResponse(TextMessage response, String correlationId) throws SpatialModelMapperException {
        try {
            validateResponse(response, correlationId);
            CreateDatasetResponse createDatasetResponse = JAXBUtils.unMarshallMessage(response.getText(), CreateDatasetResponse.class);
            return createDatasetResponse.getResponse();
        } catch (JMSException | JAXBException e) {
            log.error(ERROR_WHEN_MARSHALLING_DATA, e);
            throw new SpatialModelMarshallException(ERROR_WHEN_MARSHALLING_OBJECT_TO_STRING, e);
        }
    }

    private static void validateResponse(TextMessage response, String correlationId) throws SpatialModelValidationException {

        try {
            if (response == null) {
                throw new SpatialModelValidationException("Error when validating response in ResponseMapper: Response is Null");
            }

            if (response.getJMSCorrelationID() == null) {
                throw new SpatialModelValidationException("No correlationId in response (Null) . Expected was: " + correlationId);
            }

            if (!correlationId.equalsIgnoreCase(response.getJMSCorrelationID())) {
                throw new SpatialModelValidationException("Wrong correlationId in response. Expected was: " + correlationId + " But actual was: " + response.getJMSCorrelationID());
            }

            try {
                Fault fault = JAXBUtils.unMarshallMessage(response.getText(), Fault.class);
                throw new SpatialModelValidationException(fault.getCode() + " : " + fault.getFault());
            } catch (JAXBException e) {
                log.info("Expected Exception"); // Exception received in case if the validation is success
            }

        } catch (JMSException e) {
            log.error("JMS exception during validation ", e);
            throw new SpatialModelValidationException("JMS exception during validation " + e.getMessage());
        }
    }

}

