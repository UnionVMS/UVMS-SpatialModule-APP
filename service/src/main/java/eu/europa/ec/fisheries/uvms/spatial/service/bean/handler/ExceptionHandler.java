package eu.europa.ec.fisheries.uvms.spatial.service.bean.handler;

import eu.europa.ec.fisheries.uvms.util.exception.ExceptionMapper;
import eu.europa.ec.fisheries.uvms.util.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.util.exception.SpatialServiceException;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/**
 * Created by kopyczmi on 18-Aug-15.
 */
public class ExceptionHandler {

    protected final Logger LOG = LoggerFactory.getLogger(ExceptionHandler.class);

    @EJB
    ExceptionMapper exceptionMapper;

    @AroundInvoke
    public Object log(InvocationContext ctx) throws Exception
    {
        System.out.println("*** TracingInterceptor intercepting " + ctx.getMethod().getName());
        long start = System.currentTimeMillis();
        String param = (String)ctx.getParameters()[0];

        if (param == null)
            ctx.setParameters(new String[]{"default"});

        try
        {
            return ctx.proceed();
        } catch (Exception ex) {
            if (ex instanceof HibernateException) {
                SpatialServiceErrors error = exceptionMapper.convertToSpatialError(ex.getClass());
                return createErrorResponse(error.formatMessage(), error.getErrorCode());
            } else if (ex instanceof SpatialServiceException) {
                logError(ex);
                SpatialServiceException sse = (SpatialServiceException) ex;
                return createErrorResponse(sse.getMessage(), sse.getErrorCode());
            } else {
                logError(ex);
                SpatialServiceErrors error = SpatialServiceErrors.INTERNAL_APPLICATION_ERROR;
                return createErrorResponse(error.formatMessage(), error.getErrorCode());
            }
        }
        finally
        {
            long time = System.currentTimeMillis() - start;
            String method = ctx.getClass().getName();
            System.out.println("*** TracingInterceptor invocation of " + method + " took " + time + "ms");
        }
    }

    private Object createErrorResponse(String errorMessage, Integer errorCode) {
        return null;
    }

    protected void logError(Exception ex) {
        LOG.error("Exception: ", ex);
        LOG.error("Exception cause: ", ex.getCause());
    }
}
