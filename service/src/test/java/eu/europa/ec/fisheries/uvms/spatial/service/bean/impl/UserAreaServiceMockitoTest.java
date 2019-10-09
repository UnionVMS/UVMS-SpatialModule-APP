/*
 Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
 and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
 the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.spatial.service.bean.impl;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import eu.europa.ec.fisheries.uvms.spatial.service.dao.util.PostGres;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.geojson.UserAreaGeoJsonDto;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.UserAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.exception.SpatialServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.mapper.UserAreaMapper;
import eu.europa.ec.fisheries.wsdl.user.types.DatasetExtension;
import eu.europa.ec.fisheries.wsdl.user.types.DatasetList;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UserAreaServiceMockitoTest {

    private static String USER_NAME = "testUser";

    @InjectMocks
    private UserAreaServiceBean userAreaService;

    @Mock
    private SpatialUserServiceBean spatialUSMService;

    @Mock
    private UserAreaMapper mapper;

    @Mock
    private SpatialRepositoryBean repo;

    private UserAreaGeoJsonDto incomingDto = new UserAreaGeoJsonDto();
    private DatasetList usmDataSetList = new DatasetList();
    private UserAreasEntity areasEntity;

    @Before
    public void before() throws ParseException {

        areasEntity = new UserAreasEntity();
        areasEntity.setId(1L);
        areasEntity.setDatasetName("name");
        MockitoAnnotations.initMocks(this);
        userAreaService.setDialect(new PostGres());
        incomingDto.setDesc("description");
        incomingDto.setGeometry(new WKTReader().read("MULTIPOLYGON(((1 1,5 1,5 5,1 5,1 1),(2 2, 3 2, 3 3, 2 3,2 2)),((3 3,6 2,6 4,3 3)))"));
        DatasetExtension datasetExtension = new DatasetExtension();
        datasetExtension.setApplicationName("spatial");
        usmDataSetList.getList().add(datasetExtension);
    }

    @Test
    @SneakyThrows
    public void testSaveUserAreaWithNoDataSetNameShouldOnlyPersistInSpatial(){

        incomingDto.setDatasetName(null);

        usmDataSetList.getList().get(0).setName("otherName");

        when(spatialUSMService.listDatasets()).thenReturn(usmDataSetList);
        when(repo.save(any(UserAreasEntity.class))).thenReturn(areasEntity);
        when(mapper.fromDtoToEntity(incomingDto)).thenReturn(areasEntity);

        userAreaService.createUserArea(incomingDto, USER_NAME);

        verify(spatialUSMService, times(0)).listDatasets();
        verify(repo, times(1)).save(any(UserAreasEntity.class));
        verify(spatialUSMService, times(0)).persistDataSetInUSM(null, null);

        verifyNoMoreInteractions(spatialUSMService, repo);

    }

    @Test(expected = SpatialServiceException.class)
    @SneakyThrows
    public void testSaveUserAreaWithAreaNameAlreadyUsedInUSMShouldThrowServiceException(){

        incomingDto.setDatasetName("newDataSetName");
        usmDataSetList.getList().get(0).setName("newDataSetName");

        when(spatialUSMService.listDatasets()).thenReturn(usmDataSetList);
        when(repo.save(any(UserAreasEntity.class))).thenReturn(areasEntity);

        userAreaService.createUserArea(incomingDto, USER_NAME);

    }

    @Test
    @SneakyThrows
    public void testSaveUserAreaWithNewDataSetName(){

        incomingDto.setDatasetName("other");
        usmDataSetList.getList().get(0).setName("otherName");

        when(repo.getUserAreaByUserNameAndName(USER_NAME, "other")).thenReturn(areasEntity);
        when(spatialUSMService.listDatasets()).thenReturn(usmDataSetList);
        when(repo.save(any(UserAreasEntity.class))).thenReturn(areasEntity);
        when(mapper.fromDtoToEntity(incomingDto)).thenReturn(areasEntity);

        userAreaService.createUserArea(incomingDto, USER_NAME);

        verify(spatialUSMService, times(1)).listDatasets();
        verify(repo, times(1)).save(any(UserAreasEntity.class));
        verify(spatialUSMService, times(1)).persistDataSetInUSM("other", "USERAREA_1");

        verifyNoMoreInteractions(spatialUSMService, repo);

    }

    @Test
    @SneakyThrows
    public void testDeleteUserAreaWithDataDifferentThanNullSetNameShouldDeleteOldDataSetNameFromUSM(){

        areasEntity.setDatasetName("previousName");

        when(repo.findUserAreaById(anyLong(), anyString(), anyBoolean(), anyString())).thenReturn(areasEntity);

        userAreaService.deleteUserArea(1L, USER_NAME, true, "public");

        verify(repo, times(1)).findUserAreaById(1L, USER_NAME, true, "public");
        verify(repo, times(1)).deleteUserArea(any(UserAreasEntity.class));
        verify(spatialUSMService, times(1)).deleteDataSetNameFromUSM("previousName", "Spatial", "USERAREA_1");

        verifyNoMoreInteractions(spatialUSMService, repo);

    }

    @Test
    @SneakyThrows
    public void testDeleteUserAreaWithDataSetNullShouldNotDeleteOldDataSetNameFromUSM(){

        areasEntity.setDatasetName(null);

        when(repo.findUserAreaById(anyLong(), anyString(), anyBoolean(), anyString())).thenReturn(areasEntity);

        userAreaService.deleteUserArea(1L, USER_NAME, true, "public");

        verify(repo, times(1)).findUserAreaById(1L, USER_NAME, true, "public");
        verify(repo, times(1)).deleteUserArea(any(UserAreasEntity.class));
        verify(spatialUSMService, times(0)).deleteDataSetNameFromUSM("previousName", "Spatial", "USERAREA_1");

        verifyNoMoreInteractions(spatialUSMService, repo);

    }

    @Test(expected = SpatialServiceException.class)
    @SneakyThrows
    public void testDeleteUserAreaWithUnExistingUserAreaShouldThrowException(){
        when(repo.findUserAreaById(anyLong(), anyString(), anyBoolean(), anyString())).thenReturn(null);
        userAreaService.deleteUserArea(1L, USER_NAME, true, "public");
    }

    @Test
    @SneakyThrows
    public void testUpdateUserAreaWithDataSetNullShouldDeleteOldDataSetNameFromUSM(){

        incomingDto.setDatasetName(null);
        incomingDto.setId(1L);
        areasEntity.setDatasetName("oldDataSetName");

        when(repo.findUserAreaById(anyLong(), anyString(), anyBoolean(), anyString())).thenReturn(areasEntity);

        userAreaService.updateUserArea(incomingDto, USER_NAME, true, "public");

        verify(repo, times(1)).findUserAreaById(1L, USER_NAME, true, "public");
        verify(spatialUSMService, times(1)).deleteDataSetNameFromUSM("oldDataSetName", "Spatial", "USERAREA_1");
        verifyNoMoreInteractions(spatialUSMService, repo);

        UserAreasEntity updatedArea = repo.findUserAreaById(1L, USER_NAME, true, "public");

        assertNull(updatedArea.getDatasetName());
    }

    @Test
    @SneakyThrows
    public void testUpdateUserAreaWithNewDataSetNullShouldDeleteOldDataSetNameFromUSM(){

        incomingDto.setDatasetName("newDataSetName");
        incomingDto.setId(1L);
        areasEntity.setDatasetName("oldDataSetName");

        when(repo.findUserAreaById(anyLong(), anyString(), anyBoolean(), anyString())).thenReturn(areasEntity);

        userAreaService.updateUserArea(incomingDto, USER_NAME, true, "public");

        verify(repo, times(1)).findUserAreaById(1L, USER_NAME, true, "public");
        verify(spatialUSMService, times(1)).deleteDataSetNameFromUSM("oldDataSetName", "Spatial", "USERAREA_1");
        verify(spatialUSMService, times(1)).listDatasets();
        verify(spatialUSMService, times(1)).persistDataSetInUSM("newDataSetName","USERAREA_1");

        verifyNoMoreInteractions(spatialUSMService, repo);

        UserAreasEntity updatedArea = repo.findUserAreaById(1L, USER_NAME, true, "public");

        assertEquals("newDataSetName", updatedArea.getDatasetName());
    }
}
