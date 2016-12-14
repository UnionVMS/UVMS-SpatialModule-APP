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
import eu.europa.ec.fisheries.uvms.service.QueryParameter;
import eu.europa.ec.fisheries.uvms.spatial.entity.PortEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.ServiceLayerEntity;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LocationType;
import eu.europa.ec.fisheries.uvms.spatial.utility.BaseSpatialDaoTest;
import java.util.List;
import java.util.Map;
import junit.framework.Assert;
import lombok.SneakyThrows;
import org.geotools.geometry.jts.GeometryBuilder;
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

}