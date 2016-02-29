package eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured.config;

import eu.europa.ec.fisheries.uvms.spatial.rest.constants.RestConstants;
import eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath(RestConstants.REST_URL)
public class RestActivator extends Application {

    final static Logger LOG = LoggerFactory.getLogger(RestActivator.class);

    private final Set<Object> singletons = new HashSet<>();
    private final Set<Class<?>> set = new HashSet<>();

    public RestActivator() {
        set.add(AreaResource.class);
        set.add(LocationResource.class);
        set.add(EezResource.class);
        set.add(EnrichmentResource.class);
        set.add(ConfigResource.class);
        set.add(UserAreaResource.class);
        set.add(AreaPortResource.class);
        set.add(CountryResource.class);
        set.add(MapConfigResource.class);
        set.add(FileUploadResource.class);
        set.add(BookmarkResource.class);
        set.add(ImageResource.class);
        LOG.info(RestConstants.MODULE_NAME + " module starting up");
    }

    @Override
    public Set<Class<?>> getClasses() {
        return set;
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }

}
