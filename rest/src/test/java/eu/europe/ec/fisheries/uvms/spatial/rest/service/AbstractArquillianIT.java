package eu.europe.ec.fisheries.uvms.spatial.rest.service;

import java.io.File;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;

public class AbstractArquillianIT {
	
    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        WebArchive webArchive = ShrinkWrap.create(WebArchive.class).addPackages(true, "eu.europa.ec.fisheries.uvms.spatial.rest");

        File[] libs = Maven.resolver().loadPomFromFile("pom.xml").importDependencies(ScopeType.COMPILE, ScopeType.RUNTIME, ScopeType.TEST, ScopeType.PROVIDED).resolve().withTransitivity().asFile();
        webArchive = webArchive.addAsLibraries(libs);

        System.out.println(webArchive.toString(true)); 

        return webArchive;
    }
}
