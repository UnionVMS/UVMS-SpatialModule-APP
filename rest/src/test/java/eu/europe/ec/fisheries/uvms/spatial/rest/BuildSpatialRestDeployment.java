package eu.europe.ec.fisheries.uvms.spatial.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import eu.europa.ec.fisheries.uvms.commons.rest.resource.UnionVMSResource;
import org.eu.ingwar.tools.arquillian.extension.suite.annotations.ArquillianSuiteDeployment;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.io.File;

@ArquillianSuiteDeployment
public class BuildSpatialRestDeployment {


    @Deployment(name = "movement", order = 2)
    public static Archive<?> createDeployment() {

        WebArchive testWar = ShrinkWrap.create(WebArchive.class, "test.war");

        File[] files = Maven.resolver().loadPomFromFile("pom.xml").importRuntimeAndTestDependencies().resolve(/*"eu.europa.ec.fisheries.uvms.spatial:service"*/)
                .withTransitivity().asFile();
        testWar.addAsLibraries(files);


        testWar.addAsLibraries(Maven.configureResolver().loadPomFromFile("pom.xml")
                .resolve("eu.europa.ec.fisheries.uvms.spatial:service")
                .withTransitivity().asFile());

        //testWar.addAsLibraries("org.apache.xmlgraphics:batik-codec", "org.apache.xmlgraphics:batik-transcoder");

        testWar.addPackages(true, "eu.europa.ec.fisheries.uvms.spatial");
        testWar.addPackages(true, "eu.europa.ec.fisheries.uvms.commons");
        //testWar.addClass(UnionVMSResource.class);


        testWar.delete("/WEB-INF/web.xml");
        testWar.addAsWebInfResource("mock-web.xml", "web.xml");

        for (ArchivePath a: testWar.getContent().keySet()) {
            System.out.println(a.get());
        }

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
