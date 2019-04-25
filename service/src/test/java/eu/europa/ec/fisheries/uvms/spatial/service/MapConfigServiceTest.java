/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.spatial.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.util.collections.Sets.newSet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.europa.ec.fisheries.uvms.BaseUnitilsTest;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.CoordinatesFormat;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.LayerSettingsType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ScaleBarUnits;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialDeleteMapConfigurationRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialGetMapConfigurationRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialGetMapConfigurationRS;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.impl.MapConfigServiceBean;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.config.DisplayProjectionDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.config.MapConfigDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.config.MapDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.config.ProjectionDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.config.ServiceLayersDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.usm.ConfigurationDto;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.ProjectionEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.ProviderFormatEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.ReportConnectServiceAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.ReportConnectSpatialEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.entity.ServiceLayerEntity;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
@Ignore
public class MapConfigServiceTest extends BaseUnitilsTest {

    @Mock
    private SpatialRepository repository;

    @InjectMocks
    private MapConfigServiceBean mapConfigServiceBean;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHandleDeleteMapConfigurationWithNull() throws ServiceException {
        mapConfigServiceBean.handleDeleteMapConfiguration(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMapConfigurationWithNull() throws ServiceException {
        mapConfigServiceBean.getMapConfiguration(null);
    }

    @Test
    public void testGetMapConfigurationWithPortLayerShouldNotFilterPort() throws ServiceException {

        SpatialGetMapConfigurationRQ rq = new SpatialGetMapConfigurationRQ();
        rq.setPermittedServiceLayers(Arrays.asList("LAYER1", "LAYER2"));
        rq.setReportId(100L);

        ReportConnectSpatialEntity entity = new ReportConnectSpatialEntity();
        ReportConnectServiceAreasEntity areasEntity = new ReportConnectServiceAreasEntity();
        ServiceLayerEntity layer2 = new ServiceLayerEntity();
        layer2.setId(1L);
        layer2.setName("LAYER2");
        areasEntity.setServiceLayer(layer2);
        areasEntity.setLayerType("port");
        AreaLocationTypesEntity typesEntity = new AreaLocationTypesEntity();
        typesEntity.setTypeName("LAYER2");
        typesEntity.setServiceLayer(layer2);
        layer2.setAreaType(typesEntity);
        areasEntity.setServiceLayer(layer2);
        entity.setReportConnectServiceAreas(newSet(areasEntity));

        when(repository.findServiceLayerEntityByIds(Collections.singletonList(1L))).thenReturn(Collections.singletonList(layer2));
        when(repository.findReportConnectSpatialByReportId(100L)).thenReturn(entity);

        SpatialGetMapConfigurationRS mapConfiguration = mapConfigServiceBean.getMapConfiguration(rq);

        LayerSettingsType layerSettings = mapConfiguration.getMapConfiguration().getLayerSettings();

        assertEquals(1, layerSettings.getPortLayers().size());
        assertEquals("LAYER2", layerSettings.getPortLayers().get(0).getName());

    }

    @Test
    public void testGetMapConfigurationWithPortLayerShouldFilterPort() throws ServiceException {

        SpatialGetMapConfigurationRQ rq = new SpatialGetMapConfigurationRQ();
        rq.setPermittedServiceLayers(Arrays.asList("LAYER1", "LAYER3"));
        rq.setReportId(100L);

        ReportConnectSpatialEntity entity = new ReportConnectSpatialEntity();
        ReportConnectServiceAreasEntity areasEntity = new ReportConnectServiceAreasEntity();
        ServiceLayerEntity layer2 = new ServiceLayerEntity();
        layer2.setId(1L);
        areasEntity.setServiceLayer(layer2);
        areasEntity.setLayerType("port");
        AreaLocationTypesEntity typesEntity = new AreaLocationTypesEntity();
        typesEntity.setTypeName("LAYER2");
        typesEntity.setServiceLayer(layer2);
        layer2.setAreaType(typesEntity);
        areasEntity.setServiceLayer(layer2);
        entity.setReportConnectServiceAreas(newSet(areasEntity));

        when(repository.findServiceLayerEntityByIds(Arrays.asList(1L))).thenReturn(Arrays.asList(layer2));
        when(repository.findReportConnectSpatialByReportId(100L)).thenReturn(entity);

        SpatialGetMapConfigurationRS mapConfiguration = mapConfigServiceBean.getMapConfiguration(rq);

        LayerSettingsType layerSettings = mapConfiguration.getMapConfiguration().getLayerSettings();

        assertEquals(0, layerSettings.getPortLayers().size());

        System.out.println(mapConfiguration);

    }

    @Test
    public void testHandleDeleteMapConfigurationWithIdList() throws ServiceException {

        SpatialDeleteMapConfigurationRQ rq = new SpatialDeleteMapConfigurationRQ();
        rq.setSpatialConnectIds(Arrays.asList(100L, 200L));

        mapConfigServiceBean.handleDeleteMapConfiguration(rq);

        verify(repository, times(1)).deleteReportConnectServiceAreas(Arrays.asList(100L, 200L));

    }

    @Test
    public void testHandleDeleteMapConfigurationWithEmptyIdList() throws ServiceException {

        SpatialDeleteMapConfigurationRQ rq = new SpatialDeleteMapConfigurationRQ();

        mapConfigServiceBean.handleDeleteMapConfiguration(rq);

        verify(repository, times(0)).deleteReportConnectServiceAreas(Mockito.anyList());

    }

    @Test
    public void testCqlActive() {
        String startDate = null;
        String endDate = "2016-08-12 00:00:00";
        StringBuilder cql = new StringBuilder();
        if (startDate != null && endDate != null) {
            cql.append("(").
                    append("(").append("start_date IS NULL").append(" AND ").append("end_date IS NULL").append(")").append(" OR ").
                    append("(").append("NOT ( ").append("start_date > ").append("'").append(endDate).append("'").append(" OR ").append("end_date < ").append("'").append(startDate).append("'").append(")").append(")").append(" OR ").
                    append("(").append("start_date IS NULL").append(" AND ").append("end_date >= ").append("'").append(startDate).append("'").append(")").append(" OR ").
                    append("(").append("end_date IS NULL").append(" AND ").append("start_date <= ").append("'").append(endDate).append("'").append(")").
                    append(")");
        } else if (startDate == null && endDate != null) {
            cql.append("(").append("start_date <= ").append("'").append(endDate).append("'").append(" OR ").append("start_date IS NULL").append(")");
        } else {
            cql.append("");
        }
        assertNotNull(cql);
    }

    @Test
    public void TestDefaultAdminConfig() throws IOException {
        //Given
        //Read JSON from resources
        InputStream is = new FileInputStream("src/test/resources/Config.json");
        String jsonTxt = IOUtils.toString(is);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        ConfigurationDto configDto = mapper.readValue(jsonTxt, ConfigurationDto.class);

        //Test
        assertNotNull(configDto);
    }

    @Test
    public void TestUserConfig() throws IOException {
        //Given
        //Read JSON from resources
        InputStream is = new FileInputStream("src/test/resources/UserConfig.json");
        String jsonTxt = IOUtils.toString(is);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        ConfigurationDto configDto = mapper.readValue(jsonTxt, ConfigurationDto.class);

        //Test
        assertNotNull(configDto);
    }

    @Test
    public void testGetMapConfigWithDefaultConfig() throws IOException, ServiceException {
        //mock
        mockGenMapProjectionWithDefaultConfig();

        //Given
        MapConfigDto mapConfigDto = mapConfigServiceBean.getReportConfig(1, getConfig("src/test/resources/UserConfig.json"), getConfig("src/test/resources/Config.json"), "rep_power", "EC", new Date().toString(), null);
        MapDto mapDto = mapConfigDto.getMap();
        ServiceLayersDto layers = mapDto.getServiceLayers();

        //Test
        assertNotNull(layers);
        assertNotNull(mapConfigDto.getVectorStyles());
        assertNotNull(mapConfigDto.getVisibilitySettings());
        assertNotNull(mapDto.getControlDtos());
        assertNotNull(mapDto.getTbControlDtos());
        assertNotNull(mapDto.getProjectionDto());
    }

    @Test
    public void testGetMapConfigWithoutDefaultConfig() throws IOException, ServiceException {
        //mock
        mockGenMapProjectionWithoutDefaultConfig();

        //Given
        MapConfigDto mapConfigDto = mapConfigServiceBean.getReportConfig(1, getConfig("src/test/resources/UserConfig.json"), getConfig("src/test/resources/Config.json"), "rep_power", "EC", new Date().toString(), null);
        MapDto mapDto = mapConfigDto.getMap();
        ServiceLayersDto layers = mapDto.getServiceLayers();

        //Test
        assertNotNull(layers);
        assertNotNull(mapConfigDto.getVectorStyles());
        assertNotNull(mapConfigDto.getVisibilitySettings());
        assertNotNull(mapDto.getControlDtos());
        assertNotNull(mapDto.getTbControlDtos());
        assertNotNull(mapDto.getProjectionDto());
    }

    private void mockGenMapProjectionWithDefaultConfig() throws IOException, ServiceException {
        when(repository.findProjectionByMap(Mockito.any(Integer.class))).thenReturn(null);
        when(repository.findProjectionById(Mockito.any(Long.class))).thenReturn(Arrays.asList(getProjectionDto()));
        when(repository.findReportConnectServiceAreas(Mockito.any(Integer.class))).thenReturn(null);
        when(repository.findReportConnectSpatialByReportId(Mockito.any(Long.class))).thenReturn(null);
        when(repository.findSystemConfigByName(Mockito.any(String.class))).thenReturn("http://localhost:8080/geoserver/");
        when(repository.findServiceLayerEntityByIds(Mockito.any(List.class))).thenReturn(getServiceLayers());
    }
    private void mockGenMapProjectionWithoutDefaultConfig() throws IOException, ServiceException {
        when(repository.findProjectionByMap(Mockito.any(Integer.class))).thenReturn(Arrays.asList(getProjectionDto()));
        when(repository.findReportConnectSpatialByReportId(Mockito.any(Long.class))).thenReturn(getReportConnectSpatialEntity());
        when(repository.findReportConnectServiceAreas(Mockito.any(Integer.class))).thenReturn(getReportConnectServiceAreas());
        when(repository.findSystemConfigByName(Mockito.any(String.class))).thenReturn("http://localhost:8080/geoserver/");
    }

    private List<ServiceLayerEntity> getServiceLayers() {
        List<ServiceLayerEntity> serviceLayerEntities = new ArrayList<ServiceLayerEntity>();
        serviceLayerEntities.add(getServiceLayerEntity("OSM", 4));
        serviceLayerEntities.add(getServiceLayerEntity("WMS",  1));
        serviceLayerEntities.add(getServiceLayerEntity("WMS",  2));
        serviceLayerEntities.add(getServiceLayerEntity("WMS",  7));
        return serviceLayerEntities;
    }

    private ReportConnectSpatialEntity getReportConnectSpatialEntity() {
        ReportConnectSpatialEntity reportConnectSpatialEntity = new ReportConnectSpatialEntity();
        reportConnectSpatialEntity.setId(1L);

        ProjectionEntity projectionEntity = new ProjectionEntity();
        projectionEntity.setSrsCode(3857);
        reportConnectSpatialEntity.setProjectionByDisplayProjId(projectionEntity);

        reportConnectSpatialEntity.setScaleBarType(ScaleBarUnits.DEGREES);
        reportConnectSpatialEntity.setDisplayFormatType(CoordinatesFormat.DD);

        return reportConnectSpatialEntity;
    }

    private ProjectionDto getProjectionDto() {
        return new ProjectionDto(new Long(1), "Spherical Mercator", 3857, "m", "m", true, "-20026376.39;-20048966.10;20026376.39;20048966.10");
    }

    private DisplayProjectionDto getDisplayProjectionDto() {
        return new DisplayProjectionDto(3857, CoordinatesFormat.DDM, ScaleBarUnits.NAUTICAL);
    }

    private ServiceLayerEntity getServiceLayerEntity(String serviceType, int id) {
        ServiceLayerEntity serviceLayerEntity = new ServiceLayerEntity();
        ProviderFormatEntity providerFormatEntity = new ProviderFormatEntity();
        providerFormatEntity.setServiceType(serviceType);
        serviceLayerEntity.setProviderFormat(providerFormatEntity);
        AreaLocationTypesEntity areaLocationTypesEntity = new AreaLocationTypesEntity();
        serviceLayerEntity.setAreaType(areaLocationTypesEntity);
        serviceLayerEntity.setName(serviceType);
        return serviceLayerEntity;
    }

    private List<ReportConnectServiceAreasEntity> getReportConnectServiceAreas() {

        //first object
        ReportConnectServiceAreasEntity entityOne = new ReportConnectServiceAreasEntity();
        entityOne.setLayerOrder(3);
        entityOne.setSqlFilter("Test filter");
        entityOne.setServiceLayer(getServiceLayerEntity("OSM", 4));

        // second object
        ReportConnectServiceAreasEntity entityTwo = new ReportConnectServiceAreasEntity();
        entityTwo.setLayerOrder(1);
        entityTwo.setSqlFilter("Test filter");
        entityTwo.setServiceLayer(getServiceLayerEntity("WMS",  1));

        //third object
        ReportConnectServiceAreasEntity entityThree = new ReportConnectServiceAreasEntity();
        entityThree.setLayerOrder(2);
        entityThree.setSqlFilter("Test filter");
        entityThree.setServiceLayer(getServiceLayerEntity("OSM",2));

        return Arrays.asList(entityOne, entityTwo, entityThree);
    }

    private String getConfig(String file) throws IOException {
        InputStream is = new FileInputStream(file);
        return IOUtils.toString(is);
    }
}