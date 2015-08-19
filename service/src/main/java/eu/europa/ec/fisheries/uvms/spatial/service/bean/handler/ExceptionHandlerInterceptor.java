package eu.europa.ec.fisheries.uvms.spatial.service.bean.handler;

import eu.europa.ec.fisheries.uvms.service.exception.CommonGenericDAOException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ErrorMessageType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ErrorsType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ResponseMessageType;
import eu.europa.ec.fisheries.uvms.util.exception.ExceptionMapper;
import eu.europa.ec.fisheries.uvms.util.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.util.exception.SpatialServiceException;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by kopyczmi on 18-Aug-15.
 */
@Interceptor
public class ExceptionHandlerInterceptor {
    protected final Logger LOG = LoggerFactory.getLogger(ExceptionHandlerInterceptor.class);

    private static final String RESPONSE_MESSAGE = "responseMessage";

    @EJB
    ExceptionMapper exceptionMapper;

    @AroundInvoke
    public Object log(InvocationContext ctx) throws Exception {
        LOG.debug("*** TracingInterceptor intercepting " + ctx.getMethod().getName());
        long start = System.currentTimeMillis();

        try {
            return ctx.proceed();
        } catch (Exception ex) {
            Object rsObject = createErrorResponse(ctx);
            if (isDatabaseException(ex)) {
                SpatialServiceErrors error = exceptionMapper.convertToSpatialError(ex.getClass());
                setErrorMessage(rsObject, error.getDescription(), error.getErrorCode());
            } else if (ex instanceof SpatialServiceException) {
                logError(ex);
                SpatialServiceException sse = (SpatialServiceException) ex;
                setErrorMessage(rsObject, sse.getMessage(), sse.getErrorCode());
            } else {
                logError(ex);
                SpatialServiceErrors error = SpatialServiceErrors.INTERNAL_APPLICATION_ERROR;
                setErrorMessage(rsObject, error.getDescription(), error.getErrorCode());
            }
            return rsObject;
        } finally {
            long time = System.currentTimeMillis() - start;
            String method = ctx.getClass().getName();
            LOG.debug("*** TracingInterceptor invocation of " + method + " took " + time + "ms");
        }
    }

    private Object createErrorResponse(InvocationContext ctx) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        SpatialExceptionHandler annotation = ctx.getMethod().getAnnotation(SpatialExceptionHandler.class);
        Class responseTypeClass = annotation.responseType();
        if (responseTypeClass == null) {
            throw new SpatialServiceException(SpatialServiceErrors.INTERNAL_APPLICATION_ERROR);
        }
        return responseTypeClass.getConstructor().newInstance();
    }

    private boolean isDatabaseException(Exception ex) {
        return ex instanceof HibernateException || ex instanceof CommonGenericDAOException;
    }

    private void setErrorMessage(Object rsObject, String errorMessage, Integer errorCode) {
        set(rsObject, RESPONSE_MESSAGE, createErrorResponseMessage(errorMessage, errorCode));
    }

    private ResponseMessageType createErrorResponseMessage(String errorMessage, Integer errorCode) {
        ResponseMessageType responseMessage = new ResponseMessageType();
        ErrorMessageType errorMessageType = new ErrorMessageType(errorMessage, String.valueOf(errorCode));
        ArrayList<ErrorMessageType> errorMessageTypes = newArrayList(errorMessageType);
        responseMessage.setErrors(new ErrorsType(errorMessageTypes));
        return responseMessage;
    }

    private void logError(Exception ex) {
        LOG.error("Exception: ", ex);
        LOG.error("Exception cause: ", ex.getCause());
    }

    private boolean set(Object object, String fieldName, Object fieldValue) {
        Class<?> clazz = object.getClass();
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(object, fieldValue);
                return true;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        return false;
    }
}
