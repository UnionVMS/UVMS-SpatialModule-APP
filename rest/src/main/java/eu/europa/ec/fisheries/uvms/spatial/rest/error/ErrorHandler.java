package eu.europa.ec.fisheries.uvms.spatial.rest.error;

import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseCode;
import eu.europa.ec.fisheries.uvms.spatial.rest.dto.ResponseDto;
import eu.europa.ec.fisheries.uvms.util.exception.SpatialServiceException;
import eu.europa.ec.fisheries.wsdl.vessel.VesselFaultException;

public class ErrorHandler {

    public static ResponseDto getFault(Exception ex) {
        if(ex instanceof SpatialServiceException) {
            return new ResponseDto<>(ex.getMessage(), ResponseCode.SERVICE_ERROR);
        }

        return new ResponseDto<>(ex.getMessage(), ResponseCode.UNDEFINED_ERROR);
    }

    private static ResponseDto<String> extractFault(VesselFaultException ex) {

        return new ResponseDto<>(ex.getMessage(), ResponseCode.DOMAIN_ERROR);
    }

}
