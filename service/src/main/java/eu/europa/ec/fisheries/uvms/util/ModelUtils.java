package eu.europa.ec.fisheries.uvms.util;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ResponseMessageType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SuccessType;

public class ModelUtils {

    private ModelUtils(){

    }

    public static ResponseMessageType createSuccessResponseMessage() {
        ResponseMessageType responseMessage = new ResponseMessageType();
        responseMessage.setSuccess(new SuccessType());
        return responseMessage;
    }

    public static boolean containsError(ResponseMessageType responseMessage) {
        return responseMessage.getErrors() != null;
    }

}
