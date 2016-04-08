package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import junit.framework.Assert;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import javax.persistence.EntityTransaction;
import java.math.BigDecimal;
import java.util.List;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class EezDaoTest extends BaseSpatialDaoTest {

    private EezDao dao = new EezDao(em);

    private EezEntity expected;

    @Before
    @SneakyThrows
    public void prepare(){

        Operation operation =
                sequenceOf(
                        DELETE_ALL,
                        INSERT_REFERENCE_DATA);

        DbSetup dbSetup = new DbSetup(new DataSourceDestination(ds), operation);
        dbSetupTracker.launchIfNecessary(dbSetup);

        expected = EezEntity.builder()
                .country("Christmas Island")
                .sovereign("Australia")
                .sovId(16)
                .code("CXR")
                .eezId(2L)
                .mrgid(BigDecimal.valueOf(8309))
                .areaM2(329438200300D)
                .longitude(105.365343191397)
                .latitude(-11.243213614649)
                .mrgidEez(8309)
                .geom(wktReader.read("MULTIPOLYGON(((" +
                        "106.867924148 -9.16467987999994," +
                        "108.036593601 -12.9679006599999," +
                        "103.079231596 -12.82837266," +
                        "102.56917584 -8.87249927999994," +
                        "106.867924148 -9.16467987999994)))"))
                .enabled(true)
                .build();

        EntityTransaction t = em.getTransaction();
        t.begin();
        em.persist(expected);
        em.flush();
        t.commit();
    }

    @Test
    @SneakyThrows
    public void shouldReturnEez() {
        assertEquals(expected, dao.getEezById(expected.getGid()));
    }

    @Test
    @SneakyThrows
    public void shouldReturnIntersectedEez(){
        List<EezEntity> intersects = dao.intersects(wktReader.read("POINT(103 -12)"));
        assertEquals(expected, intersects.get(0));
    }

    @Test
    @SneakyThrows
    public void shouldReturnAnEmptyGeometry(){
        List<EezEntity> eezEntities = dao.listEmptyGeometries();
        assertEquals(1, eezEntities.size());
        assertEquals(1L, eezEntities.get(0).getGid());
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
