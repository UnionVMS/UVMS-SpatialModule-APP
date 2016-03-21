package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;

import java.io.File;

public class AbstractArquillianIT {

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive webArchive = ShrinkWrap.create(WebArchive.class).addPackages(true, "eu.europa.ec.fisheries.uvms.spatial")
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("META-INF/orm.xml")
                .addAsResource("config.properties")
                        //.addAsResource("META-INF/jboss-deployment-structure.xml")
                .addAsResource("app.properties")
                .addAsResource("logback.xml")
                .addAsResource("Config.json")
                .addAsResource("UserConfig.json")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");

        File[] libs = Maven.resolver().loadPomFromFile("pom.xml").importDependencies(ScopeType.COMPILE, ScopeType.RUNTIME, ScopeType.TEST).resolve().withTransitivity().asFile();
        webArchive = webArchive.addAsLibraries(libs);

        System.out.println(webArchive.toString(true));

        return webArchive;
    }

}
