package eu.europa.ec.fisheries.uvms.spatial.util;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.rest.resource.UnionVMSResource;
import lombok.extern.slf4j.Slf4j;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * Created by padhyad on 6/22/2016.
 */
@Interceptor
@Slf4j
public class RuntimeExceptionInterceptor extends UnionVMSResource {

    @AroundInvoke
    public Object createResponse(final InvocationContext ic) {
        log.info("ExceptionInterceptor received");
        try {
            return ic.proceed();
        } catch (Exception e) {
            if (e.getCause() instanceof ServiceException) {
                throw new RuntimeException(((ServiceException)e.getCause()).getMessage());
            }
            throw new RuntimeException(e);
        }
    }
}
