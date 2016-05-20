package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import eu.europa.ec.fisheries.uvms.service.QueryParameter;
import eu.europa.ec.fisheries.uvms.spatial.entity.PortEntity;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import org.geotools.geometry.jts.GeometryBuilder;
import org.junit.Before;
import org.junit.Test;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static junit.framework.TestCase.assertEquals;

public class PortDaoTest extends BaseSpatialDaoTest {

    private PortDao dao = new PortDao(em);

    @Before
    public void prepare(){
        Operation operation = sequenceOf(DELETE_ALL, INSERT_PORT_REFERENCE_DATA);
        DbSetup dbSetup = new DbSetup(new DataSourceDestination(ds), operation);
        dbSetupTracker.launchIfNecessary(dbSetup);
    }

    @Test
    @SneakyThrows
    public void shouldReturnPortOrderedByDistanceAscending(){

        Map parameters = QueryParameter.with("shape", new GeometryBuilder().point(12, -15)).parameters();
        List<PortEntity> entityByNamedQuery =
                dao.findEntityByNamedQuery(PortEntity.class, PortEntity.LIST_ORDERED_BY_DISTANCE, parameters, 10);
        dao.findAllEntity(PortEntity.class);
        assertEquals(3L, entityByNamedQuery.get(0).getId().longValue());
        assertEquals(2L, entityByNamedQuery.get(1).getId().longValue());
        assertEquals(1L, entityByNamedQuery.get(2).getId().longValue());

    }
}
