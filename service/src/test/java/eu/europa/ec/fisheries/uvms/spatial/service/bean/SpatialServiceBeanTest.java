package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.service.SpatialService;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.entity.Eez;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.io.File;

@RunWith(Arquillian.class)
@Ignore
public class SpatialServiceBeanTest {

    @Inject
    SpatialService spatialService;

    @Deployment
    public static JavaArchive createDeployment() {

        JavaArchive jar = ShrinkWrap.create(JavaArchive.class);
        jar.addPackages(true, "eu.europa");
        jar.addAsResource("test-persistence.xml");
        jar.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");

        System.out.println(jar.toString(true));

        return jar;
    }

    @Before
    public void setUp() {
    }

    @Test
    public void shouldReturnEez() {
        // given

        // when
        Eez eez = spatialService.getEezById(120);

        //then
        Assert.assertNotNull(eez);
    }


}