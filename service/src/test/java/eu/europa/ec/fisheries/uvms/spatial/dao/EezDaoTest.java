package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import lombok.SneakyThrows;
import org.geotools.geometry.jts.GeometryBuilder;
import org.junit.Before;
import org.junit.Test;
import javax.persistence.EntityTransaction;
import java.util.List;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class EezDaoTest extends BaseSpatialDaoTest {

    private EezDao dao = new EezDao(em);

    @Before
    @SneakyThrows
    public void prepare(){
        Operation operation = sequenceOf(DELETE_ALL, INSERT_EEZ_REFERENCE_DATA);
        DbSetup dbSetup = new DbSetup(new DataSourceDestination(ds), operation);
        dbSetupTracker.launchIfNecessary(dbSetup);
    }

    @Test
    @SneakyThrows
    public void shouldReturnEez() {
        dbSetupTracker.skipNextLaunch();

        assertNotNull(dao.getEezById(2L));
    }

    @Test
    @SneakyThrows
    public void shouldReturnIntersectedEez(){
        dbSetupTracker.skipNextLaunch();
        List<EezEntity> intersects = dao.intersects(new GeometryBuilder().point(103, -12));
        assertEquals(3L, intersects.get(0).getGid().longValue());
    }

    @Test
    @SneakyThrows
    public void shouldReturnAnEmptyGeometry(){
        List<EezEntity> eezEntities = dao.listEmptyGeometries();
        assertEquals(1, eezEntities.size());
        assertEquals(1L, eezEntities.get(0).getGid().longValue());
        assertTrue(eezEntities.get(0).getGeom().isEmpty());
    }

    @Test
    @SneakyThrows
    public void shouldDisableAreas(){

        List<EezEntity> allEntity = dao.findAllEntity(EezEntity.class);
        assertFalse(allEntity.isEmpty());

        EntityTransaction t = em.getTransaction();
        t.begin();

        dao.disable();

        em.flush();
        t.commit();

        allEntity = dao.findAllEntity(EezEntity.class);
        assertTrue(allEntity.isEmpty());

    }
}
