package eu.europa.ec.fisheries.uvms.spatial.rest.resources;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ErrorMessageType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ResponseMessageType;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseDto;

/**
 * Created by kopyczmi on 14-Aug-15.
 */
public abstract class AbstractResource {

    protected ResponseDto createErrorResponse(ResponseMessageType responseMessage) {
        ErrorMessageType error = responseMessage.getErrors().getErrorMessage().iterator().next();
        return new ResponseDto(error.getValue(), ResponseCode.map(error.getErrorCode()));
    }

    protected boolean isSuccess(ResponseMessageType responseMessage) {
        return responseMessage.getSuccess() != null;
    }
}
