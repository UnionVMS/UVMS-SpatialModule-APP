package eu.europe.ec.fisheries.uvms.spatial.rest;

import java.io.File;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

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

    @Deployment(name = "spatial", order = 1)
    public static Archive<?> createDeployment() {

        WebArchive testWar = ShrinkWrap.create(WebArchive.class, "test.war");

        File[] files = Maven.configureResolver().loadPomFromFile("pom.xml")
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

    protected WebTarget getWebTarget() {

        ObjectMapper objectMapper = new ObjectMapper();
        Client client = ClientBuilder.newClient();
        client.register(new JacksonJaxbJsonProvider(objectMapper, JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS));
        return client.target("http://localhost:28080/test/spatialnonsecure");
        //return client.target("http://localhost:8080/test/rest");
    }
}
