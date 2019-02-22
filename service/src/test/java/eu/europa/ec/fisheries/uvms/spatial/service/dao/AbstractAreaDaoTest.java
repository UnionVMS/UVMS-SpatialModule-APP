/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */

package eu.europa.ec.fisheries.uvms.spatial.service.dao;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.util.DAOFactory;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.util.PostGres;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.BaseAreaEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.EezEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.FaoEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.PortAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.utility.BaseSpatialDaoTest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import lombok.SneakyThrows;
import org.geotools.geometry.jts.GeometryBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class AbstractAreaDaoTest extends BaseSpatialDaoTest {

    @Before
    public void prepare(){

        Operation operation = sequenceOf(DELETE_ALL, INSERT_EEZ_REFERENCE_DATA,
                INSERT_RFMO_REFERENCE_DATA, INSERT_PORT_AREA_REFERENCE_DATA,
                INSERT_COUNTRY_REFERENCE_DATA, INSERT_FAO_REFERENCE_DATA);
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
    public void shouldReturnIntersectedFao(){
        AbstractAreaDao fao = DAOFactory.getAbstractSpatialDao(em, "FAO");
        FaoEntity faoOne = (FaoEntity)fao.findOne(FaoEntity.class, 1L);
        assertEquals(faoOne.getDivisionL(), "division_l");
        assertEquals(faoOne.getDivisionN(), "division_n");

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
