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
public class AbstractArquillianIT {

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive javaArchive = ShrinkWrap.create(WebArchive.class).addPackages(true, "eu.europa")
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("config.properties")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");

        // TODO Please remove that dependencies
        File[] jaxbPluginLibs = Maven.resolver().resolve("org.jvnet.jaxb2_commons:jaxb2-basics-runtime:0.9.4").withTransitivity().as(File.class);
        File[] guavaLibs = Maven.resolver().resolve("com.google.guava:guava:18.0").withTransitivity().as(File.class);
        File[] commonsLibs = Maven.resolver().resolve("org.apache.commons:commons-lang3:3.4").withTransitivity().as(File.class);
        File[] mapstructLibs = Maven.resolver().resolve("org.mapstruct:mapstruct:1.0.0.CR1").withTransitivity().as(File.class);
        File[] mapstructProcessorLibs = Maven.resolver().resolve("org.mapstruct:mapstruct-processor:1.0.0.CR1").withTransitivity().as(File.class);
        javaArchive = javaArchive
                .addAsLibraries(jaxbPluginLibs)
                .addAsLibraries(guavaLibs)
                .addAsLibraries(commonsLibs)
                .addAsLibraries(mapstructLibs)
                .addAsLibraries(mapstructProcessorLibs);

        return javaArchive;
    }

}
