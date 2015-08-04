package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

import java.io.File;

/**
 * Created by kopyczmi on 04-Aug-15.
 */
public class AbstractArquillianTest {

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive javaArchive = ShrinkWrap.create(WebArchive.class).addPackages(true, "eu.europa")
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("config.properties")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");

        File[] libs = Maven.resolver().resolve("org.jvnet.jaxb2_commons:jaxb2-basics-runtime:0.9.4").withTransitivity().as(File.class);
        javaArchive = javaArchive.addAsLibraries(libs);

        return javaArchive;
    }

}
