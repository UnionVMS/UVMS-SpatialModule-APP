package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.dao.CrudDao;
import eu.europa.ec.fisheries.uvms.spatial.entity.CountryEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.util.platform.Java;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import java.io.File;

import static junit.framework.TestCase.assertNotNull;

@RunWith(Arquillian.class)
public class CrudDaoImplIT {

    @Deployment
    public static JavaArchive createDeployment() {
       return ShrinkWrap.create(JavaArchive.class).addPackages(true, "eu.europa")
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("config.properties")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @EJB
    CrudDao crudDao;

    @Before
    public void beforeTest() {
        assertNotNull("CrudService not injected", crudDao);
    }

    @Test
    public void shouldFindCountry() {
        CountryEntity country = (CountryEntity) crudDao.find(CountryEntity.class, 1);
        assertNotNull(country);
    }

    @Test
    public void shouldFindExclusiveEconomicZone() {
        EezEntity eez = (EezEntity) crudDao.find(EezEntity.class, 1);
        assertNotNull(eez);
        assertNotNull(eez.getGeom());
    }
}