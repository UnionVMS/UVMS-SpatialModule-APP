package eu.europa.ec.fisheries.uvms.spatial.rest.resources.unsecured;

import eu.europa.ec.fisheries.uvms.spatial.rest.util.ObjectMapperContextResolver;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("spatialnonsecure")
public class RestActivatorSpatial extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        resources.add(ObjectMapperContextResolver.class);
        resources.add(SpatialRestResource.class);
        return resources;
    }

}
