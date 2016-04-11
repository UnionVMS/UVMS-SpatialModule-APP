package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.spatial.entity.UserAreasEntity;
import org.geotools.geometry.jts.GeometryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static junit.framework.TestCase.assertTrue;

public class UserAreaDaoTest extends BaseSpatialDaoTest {

    private UserAreaDao dao = new UserAreaDao(em);

    private final static Operation INSERT = sequenceOf(
            insertInto("spatial.user_areas")
                    .columns("GID", "USER_NAME", "NAME", "TYPE", "AREA_DESC", "GEOM", "ENABLED", "CREATED_ON")
                    .values(1L, "userDaoTest", "MyArea", "EEZ", "a simple description", "MULTIPOLYGON EMPTY", "Y", "2015-10-11 13:02:23.0")
                    .build()
    );

    @Before
    public void prepare(){

        Operation operation = sequenceOf(DELETE_ALL, INSERT);
        DbSetup dbSetup = new DbSetup(new DataSourceDestination(ds), operation);
        dbSetupTracker.launchIfNecessary(dbSetup);
    }

    @Test
     public void testFindByUserNameAndGeometryIfUsernameNotExisting(){

        List<UserAreasEntity> entityList = dao.findByUserNameAndGeometry("user", null);
        assertTrue(entityList.isEmpty());
    }

    @Test
    public void testFindByUserNameAndGeometryIfShapeIntersectsGeometry(){

        Point shape = new GeometryBuilder().point(12L, 23L);
        List<UserAreasEntity> entityList = dao.findByUserNameAndGeometry("user", shape);
        assertTrue(entityList.isEmpty());
    }

}
