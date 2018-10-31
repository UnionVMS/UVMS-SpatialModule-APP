package eu.europa.ec.fisheries.uvms.spatial.rest.util;


import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Provider
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {
    private final ObjectMapper mapper;

    public ObjectMapperContextResolver() {
        mapper = new ObjectMapper();

        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return mapper;
    }
}
