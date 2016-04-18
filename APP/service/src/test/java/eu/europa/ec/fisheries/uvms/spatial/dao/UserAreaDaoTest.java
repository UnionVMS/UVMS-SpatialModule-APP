package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.spatial.entity.UserAreasEntity;
import lombok.SneakyThrows;
import org.geotools.geometry.jts.GeometryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class UserAreaDaoTest extends BaseSpatialDaoTest {

    private UserAreaDao dao = new UserAreaDao(em);

    @Before
    public void prepare(){
        Operation operation = sequenceOf(DELETE_ALL, INSERT_USER_AREA_REFERENCE_DATA);
        DbSetup dbSetup = new DbSetup(new DataSourceDestination(ds), operation);
        dbSetupTracker.launchIfNecessary(dbSetup);
    }

    @Test
    @SneakyThrows
     public void testFindByUserNameAndGeometryIfUsernameNotExisting(){
        List<UserAreasEntity> entityList = dao.findByUserNameAndGeometry("user", null);
        assertTrue(entityList.isEmpty());
    }

    @Test
    @SneakyThrows
    public void testFindByUserNameAndGeometryIfShapeIntersectsGeometry(){
        Point shape = new GeometryBuilder().point(-108L, 37L);
        List<UserAreasEntity> entityList = dao.findByUserNameAndGeometry("userDaoTest", shape);
        assertFalse(entityList.isEmpty());
    }

    @Test
    @SneakyThrows
    public void testFindByUserNameAndSCopeName(){
        List<UserAreasEntity> entityList = dao.findByUserNameAndScopeName("userDaoTest", "EC");
        assertEquals(1L, entityList.get(0).getGid().longValue());
    }


}
