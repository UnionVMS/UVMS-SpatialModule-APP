package eu.europa.ec.fisheries.uvms.spatial.service.bean.exception;

import com.google.common.collect.ImmutableMap;
import eu.europa.ec.fisheries.uvms.service.exception.CommonGenericDAOException;
import org.hibernate.HibernateException;

import javax.ejb.Stateless;

/**
 * Created by kopyczmi on 17-Aug-15.
 */
@Stateless
public class ExceptionMapper {
    private ImmutableMap<Class, SpatialServiceErrors> exceptionMapper = ImmutableMap.<Class, SpatialServiceErrors>builder()
            .put(HibernateException.class, SpatialServiceErrors.INTERNAL_APPLICATION_ERROR)
            .put(CommonGenericDAOException.class, SpatialServiceErrors.INTERNAL_APPLICATION_ERROR)
            .build();

    public SpatialServiceErrors convertToSpatialError(Class aClass) {
        return exceptionMapper.get(aClass);
    }
}
