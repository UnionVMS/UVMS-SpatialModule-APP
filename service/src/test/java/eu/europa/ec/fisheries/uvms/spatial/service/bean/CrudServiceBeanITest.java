package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.entity.Country;
import eu.europa.ec.fisheries.uvms.spatial.entity.ExclusiveEconomicZone;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import java.io.File;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(Arquillian.class)
public class CrudServiceBeanITest {

    @EJB
    CrudService crudService;

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

    @Before
    public void beforeTest() {
        assertNotNull("CrudService not injected", crudService);
    }

    @Test
    public void shouldFindCountry() {
        // when
        Country country = (Country) crudService.find(Country.class, 1);

        //then
        assertNotNull(country);
    }

    @Test
    public void shouldFindExclusiveEconomicZone() {
        // when
        ExclusiveEconomicZone eez = (ExclusiveEconomicZone) crudService.find(ExclusiveEconomicZone.class, 1);

        // then
        assertNotNull(eez);
        assertNotNull(eez.getGeometry());
    }
}