package eu.europa.ec.fisheries.uvms.spatial.rest.error;

import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;

public class ErrorHandler {

    public static ResponseDto getFault(Exception ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof SpatialServiceException) {
            return new ResponseDto<>(cause.getMessage(), ResponseCode.SERVICE_ERROR);
        }

        return new ResponseDto<>(ex.getMessage(), ResponseCode.UNDEFINED_ERROR);
    }

}
