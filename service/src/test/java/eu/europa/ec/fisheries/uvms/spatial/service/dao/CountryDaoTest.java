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
import eu.europa.ec.fisheries.uvms.spatial.service.entity.CountryEntity;
import eu.europa.ec.fisheries.uvms.spatial.utility.BaseSpatialDaoTest;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.junit.Assert.assertEquals;

public class CountryDaoTest extends BaseSpatialDaoTest {

    /*

    private CountryDao dao = new CountryDao(em);

    @Before
    public void prepare(){
        Operation operation = sequenceOf(DELETE_ALL, INSERT_COUNTRY_REFERENCE_DATA);
        DbSetup dbSetup = new DbSetup(new DataSourceDestination(ds), operation);
        dbSetupTracker.launchIfNecessary(dbSetup);
    }

    @Test
    @SneakyThrows
    public void testFindAllCountriesDesc() {
        dbSetupTracker.skipNextLaunch();

        List<CountryEntity> entityByNamedQuery = dao.findEntitiesByNamedQuery(CountryEntity.FIND_ALL);

        assertEquals("Portugal", entityByNamedQuery.get(0).getName());
        assertEquals("PRT", entityByNamedQuery.get(0).getCode());

        assertEquals("Spain", entityByNamedQuery.get(1).getName());
        assertEquals("ESP", entityByNamedQuery.get(1).getCode());

    }

    */
}