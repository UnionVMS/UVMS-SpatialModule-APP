/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.europa.ec.fisheries.uvms.spatial.model.mapper;

import eu.europa.ec.fisheries.schema.spatial.module.v1.CreateMovementResponse;
import eu.europa.ec.fisheries.schema.spatial.v1.MovementBaseType;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.ModelMapperException;
import eu.europa.ec.fisheries.uvms.spatial.model.exception.ModelMarshallException;
import javax.jms.JMSException;
import javax.jms.TextMessage;

/**
 *
 * @author jojoha
 */
public class ModuleResponseMapper {

    private static void validateResponse(TextMessage response, String correlationId) throws ModelMapperException, JMSException {

        if (response == null) {
            throw new ModelMapperException("Error when validating response in ResponseMapper: Reesponse is Null");
        }

        if (response.getJMSCorrelationID() == null) {
            throw new ModelMapperException("No corelationId in response (Null) . Expected was: " + correlationId);
        }

        if (!correlationId.equalsIgnoreCase(response.getJMSCorrelationID())) {
            throw new ModelMapperException("Wrong corelationId in response. Expected was: " + correlationId + "But actual was: " + response.getJMSCorrelationID());
        }

    }

    public static String mapToCreateMovementResponse(MovementBaseType movment) throws ModelMarshallException {
        CreateMovementResponse response = new CreateMovementResponse();
        response.setMovement(movment);
        return JAXBMarshaller.marshallJaxBObjectToString(response);
    }

}
