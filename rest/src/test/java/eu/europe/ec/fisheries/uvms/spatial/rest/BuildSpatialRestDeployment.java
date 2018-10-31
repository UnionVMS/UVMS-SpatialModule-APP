package eu.europe.ec.fisheries.uvms.spatial.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import eu.europa.ec.fisheries.uvms.commons.domain.BaseEntity;
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
import java.util.Arrays;

@ArquillianSuiteDeployment
public abstract class BuildSpatialRestDeployment {


    @Deployment(name = "movement", order = 1)
    public static Archive<?> createDeployment() {

        WebArchive testWar = ShrinkWrap.create(WebArchive.class, "test.war");

        File fi = new File("pom.xml");
        System.out.println(fi.getAbsolutePath());

        File[] files = Maven.resolver().loadPomFromFile("pom.xml").importRuntimeAndTestDependencies().resolve(/*"org.apache.xmlgraphics:batik-codec:1.8", "org.apache.xmlgraphics:batik-transcoder:1.8", "eu.europa.ec.fisheries.uvms.reporting:reporting-model" ,"eu.europa.ec.fisheries.uvms.spatial:message", "eu.europa.ec.fisheries.uvms:usm4uvms"*/)
                .withTransitivity().asFile();
        Arrays.asList(files).stream().forEach(f -> System.out.println(f.getName()));
        testWar.addAsLibraries(files);


        testWar.addAsLibraries(Maven.configureResolver().loadPomFromFile("pom.xml")
                .resolve("eu.europa.ec.fisheries.uvms.commons:uvms-commons-service:3.0.22", "eu.europa.ec.fisheries.uvms.spatial:service", "org.apache.xmlgraphics:batik-codec:1.8", "org.apache.xmlgraphics:batik-transcoder:1.8", "eu.europa.ec.fisheries.uvms.reporting:reporting-model", "eu.europa.ec.fisheries.uvms:usm4uvms:4.0.12")
                .withTransitivity().asFile());

        //testWar.deletePackages(true, "eu.europa.ec.fisheries.uvms.spatial.service");
        //testWar.addAsLibraries("org.apache.xmlgraphics:batik-codec", "org.apache.xmlgraphics:batik-transcoder");

        testWar.addPackages(true, "eu.europa.ec.fisheries.uvms.spatial.rest");
        //testWar.addPackages(true, "eu.europa.ec.fisheries.uvms.spatial.service.entity");
        testWar.addClass(BaseEntity.class);
        //testWar.addClass(UnionVMSResource.class);
        //testWar.deleteClass(JwtTokenHandler.class);

        testWar.delete("/WEB-INF/web.xml");
        testWar.addAsWebInfResource("mock-web.xml", "web.xml");

        for (ArchivePath a: testWar.getContent().keySet()) {
            System.out.println(a.get());
        }
        System.out.println(testWar.getContent().keySet().size());

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
