package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ErrorMessageType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ErrorsType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ResponseMessageType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SuccessType;

import java.util.ArrayList;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by kopyczmi on 14-Aug-15.
 */
public class AbstractServiceBean {

    protected ResponseMessageType createSuccessResponseMessage() {
        ResponseMessageType responseMessage = new ResponseMessageType();
        responseMessage.setSuccess(new SuccessType());
        return responseMessage;
    }

    protected ResponseMessageType createErrorResponseMessage(String errorMessage, Integer errorCode) {
        ResponseMessageType responseMessage = new ResponseMessageType();
        ErrorMessageType errorMessageType = new ErrorMessageType(errorMessage, String.valueOf(errorCode));
        ArrayList<ErrorMessageType> errorMessageTypes = newArrayList(errorMessageType);
        responseMessage.setErrors(new ErrorsType(errorMessageTypes));
        return responseMessage;
    }

}
