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
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.persistence.EntityTransaction;
import java.util.Arrays;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Operation;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.CoordinatesFormat;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ScaleBarUnits;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.ProjectionEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.ReportConnectSpatialEntity;
import eu.europa.ec.fisheries.uvms.spatial.utility.BaseSpatialDaoTest;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

public class ReportConnectSpatialDaoTest extends BaseSpatialDaoTest {

    private ReportConnectSpatialDao dao = new ReportConnectSpatialDao(em);

    private ProjectionDao projectionDao = new ProjectionDao(em);

    @Before
    public void prepare(){
        Operation operation = sequenceOf(DELETE_ALL, INSERT_REFERENCE_DATA);
        DbSetup dbSetup = new DbSetup(new DataSourceDestination(ds), operation);
        dbSetupTracker.launchIfNecessary(dbSetup);
    }

    @Test
    @SneakyThrows
    public void shouldCreateReportConnect(){

        EntityTransaction tx = em.getTransaction();

        ReportConnectSpatialEntity entity = ReportConnectSpatialEntity.builder()
                .appVersion("2.6")
                .displayFormatType(CoordinatesFormat.DDM)
                .scaleBarType(ScaleBarUnits.IMPERIAL)
                .projectionByDisplayProjId(projectionDao.findEntityById(ProjectionEntity.class, 1L))
                .projectionByMapProjId(projectionDao.findEntityById(ProjectionEntity.class, 2L))
                .mapCenter("mandatory")
                .build();

        tx.begin();

        dao.createEntity(entity);

        tx.commit();

        assertEquals(CoordinatesFormat.DDM, entity.getDisplayFormatType());
        assertEquals(ScaleBarUnits.IMPERIAL, entity.getScaleBarType());
        assertNotNull(entity.getId());

    }

    @Test
    @SneakyThrows
    public void shouldDeleteReports(){

        EntityTransaction tx = em.getTransaction();
        assertEquals(2, dao.findAllEntity(ReportConnectSpatialEntity.class).size());
        tx.begin();
        dao.deleteById(Arrays.asList(100L, 200L));
        tx.commit();
        assertEquals(0, dao.findAllEntity(ReportConnectSpatialEntity.class).size());

    }

    @Test
    @SneakyThrows
    public void deleteByIdWithNullShouldNotThrowError() {
        dao.deleteById(null);
    }

    @Test
    @SneakyThrows
    public void findByReportIdWithNonExistentReportShouldReturnNull() {
        assertNull(dao.findByReportId(1221020L));
    }
}