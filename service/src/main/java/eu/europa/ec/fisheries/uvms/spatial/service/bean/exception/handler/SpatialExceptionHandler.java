package eu.europa.ec.fisheries.uvms.spatial.service.bean.exception.handler;

import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;
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
