package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import eu.europa.ec.fisheries.uvms.spatial.dao.util.PostGres;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import junit.framework.Assert;
import org.geotools.geometry.jts.GeometryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static junit.framework.Assert.*;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class AreasDaoTest extends BaseSpatialDaoTest {

    private AreasDao dao = new AreasDao(em);

    @Before
    public void prepare(){

        Operation operation = sequenceOf(DELETE_ALL,
                INSERT_EEZ_REFERENCE_DATA,
                INSERT_RFMO_REFERENCE_DATA ,
                INSERT_PORT_AREA_REFERENCE_DATA,
                INSERT_COUNTRY_REFERENCE_DATA);
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
    public void testGetNameAndCode(){
        dbSetupTracker.skipNextLaunch();
        List<AreaTypeEntry> areaTypeEntries = new ArrayList<>();
        AreaTypeEntry eez = new AreaTypeEntry();
        eez.setId("1");
        eez.setAreaType(AreaType.EEZ);
        areaTypeEntries.add(eez);
        AreaTypeEntry country = new AreaTypeEntry();
        country.setId("1");
        country.setAreaType(AreaType.COUNTRY);
        areaTypeEntries.add(country);

        List<AreaLocationTypesEntity> locationTypesEntities = new ArrayList<>();

        AreaLocationTypesEntity entity = new AreaLocationTypesEntity();
        entity.setTypeName("EEZ");
        entity.setAreaDbTable("eez");
        locationTypesEntities.add(entity);

        AreaLocationTypesEntity entity1 = new AreaLocationTypesEntity();
        entity1.setTypeName("COUNTRY");
        entity1.setAreaDbTable("countries");
        locationTypesEntities.add(entity1);

        List list = dao.getNameAndCode(locationTypesEntities, areaTypeEntries);
        assertEquals(2, list.size());
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

        assertEquals(8, list.size());
        // TODO continue test what is inside the collection

    }

    @Test
    public void testIntersectingArea(){
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

        List list = dao.intersectingArea(entities, new PostGres(), new GeometryBuilder().point(-8, 40));
        assertEquals(1, list.size());

        // TODO continue test what is inside the collection


    }

}
