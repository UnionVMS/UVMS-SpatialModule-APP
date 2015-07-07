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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Singleton
@Startup
public class ModuleInitializerBean {

    private static final String PROP_MODULE_NAME = "module.name";
    private static final String PROP_USM_REST_SERVER = "usm.rest.server";
    private static final String PROP_USM_DESCRIPTOR_FORCE_UPDATE = "usm.deplyment.descriptor.force-update";

    private static final String USM_REST_DESCRIPTOR_URI = "/usm-administration/rest/deployments/";
    private static final String CONFIG_USM_DEPLOYMENT_DESCRIPTOR_XML = "config.usmDeploymentDescriptor.xml";
    private static final String PROP_FILE_NAME = "config.properties";
    private static final String TRUE = "true";

    private static final Logger LOG = LoggerFactory.getLogger(ModuleInitializerBean.class);

    @PostConstruct
    protected void startup() throws IOException {
        // do something on application startup
        Properties moduleConfigs = retrieveModuleConfigs();

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(moduleConfigs.getProperty(PROP_USM_REST_SERVER)).path(USM_REST_DESCRIPTOR_URI);

        Response response = target.path(moduleConfigs.getProperty(PROP_MODULE_NAME)).request(MediaType.APPLICATION_XML_TYPE).get();

        try {
            USMDeploymentDescriptor descriptor = createDescriptor();

            // TODO This is just an assumption that USM restful service returns 200
            if (response.getStatus() != HttpServletResponse.SC_OK) {
                LOG.info("USM doesn't recognize the current module. Deploying module deployment descriptor...");
                response = target.request(MediaType.APPLICATION_XML_TYPE).post(Entity.xml(descriptor));
                checkResult(response, "");
            } else {
                LOG.info("Module deployment descriptor has already been deployed at USM.");

                if (TRUE.equalsIgnoreCase(moduleConfigs.getProperty(PROP_USM_DESCRIPTOR_FORCE_UPDATE))) {
                    LOG.info("Updating the existing module deployment descriptor into USM.");
                    response = target.request(MediaType.APPLICATION_XML_TYPE).put(Entity.xml(descriptor));
                    checkResult(response, "re");

                    // create a test which mocks the USM services (Jersey consumer and producer of those deployment descriptors
                    // maybe an integration test
                }
            }
        } catch (JAXBException e) {
            throw new RuntimeException("Unable to unmarshal descriptor", e);
        } finally {
            client.close();
        }

    }

    private Properties retrieveModuleConfigs() throws IOException {
        Properties prop = new Properties();
        InputStream properties = getClass().getClassLoader().getResourceAsStream(PROP_FILE_NAME);
        if (properties != null) {
            prop.load(properties);
            return prop;
        } else {
            throw new FileNotFoundException("Property file '" + PROP_FILE_NAME + "' not found in the classpath");
        }
    }

    private USMDeploymentDescriptor createDescriptor() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(USMDeploymentDescriptor.class);
        Unmarshaller un = context.createUnmarshaller();

        InputStream descriptorXML = getClass().getResourceAsStream(CONFIG_USM_DEPLOYMENT_DESCRIPTOR_XML);
        return (USMDeploymentDescriptor) un.unmarshal(descriptorXML);
    }

    private void checkResult(Response response, String logPrefix) {
        if (response.getStatus() == HttpServletResponse.SC_OK) {
            LOG.info("Application " + logPrefix + "deployment descriptor successfully " + logPrefix + "deployed into USM.");
        } else {
            throw new RuntimeException("Unable to " + logPrefix + "deploy application descriptor into USM. Response code: " + response.getStatus() + ". Response body: " + response.toString());
        }
    }

    @PreDestroy
    private void shutdown() {
        //do something on application shutdown
    }
}