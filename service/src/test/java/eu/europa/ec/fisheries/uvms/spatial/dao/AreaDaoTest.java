package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import eu.europa.ec.fisheries.uvms.spatial.dao.util.PostGres;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import org.geotools.geometry.jts.GeometryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class AreaDaoTest extends BaseSpatialDaoTest {

    private AreaDao dao = new AreaDao(em);

    @Before
    public void prepare(){

        Operation operation = sequenceOf(DELETE_ALL, INSERT_EEZ_REFERENCE_DATA, INSERT_RFMO_REFERENCE_DATA ,
                INSERT_PORT_AREA_REFERENCE_DATA);
        DbSetup dbSetup = new DbSetup(new DataSourceDestination(ds), operation);
        dbSetupTracker.launchIfNecessary(dbSetup);
    }

    @Test
    public void testClosestAreaWithIllegalArguments(){
        dbSetupTracker.skipNextLaunch();
        List list = dao.closestArea(null, null, null);
        assertTrue(list.isEmpty());
    }

    @Test
    public void testClosestArea(){
        dbSetupTracker.skipNextLaunch();

        List<AreaLocationTypesEntity> entities = new ArrayList<>();
        AreaLocationTypesEntity eezLocationTypesEntity = new AreaLocationTypesEntity();
        eezLocationTypesEntity.setAreaDbTable("eez");
        eezLocationTypesEntity.setTypeName("EEZ");
        entities.add(eezLocationTypesEntity);

        AreaLocationTypesEntity rfmoLocationTypesEntity = new AreaLocationTypesEntity();
        rfmoLocationTypesEntity.setAreaDbTable("rfmo");
        rfmoLocationTypesEntity.setTypeName("RFMO");
        entities.add(rfmoLocationTypesEntity);

        AreaLocationTypesEntity portAreaLocationTypesEntity = new AreaLocationTypesEntity();
        portAreaLocationTypesEntity.setAreaDbTable("port_area");
        portAreaLocationTypesEntity.setTypeName("PORT_AREA");
        entities.add(portAreaLocationTypesEntity);

        List list = dao.closestArea(entities, new PostGres(), new GeometryBuilder().point(-8, 40));

        assertFalse(list.isEmpty());

    }

}
