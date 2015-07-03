/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.europa.ec.fisheries.uvms.spatial.model.mapper;

import java.util.List;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.fisheries.uvms.spatial.model.exception.ModelMapperException;

/**
 *
 * @author jojoha
 */
public class DataSourceResponseMapper {

    final static Logger LOG = LoggerFactory.getLogger(DataSourceRequestMapper.class);

    /**
     * Validates a response
     *
     * @param response
     * @param correlationId
     * @throws ModelMapperException
     * @throws JMSException
     */
    private static void validateResponse(TextMessage response, String correlationId) throws ModelMapperException, JMSException {

        if (response == null) {
            LOG.error("[ Error when validating response in ResponseMapper: Response is Null ]");
            throw new ModelMapperException("[ Error when validating response in ResponseMapper: Response is Null ]");
        }

        if (response.getJMSCorrelationID() == null) {
            LOG.error("[ No corelationId in response.] Expected was: {} ", correlationId);
            throw new ModelMapperException("[ No corelationId in response (Null) ] . Expected was: " + correlationId);
        }

        if (!correlationId.equalsIgnoreCase(response.getJMSCorrelationID())) {
            LOG.error("[ Wrong corelationId in response. Expected was {0} But actual was: {1} ]", correlationId, response.getJMSCorrelationID());
            throw new ModelMapperException("[ Wrong corelationId in response. ] Expected was: " + correlationId + "But actual was: " + response.getJMSCorrelationID());
        }

        // TODO: Check if fault
        // try {
        // Fault fault = JAXBMarshaller.unmarshallTextMessage(response,
        // VesselFault.class);
        // throw new VesselModelValidateException(fault.getCode() + " : " +
        // fault.getFault());
        // } catch (VesselModelUnmarshallException e) {
        // //everything is well
        // }
    }
/*

    public static List<MovementBaseType> mapToMovementBaseTypeListFromResponse(TextMessage message) throws ModelMapperException, JMSException {
        validateResponse(message, message.getJMSCorrelationID());
        GetMovementListByQueryResponse response = JAXBMarshaller.unmarshallTextMessage(message, GetMovementListByQueryResponse.class);
        return response.getMovement();
    }

    public static GetMovementListByQueryResponse mapToGetMovementListByQueryResponse(TextMessage message) throws ModelMapperException, JMSException {
        validateResponse(message, message.getJMSCorrelationID());
        GetMovementListByQueryResponse response = JAXBMarshaller.unmarshallTextMessage(message, GetMovementListByQueryResponse.class);
        return response;
    }

    public static MovementBaseType mapToMovementBaseTypeFromResponse(TextMessage message) throws ModelMapperException, JMSException {
        validateResponse(message, message.getJMSCorrelationID());
        CreateMovementResponse response = JAXBMarshaller.unmarshallTextMessage(message, CreateMovementResponse.class);
        return response.getMovement();
    }

    public static MovementSearchGroup mapToMovementSearchGroupFromResponse(TextMessage message) throws ModelMapperException, JMSException {
        validateResponse(message, message.getJMSCorrelationID());
        SingleMovementSearchGroupResponse response = JAXBMarshaller.unmarshallTextMessage(message, SingleMovementSearchGroupResponse.class);
        return response.getSearchGroup();
    }

    public static List<MovementSearchGroup> mapToMovementSearchGroupListFromResponse(TextMessage message) throws ModelMapperException, JMSException {
        validateResponse(message, message.getJMSCorrelationID());
        ListMovementSearchGroupResponse response = JAXBMarshaller.unmarshallTextMessage(message, ListMovementSearchGroupResponse.class);
        return response.getSearchGroups();
    }

    public static TempMovementType mapToCreateTempMovementFromResponse(TextMessage message) throws ModelMapperException, JMSException {
        validateResponse(message, message.getJMSCorrelationID());
        CreateTempMovementResponse response = JAXBMarshaller.unmarshallTextMessage(message, CreateTempMovementResponse.class);
        return response.getMovement();
    }

    public static TempMovementType mapToSetStatusMovementFromResponse(TextMessage message) throws ModelMapperException, JMSException {
        validateResponse(message, message.getJMSCorrelationID());
        SetStatusMovementResponse response = JAXBMarshaller.unmarshallTextMessage(message, SetStatusMovementResponse.class);
        return response.getMovement();
    }

    public static TempMovementType mapToUpdateTempMovementFromResponse(TextMessage message) throws ModelMapperException, JMSException {
        validateResponse(message, message.getJMSCorrelationID());
        UpdateTempMovementResponse response = JAXBMarshaller.unmarshallTextMessage(message, UpdateTempMovementResponse.class);
        return response.getMovement();
    }

    public static GetTempMovementListResponse mapToTempMovementListFromResponse(TextMessage message) throws ModelMapperException, JMSException {
        validateResponse(message, message.getJMSCorrelationID());
        GetTempMovementListResponse response = JAXBMarshaller.unmarshallTextMessage(message, GetTempMovementListResponse.class);
        return response;
    }

    public static TempMovementType mapToSendTempMovementFromResponse(TextMessage message) throws ModelMapperException, JMSException {
        validateResponse(message, message.getJMSCorrelationID());
        SendTempMovementResponse response = JAXBMarshaller.unmarshallTextMessage(message, SendTempMovementResponse.class);
        return response.getMovement();
    }
*/

}
