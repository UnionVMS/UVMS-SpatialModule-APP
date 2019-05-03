package eu.europa.ec.fisheries.uvms.spatial.rest.util;


import javax.ws.rs.ext.Provider;

@Provider
public class ObjectMapperContextResolver {
//    public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {
    //private final ObjectMapper mapper;

    public ObjectMapperContextResolver() {
        //mapper = new ObjectMapper();

        //mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        //mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    //@Override
    //public ObjectMapper getContext(Class<?> type) {
    //    return null;
    //}
}
