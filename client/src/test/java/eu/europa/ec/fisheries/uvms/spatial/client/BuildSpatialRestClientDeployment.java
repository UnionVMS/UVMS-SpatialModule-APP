package eu.europa.ec.fisheries.uvms.spatial.client;

import org.eu.ingwar.tools.arquillian.extension.suite.annotations.ArquillianSuiteDeployment;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

@ArquillianSuiteDeployment
public abstract class BuildSpatialRestClientDeployment {

    @Deployment(name = "spatial")
    public static Archive<?> createDeployment() {
        WebArchive testWar = ShrinkWrap.create(WebArchive.class, "spatial.war");

        WebArchive fromZipFile = ShrinkWrap.createFromZipFile(WebArchive.class,
                Maven.configureResolver().loadPomFromFile("pom.xml")
                        .resolve("eu.europa.ec.fisheries.uvms.spatialSwe:rest:war:?")
                        .withoutTransitivity().asSingleFile());

        testWar.merge(fromZipFile);

        testWar.addAsLibraries(Maven.configureResolver().loadPomFromFile("pom.xml")
                .resolve("eu.europa.ec.fisheries.uvms.spatialSwe:service")
                .withTransitivity().asFile());

        testWar.addPackages(true, "eu.europa.ec.fisheries.uvms.spatial.client");
        testWar.addAsResource("beans.xml", "META-INF/beans.xml");

        return testWar;
    }
}
