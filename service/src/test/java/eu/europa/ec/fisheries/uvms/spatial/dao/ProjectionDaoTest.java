package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import eu.europa.ec.fisheries.uvms.spatial.entity.ProjectionEntity;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

public class ProjectionDaoTest extends BaseSpatialDaoTest {

    private ProjectionDao dao = new ProjectionDao(em);

    @Before
    public void prepare(){

        Operation operation =
                sequenceOf(
                        DELETE_ALL,
                        INSERT_REFERENCE_DATA,
                        insertInto("spatial.projection")
                                .columns("ID", "NAME", "SRS_CODE", "FORMATS", "PROJ_DEF", "UNITS", "WORLD")
                                .values(1L, "testUser", 2, "", "", "", 'N')
                                .build());


        DbSetup dbSetup = new DbSetup(new DataSourceDestination(ds), operation);
        dbSetupTracker.launchIfNecessary(dbSetup);
    }

    @Test
    @SneakyThrows
    public void shouldCreateProjection(){

        ProjectionEntity projectionEntity = ProjectionEntity.builder()
                .formats("").name("").isSystemWide(true).projDef("Transverse_Mercator")
                .units("metre").srsCode(4608)
                .build();
        ProjectionEntity entity = dao.createEntity(projectionEntity);

        assertNotNull(entity.getId());

    }

    @Test
    @SneakyThrows
    public void shouldReturnAListWithOneProjection(){

        dbSetupTracker.skipNextLaunch();

        List<ProjectionEntity> list = dao.findAllEntity(ProjectionEntity.class);

        assertEquals(1, list.size());

        assertEquals(1L, list.get(0).getId());
        assertEquals(2, list.get(0).getSrsCode());
        assertEquals("testUser", list.get(0).getName());

    }
}