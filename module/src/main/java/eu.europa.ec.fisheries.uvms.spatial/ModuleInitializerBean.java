package eu.europa.ec.fisheries.uvms.spatial;

import eu.europa.ec.fisheries.uvms.spatial.entity.USMDeploymentDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.util.Properties;

@Singleton
@Startup
public class ModuleInitializerBean {


    public final static String PROP_MODULE_NAME = "module.name";
    public final static String PROP_USM_REST_SERVER = "usm.rest.server";
    public final static String PROP_USM_DESCRIPTOR_FORCE_UPDATE = "usm.deplyment.descriptor.force-update";

    public final static String USM_REST_DESCRIPTOR_URI = "/usm-administration/rest/deployments/";

    final static Logger LOG = LoggerFactory.getLogger(ModuleInitializerBean.class);


    @PostConstruct
    private void startup() throws IOException, JAXBException {
        //do something on application startup
        Properties moduleConfigs = new Properties();
        moduleConfigs.load(getClass().getResourceAsStream("config.properties"));

        Client c = ClientBuilder.newClient();
        WebTarget target = c.target(moduleConfigs.getProperty(PROP_USM_REST_SERVER)).path(USM_REST_DESCRIPTOR_URI);

        Response response = target.path(moduleConfigs.getProperty(PROP_MODULE_NAME)).request(MediaType.APPLICATION_XML_TYPE).get();

        if (response.getStatus() != HttpServletResponse.SC_OK) {//TODO This is just an assumption that USM restful service  returns 200
            LOG.info("USM doesn't recognize the current module. Deploying module deployment descriptor...");
            JAXBContext context = JAXBContext.newInstance(USMDeploymentDescriptor.class);
            Unmarshaller un = context.createUnmarshaller();

            USMDeploymentDescriptor descriptor = (USMDeploymentDescriptor) un.unmarshal(getClass().getResourceAsStream("config.usmDeploymentDescriptor.xml"));

            response = target.request(MediaType.APPLICATION_XML_TYPE).post(Entity.xml(descriptor));

            if (response.getStatus() == HttpServletResponse.SC_OK) {
                LOG.info("Application deployment descriptor successfully deployed into USM.");
            } else {
                throw new RuntimeException("Unable to deploy application descriptor into USM. Response code: " + response.getStatus() + ". Response body: " + response.toString());
            }

        } else {
            LOG.info("Module deployment descriptor has already been deployed at USM.");

            if ("true".equalsIgnoreCase(moduleConfigs.getProperty(PROP_USM_DESCRIPTOR_FORCE_UPDATE))) {
                LOG.info("Updating the existing module deployment descriptor into USM.");

                //TODO PUT the descriptor to update it in USM @see https://webgate.ec.europa.eu/CITnet/confluence/display/UNIONVMS/Deploy+Application
                //create a test which mocks the USM services (Jersey consumer and producer of those deployment descriptors
                //maybe an integration test

            }
        }

        c.close();
    }

    @PreDestroy
    private void shutdown() {
        //do something on application shutdown
    }
}