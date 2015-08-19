package eu.europa.ec.fisheries.uvms.spatial.service.bean.handler;

import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;
import javax.interceptor.Interceptors;
import java.lang.annotation.*;

/**
 * Created by kopyczmi on 19-Aug-15.
 */
@Inherited
@InterceptorBinding
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SpatialExceptionHandler {
    @Nonbinding Class responseType();
}
