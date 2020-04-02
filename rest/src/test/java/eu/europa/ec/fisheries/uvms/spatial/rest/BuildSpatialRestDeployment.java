package eu.europa.ec.fisheries.uvms.spatial.rest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;

import eu.europa.ec.fisheries.uvms.commons.rest.filter.MDCFilter;
import org.eu.ingwar.tools.arquillian.extension.suite.annotations.ArquillianSuiteDeployment;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

@ArquillianSuiteDeployment
public abstract class BuildSpatialRestDeployment {

    private static final String PROPERTIES_FILE = "test.properties";
    private static final String DEFAULT_WF_HOST = "localhost";
    private static final String DEFAULT_WF_PORT = "8080";

    @Deployment(name = "spatial", order = 1)
    public static Archive<?> createDeployment() {

        WebArchive testWar = ShrinkWrap.create(WebArchive.class, "test.war");

        File[] files = Maven.configureResolver().workOffline().loadPomFromFile("pom.xml")
                .resolve("eu.europa.ec.fisheries.uvms.spatial:service",
                        "eu.europa.ec.fisheries.uvms.movement:movement-model")
                .withTransitivity().asFile();
        testWar.addAsLibraries(files);
        
        testWar.addPackages(true, "eu.europa.ec.fisheries.uvms.spatial.rest");

        testWar.addClass(ConfigServiceMock.class);
        testWar.addClass(MDCFilter.class);

        testWar.delete("/WEB-INF/web.xml");
        testWar.addAsWebInfResource("mock-web.xml", "web.xml");

        return testWar;
    }

    private String makebaseUri() {
        Properties properties = new Properties();
        InputStream is = getClass().getResourceAsStream("/"+PROPERTIES_FILE);
        if (is != null) {
            try {
                properties.load(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Class-path resource: '{}' does not exist. Using default values" + PROPERTIES_FILE);
        }

        return "http://" + properties.getProperty("wildfly_host", DEFAULT_WF_HOST) + ":" + properties.getProperty("wildfly_port", DEFAULT_WF_PORT);
    }

    protected WebTarget getWebTarget() {
        ObjectMapper objectMapper = new ObjectMapper();
        Client client = ClientBuilder.newClient();
        client.register(new JacksonJaxbJsonProvider(objectMapper, JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS));
        return client.target(UriBuilder.fromUri(makebaseUri()).path("test").path("spatialnonsecure").build());
    }
}
