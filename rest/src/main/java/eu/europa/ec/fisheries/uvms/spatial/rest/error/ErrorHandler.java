package eu.europa.ec.fisheries.uvms.spatial.rest.error;

import eu.europa.ec.fisheries.uvms.service.exception.CommonGenericDAOException;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.spatial.service.queue.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.queue.exception.SpatialServiceException;

public class ErrorHandler {

    public static ResponseDto getFault(Exception ex) {
        if (ex instanceof CommonGenericDAOException || ex instanceof SpatialServiceException) {
            return new ResponseDto<>(ex.getMessage(), ResponseCode.SERVICE_ERROR);
        }

        return new ResponseDto<>(ex.getMessage(), ResponseCode.UNDEFINED_ERROR);
    }

}
