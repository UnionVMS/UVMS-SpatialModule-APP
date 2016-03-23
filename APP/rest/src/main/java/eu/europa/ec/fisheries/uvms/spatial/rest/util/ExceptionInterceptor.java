package eu.europa.ec.fisheries.uvms.spatial.rest.util;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.rest.constants.ErrorCodes;
import eu.europa.ec.fisheries.uvms.rest.resource.UnionVMSResource;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import lombok.extern.slf4j.Slf4j;

@Interceptor
@Slf4j
public class ExceptionInterceptor extends UnionVMSResource {
	
	@AroundInvoke
	public Object createResponse(final InvocationContext ic) {
		log.info("ExceptionInterceptor received");	
		try {
			return ic.proceed();
		} catch (IllegalArgumentException e) {
    		return createErrorResponse(ErrorCodes.INPUT_NOT_SUPPORTED);
    	} catch (Exception e) {
    		if (e.getCause() instanceof SpatialServiceException) {
    			return createErrorResponse(((SpatialServiceException)e.getCause()).getErrorMessageCode());
    		}
			return createErrorResponse(ErrorCodes.INTERNAL_SERVER_ERROR);
		}
	}	
}
