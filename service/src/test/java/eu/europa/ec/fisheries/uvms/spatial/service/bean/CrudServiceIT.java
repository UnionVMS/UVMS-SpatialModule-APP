package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.service.CrudService;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.util.QueryNameConstants;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import java.io.File;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.fest.assertions.Assertions.assertThat;

@RunWith(Arquillian.class)
@Transactional
public class CrudServiceIT {

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
        WebArchive webArchive = ShrinkWrap.create(WebArchive.class).addPackages(true, "eu.europa")
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsResource("META-INF/orm.xml")
                .addAsResource("config.properties")
                .addAsResource("META-INF/jboss-deployment-structure.xml")
                .addAsResource("nativeSql.properties")
                .addAsResource("logback.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");

        for (String libName : libs) {
            File[] files = Maven.resolver().resolve(libName).withTransitivity().as(File.class);
            webArchive.addAsLibraries(files);
        }

        return webArchive;
    }

    private static final String EEZ = "eez";
    private static final String REMARKS = "remarks";
    private static final String AUSTRALIA = "Australia";

    @EJB
    private CrudService genericDao;

    @Before
    public void beforeEach() {
        assertNotNull("genericDao not injected", genericDao);
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    @SuppressWarnings("unchecked")
    public void testCreateEntity() throws Exception {
        EezEntity eez = createEezEntity();
        EezEntity createdEez = (EezEntity) genericDao.createEntity(eez);
        assertNotNull(createdEez);
        assertEquals(createdEez.getRemarks(), REMARKS);
        assertEquals(createdEez.getEez(), EEZ);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testFindEntityById() throws Exception {
        EezEntity eezEntity = (EezEntity) genericDao.findEntityById(EezEntity.class, 1);
        assertEquals(eezEntity.getSovereign(), AUSTRALIA);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testFindEntityByNamedQuery() throws Exception {
        List<AreaLocationTypesEntity> areas = genericDao.findEntityByNamedQuery(AreaLocationTypesEntity.class, QueryNameConstants.FIND_ALL_AREAS);
        assertThat(areas).isNotEmpty();
    }

    private EezEntity createEezEntity() {
        EezEntity eez = new EezEntity();
        eez.setRemarks(REMARKS);
        eez.setEez(EEZ);
        return eez;
    }
}