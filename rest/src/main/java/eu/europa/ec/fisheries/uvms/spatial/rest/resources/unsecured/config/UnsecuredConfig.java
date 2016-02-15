package eu.europa.ec.fisheries.uvms.spatial.rest.resources.unsecured.config;

import eu.europa.ec.fisheries.uvms.spatial.rest.constants.RestConstants;
import eu.europa.ec.fisheries.uvms.spatial.rest.resources.unsecured.LegendResource;
import eu.europa.ec.fisheries.uvms.spatial.rest.resources.unsecured.SegmentResource;
import eu.europa.ec.fisheries.uvms.spatial.rest.resources.unsecured.PositionResource;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath(RestConstants.IMAGE_URL)
public class UnsecuredConfig extends Application  {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        resources.add(SegmentResource.class);
        resources.add(LegendResource.class);
        resources.add(PositionResource.class);
        return resources;
    }
}
