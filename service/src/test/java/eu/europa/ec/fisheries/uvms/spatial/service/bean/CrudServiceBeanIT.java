package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.entity.Country;
import eu.europa.ec.fisheries.uvms.spatial.entity.ExclusiveEconomicZone;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import static junit.framework.TestCase.assertNotNull;

@RunWith(Arquillian.class)
public class CrudServiceBeanIT {

    @EJB
    CrudService crudService;

    @Deployment
    public static JavaArchive createDeployment() {

        return ShrinkWrap.create(JavaArchive.class).addPackages(true, "eu.europa")
        .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
        .addAsResource("config.properties")
        .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Before
    public void beforeTest() {
        assertNotNull("CrudService not injected", crudService);
    }

    @Test
    public void testFindCountry(){
        Country country = (Country) crudService.find(Country.class, 1);
        assertNotNull(country);
    }

    @Test
    public void testExclusiveEconomicZone(){
        ExclusiveEconomicZone eez = (ExclusiveEconomicZone) crudService.find(ExclusiveEconomicZone.class, 1);
        assertNotNull(eez);
    }
}