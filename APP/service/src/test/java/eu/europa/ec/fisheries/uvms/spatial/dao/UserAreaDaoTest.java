/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.dao;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import com.vividsolutions.jts.geom.Point;
import eu.europa.ec.fisheries.uvms.spatial.entity.UserAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.utility.BaseSpatialDaoTest;
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
        assertEquals(1L, entityList.get(0).getId().longValue());
    }


}