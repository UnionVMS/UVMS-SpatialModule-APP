/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service.dao;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.layer.ServiceLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.ServiceLayerEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.utility.BaseSpatialDaoTest;
import java.util.Arrays;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ServiceLayerDaoTest extends BaseSpatialDaoTest {

    private ServiceLayerDao dao = new ServiceLayerDao(em);

    @Before
    public void prepare(){
        Operation operation = sequenceOf(DELETE_ALL, INSERT_REFERENCE_DATA);
        DbSetup dbSetup = new DbSetup(new DataSourceDestination(ds), operation);
        dbSetupTracker.launchIfNecessary(dbSetup);
    }

    @Test
    @SneakyThrows
    public void shouldReturnPortServiceLayer(){

        ServiceLayerEntity layerEntity = dao.getBy(AreaType.PORT);

        assertEquals("Ports", layerEntity.getName());
        assertTrue(layerEntity.getIsInternal());
        assertEquals(4, layerEntity.getId(), 0.0);

    }

    @Test
    @SneakyThrows
    public void shouldReturnEezServiceLayer(){

        ServiceLayerEntity layerEntity = dao.getBy(AreaType.EEZ);

        assertEquals("EEZ", layerEntity.getName());
        assertTrue(layerEntity.getIsInternal());
        assertEquals(1, layerEntity.getId(), 0.0);

    }

    @Test
    @SneakyThrows
    public void shouldReturnNullWhenCountry(){

        ServiceLayerEntity layerEntity = dao.getBy(AreaType.COUNTRY);

        assertNull(layerEntity);
    }

    @Test
    @SneakyThrows
    public void shouldReturnNullWhenUserArea(){

        ServiceLayerEntity layerEntity = dao.getBy(AreaType.USERAREA);

        assertNull(layerEntity);
    }

    @Test
    public void shouldReturnListOfServiceLayers(){

        List<ServiceLayerEntity> list = dao.findServiceLayerEntityByIds(Arrays.asList(1L, 2L, 3L));

        assertEquals(3, list.size());
        assertEquals("EEZ", list.get(0).getName());
        assertEquals("RFMO", list.get(1).getName());
        assertEquals("Countries", list.get(2).getName());

    }

    @Test
    public void shouldReturnServiceLayerList(){

        List<ServiceLayerDto> list = dao.findServiceLayerBySubType(Arrays.asList("SYSAREA"), true);

        assertEquals(6, list.size());
        assertEquals("EEZ", list.get(0).getName());
        assertEquals("RFMO", list.get(1).getName());
        assertEquals("Countries", list.get(2).getName());
        assertEquals("Ports", list.get(3).getName());
        assertEquals("UserAreas", list.get(4).getName());
        assertEquals("PortAreas", list.get(5).getName());

    }

    @Test
    public void shouldReturnBackgroundWithBing(){

        List<ServiceLayerDto> list = dao.findServiceLayerBySubType(Arrays.asList("background"), true);

        assertEquals(1, list.size());
        assertEquals("bingRoad", list.get(0).getName());

    }

    @Test
    public void shouldNotReturnBackgroundWithBing(){

        List<ServiceLayerDto> list = dao.findServiceLayerBySubType(Arrays.asList("background"), false);

        assertEquals(0, list.size());
    }

}