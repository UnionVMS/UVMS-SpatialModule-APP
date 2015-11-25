package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import eu.europa.ec.fisheries.uvms.spatial.entity.ProjectionEntity;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.junit.Assert.assertEquals;

public class ProjectionDaoTest extends BaseSpatialDaoTest {

    private ProjectionDao dao = new ProjectionDao(em);

    @Before
    public void prepare(){

        Operation operation =
                sequenceOf(
                        DELETE_ALL,
                        INSERT_REFERENCE_DATA);

        DbSetup dbSetup = new DbSetup(new DataSourceDestination(ds), operation);
        dbSetupTracker.launchIfNecessary(dbSetup);
    }

    @Test
    @SneakyThrows
    public void shouldReturnReferenceData(){

        dbSetupTracker.skipNextLaunch();

        List<ProjectionEntity> list = dao.findAllEntity(ProjectionEntity.class);

        assertEquals(2, list.size());

    }
}