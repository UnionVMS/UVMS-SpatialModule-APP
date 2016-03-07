package eu.europa.ec.fisheries.uvms.spatial.service.bean.config;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import eu.europa.ec.fisheries.uvms.spatial.dao.BaseSpatialDaoTest;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.SpatialRepositoryBean;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.SysConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import java.util.ArrayList;
import java.util.List;

import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.junit.Assert.*;

/**
 * Created by Georgi on 23-Nov-15.
 */
public class SystemConfigServiceBeanTest extends BaseSpatialDaoTest {

    private SystemConfigServiceBean serviceBean;
    private SpatialRepositoryBean repository;

    @Before
    public void setUp() throws Exception {
        serviceBean = new SystemConfigServiceBean();
        repository = new SpatialRepositoryBean();
        Whitebox.setInternalState(repository, "em", em);
        Whitebox.setInternalState(serviceBean, "repository", repository);

        repository.init();

        Operation operation =
                sequenceOf(
                        DELETE_ALL,
                        INSERT_REFERENCE_DATA,
                        insertInto("spatial.system_configurations")
                                .columns("ID", "NAME", "VALUE")
                                .values(1L, "geo_server_url", "http://localhost:8080/geoserver/wms")
                                .build());

        DbSetup dbSetup = new DbSetup(new DataSourceDestination(ds), operation);
        dbSetupTracker.launchIfNecessary(dbSetup);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetSystemConfigs() throws Exception {
        List<SysConfig> allConfigs = serviceBean.getSystemConfigs();

        assertNotNull(allConfigs);
        assertTrue(allConfigs.size()>0);
        assertEquals("geo_server_url",allConfigs.get(0).getName() );
        assertEquals("http://localhost:8080/geoserver/wms",allConfigs.get(0).getValue() );
    }

    @Test
    public void testUpdateSystemConfigs() throws Exception {
        List<SysConfig> entitiesToUpdate = new ArrayList<>();
        SysConfig entity = new SysConfig("geo_server_url", "http://localhost:8080/geoserver/wms2");
        SysConfig entity2 = new SysConfig("proxy","http://localhost:8080/");
        entitiesToUpdate.add(entity);
        entitiesToUpdate.add(entity2);

        em.getTransaction().begin();

        try {
            serviceBean.updateSystemConfigs(entitiesToUpdate);
            em.getTransaction().commit();
        } catch(Throwable anyThrowable) {
            em.getTransaction().rollback();
        }

        List<SysConfig> allConfigs = serviceBean.getSystemConfigs();

        assertNotNull(allConfigs);
        assertTrue(allConfigs.size()==2);
        assertEquals("geo_server_url",allConfigs.get(0).getName() );
        assertEquals("http://localhost:8080/geoserver/wms2",allConfigs.get(0).getValue() );
        assertEquals("proxy",allConfigs.get(1).getName() );
        assertEquals("http://localhost:8080/",allConfigs.get(1).getValue() );
    }
}