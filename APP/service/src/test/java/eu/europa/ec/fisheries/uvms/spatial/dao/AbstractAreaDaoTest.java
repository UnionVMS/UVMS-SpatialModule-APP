package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import eu.europa.ec.fisheries.uvms.spatial.dao.util.DAOFactory;
import eu.europa.ec.fisheries.uvms.spatial.dao.util.H2gis;
import eu.europa.ec.fisheries.uvms.spatial.dao.util.PostGres;
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.BaseAreaEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.EezEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.PortAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.utility.BaseSpatialDaoTest;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import lombok.SneakyThrows;
import org.geotools.geometry.jts.GeometryBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static junit.framework.TestCase.assertTrue;
import static junitparams.JUnitParamsRunner.$;

@RunWith(JUnitParamsRunner.class)
public class AbstractAreaDaoTest extends BaseSpatialDaoTest {

    @Before
    public void prepare(){

        Operation operation = sequenceOf(DELETE_ALL, INSERT_EEZ_REFERENCE_DATA,
                INSERT_RFMO_REFERENCE_DATA, INSERT_PORT_AREA_REFERENCE_DATA,
                INSERT_COUNTRY_REFERENCE_DATA);
        DbSetup dbSetup = new DbSetup(new DataSourceDestination(ds), operation);
        dbSetupTracker.launchIfNecessary(dbSetup);
    }

    @Test(expected = Exception.class)
    @SneakyThrows
    public void shouldThrowException(){
        dbSetupTracker.skipNextLaunch();
        DAOFactory.getAbstractSpatialDao(em, "NOTYPE");
    }

    @Test
    @SneakyThrows
    public void shouldReturnIntersectedPortArea(){
        AbstractAreaDao dao = DAOFactory.getAbstractSpatialDao(em, "PORTAREA");
        dao.findByIntersect(new GeometryBuilder().point(1, 1));
    }

    @Test
    @SneakyThrows
    public void shouldReturnIntersectedArea(){
        dbSetupTracker.skipNextLaunch();
        AbstractAreaDao dao = DAOFactory.getAbstractSpatialDao(em, "EEZ");
        List<EezEntity> intersects = dao.findByIntersect(new GeometryBuilder().point(103, -12));
        Assert.assertEquals(3L, intersects.get(0).getId().longValue());
    }

    @Test
    @SneakyThrows
    @Parameters(method = "shouldReturnAreaById")
    public void testFindOneArea(String areaType, long gid){
        AbstractAreaDao dao = DAOFactory.getAbstractSpatialDao(em, areaType);
        Serializable entity = dao.findEntityById(PortAreasEntity.class, gid);
        assertTrue(String.valueOf(gid).equals(((BaseAreaEntity) entity).getId().toString()));
    }

    @Test
    @SneakyThrows
    public void testClosestAreaWithIllegalArguments(){
        dbSetupTracker.skipNextLaunch();
        List list = DAOFactory.getAbstractSpatialDao(em, "EEZ").closestArea(null, null, null);
        assertTrue(list.isEmpty());
    }

    @Test
    @SneakyThrows
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

        List list = DAOFactory.getAbstractSpatialDao(em, "EEZ").getNameAndCode(locationTypesEntities, areaTypeEntries);
        Assert.assertEquals(2, list.size());
    }

    @Test
    @SneakyThrows
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

        List list = DAOFactory.getAbstractSpatialDao(em, "EEZ").closestArea(entities, new H2gis(), new GeometryBuilder().point(-8, 40));

        Assert.assertEquals(8, list.size());
        // TODO continue test what is inside the collection

    }

    @Test
    @SneakyThrows
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

        List list = DAOFactory.getAbstractSpatialDao(em, "EEZ").intersectingArea(entities, new PostGres(), new GeometryBuilder().point(-8, 40));
        Assert.assertEquals(1, list.size());

        // TODO continue test what is inside the collection


    }

    protected Object[] shouldReturnAreaById(){

        return $(
            $("EEZ", 1L),
            $("PORTAREA", 1L)
        );
    }

}
