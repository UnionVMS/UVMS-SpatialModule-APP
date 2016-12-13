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
import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.utility.BaseSpatialDaoTest;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AreaLocationTypesDaoTest extends BaseSpatialDaoTest {

    private AreaLocationTypesDao dao = new AreaLocationTypesDao(em);

    @Before
    public void prepare(){
        Operation operation = sequenceOf(DELETE_ALL, INSERT_REFERENCE_DATA);
        DbSetup dbSetup = new DbSetup(new DataSourceDestination(ds), operation);
        dbSetupTracker.launchIfNecessary(dbSetup);
    }

    @Test
    @SneakyThrows
    public void testFindOneByTypeNameShouldReturnNull(){

        AreaLocationTypesEntity oneByTypeName = dao.findOneByTypeName("NULL");
        assertNull(oneByTypeName);

    }


    @Test
    @SneakyThrows
    public void testFindOneByTypeNameShouldReturnEez(){

        AreaLocationTypesEntity oneByTypeName = dao.findOneByTypeName("EEZ");
        assertEquals("eez", oneByTypeName.getAreaDbTable());
        assertEquals("EEZ", oneByTypeName.getTypeName());
        assertEquals(1L, oneByTypeName.getId(), 0);
        assertEquals(false, oneByTypeName.getIsLocation());
        assertEquals(true, oneByTypeName.getIsSystemWide());

    }

    @Test
    @SneakyThrows
    public void findByIsLocationAndIsSystemReturnSystemWideAreas(){

        List<AreaLocationTypesEntity> byIsLocationAndIsSystemWide = dao.findByIsLocationAndIsSystemWide(false, true);
        assertEquals(4, byIsLocationAndIsSystemWide.size());
        assertEquals("EEZ", byIsLocationAndIsSystemWide.get(0).getTypeName());
        assertEquals("RFMO", byIsLocationAndIsSystemWide.get(1).getTypeName());
        assertEquals("USERAREA", byIsLocationAndIsSystemWide.get(2).getTypeName());
        assertEquals("PORTAREA", byIsLocationAndIsSystemWide.get(3).getTypeName());

    }

    @Test
    @SneakyThrows
    public void findByIsLocationAndIsSystemReturnSystemWideLocations(){

        List<AreaLocationTypesEntity> byIsLocationAndIsSystemWide = dao.findByIsLocationAndIsSystemWide(true, true);
        assertEquals(1, byIsLocationAndIsSystemWide.size());
        assertEquals("PORT", byIsLocationAndIsSystemWide.get(0).getTypeName());

    }

    @Test
    @SneakyThrows
    public void findByIsLocationAndIsSystemReturnNonSystemWideLocations(){

        List<AreaLocationTypesEntity> byIsLocationAndIsSystemWide = dao.findByIsLocationAndIsSystemWide(false, false);
        assertEquals(1, byIsLocationAndIsSystemWide.size());
        assertEquals("COUNTRY", byIsLocationAndIsSystemWide.get(0).getTypeName());

    }

}