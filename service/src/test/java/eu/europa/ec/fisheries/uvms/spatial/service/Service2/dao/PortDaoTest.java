/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.Service2.dao;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import eu.europa.ec.fisheries.uvms.commons.service.dao.QueryParameter;
import eu.europa.ec.fisheries.uvms.spatial.service.Service2.entity.PortEntity;
import eu.europa.ec.fisheries.uvms.spatial.utility.BaseSpatialDaoTest;
import lombok.SneakyThrows;
import org.geotools.geometry.jts.GeometryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static junit.framework.TestCase.assertEquals;

public class PortDaoTest extends BaseSpatialDaoTest {

    private PortDao dao = new PortDao(em);

    @Before
    public void prepare(){
        Operation operation = sequenceOf(DELETE_ALL, INSERT_PORT_REFERENCE_DATA);
        DbSetup dbSetup = new DbSetup(new DataSourceDestination(ds), operation);
        dbSetupTracker.launchIfNecessary(dbSetup);
    }

    @Test
    @SneakyThrows
    public void shouldReturnPortOrderedByDistanceAscending(){

        Map parameters = QueryParameter.with("shape", new GeometryBuilder().point(12, -15)).parameters();
        List<PortEntity> entityByNamedQuery =
                dao.findEntityByNamedQuery(PortEntity.class, PortEntity.LIST_ORDERED_BY_DISTANCE, parameters, 10);
        dao.findAllEntity(PortEntity.class);
        assertEquals(3L, entityByNamedQuery.get(0).getId().longValue());
        assertEquals(2L, entityByNamedQuery.get(1).getId().longValue());
        assertEquals(1L, entityByNamedQuery.get(2).getId().longValue());

    }
}