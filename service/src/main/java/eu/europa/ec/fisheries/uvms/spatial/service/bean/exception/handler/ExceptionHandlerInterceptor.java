package eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.handler;

import com.google.common.collect.ImmutableMap;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.ExceptionMapper;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceErrors;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.SpatialServiceException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;

import javax.ejb.EJB;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.lang.reflect.Field;
import java.util.ArrayList;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by kopyczmi on 18-Aug-15.
 */
@Interceptor
@Slf4j
public class ExceptionHandlerInterceptor {
    private static final String RESPONSE_MESSAGE = "responseMessage";

    // Supported requests map
    private static final ImmutableMap<Class, Class> rqToRsMapping = ImmutableMap.<Class, Class>builder()
            .put(EezSpatialRQ.class, EezSpatialRS.class)
            .put(AreaByLocationSpatialRQ.class, AreaByLocationSpatialRS.class)
            .put(ClosestAreaSpatialRQ.class, ClosestAreaSpatialRS.class)
            .put(ClosestLocationSpatialRQ.class, ClosestLocationSpatialRS.class)
            .put(SpatialEnrichmentRQ.class, SpatialEnrichmentRS.class)
            .put(AreaDetailsSpatialRequest.class, AreaDetailsSpatialResponse.class)
            .build();

    @EJB
    ExceptionMapper exceptionMapper;

    @AroundInvoke
    public Object log(InvocationContext ctx) throws Exception {
        log.debug("*** TracingInterceptor intercepting " + ctx.getMethod().getName());
        long start = System.currentTimeMillis();
        Class responseTypeClass = null;

        try {
            responseTypeClass = retrieveResponseClass(ctx.getParameters());
            return ctx.proceed();
        } catch (Exception ex) {
            Object rsObject = responseTypeClass.getConstructor().newInstance();
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
            String method = ctx.getMethod().getName();
            log.debug("*** TracingInterceptor invocation of " + method + " took " + time + "ms");
        }
    }

    private Class retrieveResponseClass(Object[] parameters) {
        return rqToRsMapping.get(parameters[0]);
    }

    private boolean isDatabaseException(Exception ex) {
        return ex instanceof HibernateException;
    }

    private void setErrorMessage(Object rsObject, String errorMessage, Integer errorCode) {
        // TODO Add interface to generated schema classes to set response message by properties without the use of reflection
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
        log.error("Exception: ", ex);
        log.error("Exception cause: ", ex.getCause());
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
