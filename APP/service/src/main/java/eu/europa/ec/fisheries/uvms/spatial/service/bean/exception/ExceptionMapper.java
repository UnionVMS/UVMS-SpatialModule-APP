package eu.europa.ec.fisheries.uvms.spatial.service.bean.exception;

import com.google.common.collect.ImmutableMap;
import org.hibernate.HibernateException;

import javax.ejb.Stateless;

@Stateless // FIXME that is a small bean
public class ExceptionMapper {
    private ImmutableMap<Class, SpatialServiceErrors> exceptionMapper = ImmutableMap.<Class, SpatialServiceErrors>builder()
            .put(HibernateException.class, SpatialServiceErrors.INTERNAL_APPLICATION_ERROR)
            .build();

    public SpatialServiceErrors convertToSpatialError(Class aClass) {
        return exceptionMapper.get(aClass);
    }
}
