package eu.europa.ec.fisheries.uvms.spatial.rest;

import eu.europa.ec.fisheries.uvms.commons.date.JsonBConfigurator;
import eu.europa.ec.fisheries.uvms.rest.security.InternalRestTokenHandler;
import eu.europa.ec.fisheries.uvms.rest.security.UnionVMSFeature;
import eu.europa.ec.mare.usm.jwt.JwtTokenHandler;
import org.eu.ingwar.tools.arquillian.extension.suite.annotations.ArquillianSuiteDeployment;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

import javax.ejb.EJB;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.io.File;
import java.util.Arrays;

@ArquillianSuiteDeployment
public abstract class BuildSpatialRestDeployment {

    @EJB
    private JwtTokenHandler tokenHandler;

    @EJB
    private InternalRestTokenHandler internalRestTokenHandler;

    private String token;

    @Deployment(name = "spatial", order = 1)
    public static Archive<?> createDeployment() {

        WebArchive testWar = ShrinkWrap.create(WebArchive.class, "test.war");

        File[] files = Maven.configureResolver().loadPomFromFile("pom.xml")
                .importRuntimeAndTestDependencies()
                .resolve()
                .withTransitivity().asFile();
        testWar.addAsLibraries(files);
        
        testWar.addAsLibraries(Maven.configureResolver().loadPomFromFile("pom.xml")
                .resolve("eu.europa.ec.fisheries.uvms.spatialSwe:service")
                .withTransitivity().asFile());
        
        testWar.addPackages(true, "eu.europa.ec.fisheries.uvms.spatial.rest");
        testWar.addPackages(true, "eu.europa.ec.fisheries.uvms.commons.rest");
        
        testWar.addClass(AuthenticationFilterMock.class);
        testWar.addClass(ConfigServiceMock.class);
        
        testWar.delete("/WEB-INF/web.xml");
        testWar.addAsWebInfResource("mock-web.xml", "web.xml");

        return testWar;
    }

    protected WebTarget getWebTarget() {
        Client client = ClientBuilder.newClient();
        client.register(JsonBConfigurator.class);
        return client.target("http://localhost:8080/test/spatialnonsecure");
    }
    
    protected WebTarget getSecuredWebTarget() {
        Client client = ClientBuilder.newClient();
        client.register(JsonBConfigurator.class);
        return client.target("http://localhost:8080/test/rest");
    }

    protected String getToken() {
        if (token == null) {
            token = tokenHandler.createToken("user",
                    Arrays.asList(UnionVMSFeature.manageManualMovements.getFeatureId(),
                            UnionVMSFeature.viewMovements.getFeatureId(),
                            UnionVMSFeature.viewManualMovements.getFeatureId(),
                            UnionVMSFeature.manageAlarmsHoldingTable.getFeatureId(),
                            UnionVMSFeature.viewAlarmsHoldingTable.getFeatureId()));
        }
        return token;
    }
}
