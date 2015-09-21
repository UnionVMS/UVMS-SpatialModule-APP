package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;

import java.io.File;

public class AbstractArquillianIT {

    private static String[] libs = {
            "org.jvnet.jaxb2_commons:jaxb2-basics-runtime:0.9.4",
            "com.google.guava:guava:18.0",
            "org.apache.commons:commons-lang3:3.4",
            "org.mapstruct:mapstruct:1.0.0.CR1",
            "org.mapstruct:mapstruct-processor:1.0.0.CR1",
            "commons-collections:commons-collections:3.2.1",
            "org.geotools:gt-geojson:14-M1",
            "org.easytesting:fest-assert:1.4",
            "org.slf4j:slf4j-api:1.7.12",
            "org.geotools:gt-epsg-hsql:14-M1"
    };

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive webArchive = ShrinkWrap.create(WebArchive.class).addPackages(true, "eu.europa.ec.fisheries.uvms.spatial")
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("META-INF/orm.xml")
                .addAsResource("config.properties")
                //.addAsResource("META-INF/jboss-deployment-structure.xml")
                .addAsResource("nativeSql.properties")
                .addAsResource("logback.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        
        
        File[] libs = Maven.resolver().loadPomFromFile("pom.xml").importDependencies(ScopeType.COMPILE, ScopeType.RUNTIME, ScopeType.TEST).resolve().withTransitivity().asFile();
        webArchive = webArchive.addAsLibraries(libs);


        System.out.println(webArchive.toString(true)); 
        
/*        for (String libName : libs) {
            File[] files = Maven.resolver().resolve(libName).withTransitivity().as(File.class);
            webArchive.addAsLibraries(files);
        }*/

        return webArchive;
    }

}
