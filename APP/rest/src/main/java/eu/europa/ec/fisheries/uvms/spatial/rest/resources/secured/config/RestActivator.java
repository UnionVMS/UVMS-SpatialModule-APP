package eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured.config;

import eu.europa.ec.fisheries.uvms.spatial.rest.constants.RestConstants;
import eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured.AreaPortResource;
import eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured.AreaResource;
import eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured.BookmarkResource;
import eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured.CalculateResource;
import eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured.ConfigResource;
import eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured.CountryResource;
import eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured.FileUploadResource;
import eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured.ImageResource;
import eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured.MapConfigResource;
import eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured.ServiceLayerResource;
import eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured.UserAreaResource;
import eu.europa.ec.fisheries.uvms.spatial.rest.resources.secured.XMLResource;
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
        set.add(XMLResource.class);
        set.add(ConfigResource.class);
        set.add(UserAreaResource.class);
        set.add(AreaPortResource.class);
        set.add(CountryResource.class);
        set.add(MapConfigResource.class);
        set.add(FileUploadResource.class);
        set.add(BookmarkResource.class);
        set.add(ImageResource.class);
        set.add(ServiceLayerResource.class);
        set.add(CalculateResource.class);
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
