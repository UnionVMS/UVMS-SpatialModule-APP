package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import org.eu.ingwar.tools.arquillian.extension.suite.annotations.ArquillianSuiteDeployment;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import eu.europa.ec.fisheries.uvms.spatial.rest.AuthenticationFilterMock;
import java.io.File;

@ArquillianSuiteDeployment
public abstract class BuildServiceDeployment {

        @Deployment(name = "spatial", order = 1)
        public static Archive<?> createDeployment() {

            WebArchive testWar = ShrinkWrap.create(WebArchive.class, "test.war");

            File[] files = Maven.configureResolver().loadPomFromFile("pom.xml")
                    .importRuntimeAndTestDependencies()
                    .resolve()
                    .withTransitivity().asFile();
            testWar.addAsLibraries(files);

            testWar.addPackages(true, "eu.europa.ec.fisheries.uvms.spatial.service");
            testWar.addPackages(true, "eu.europa.ec.fisheries.uvms.spatial.rest");
            testWar.addPackages(true, "eu.europa.ec.fisheries.uvms.commons.rest");

            testWar.addAsResource("persistenceS2.xml", "META-INF/persistence.xml");
            testWar.addAsResource("beans.xml", "META-INF/beans.xml");

            testWar.addClass(AuthenticationFilterMock.class);
            testWar.addClass(ConfigServiceMock.class);

            testWar.delete("/WEB-INF/web.xml");
            testWar.addAsWebInfResource("mock-web.xml", "web.xml");

            return testWar;
        }

}
