package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.entity.*;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.CoordinatesFormat;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ScaleBarUnits;
import eu.europa.ec.fisheries.uvms.spatial.repository.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.*;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm.ConfigurationDto;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Created by padhyad on 11/23/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class MapConfigServiceTest {

    @Mock
    private SpatialRepository repository;

    @InjectMocks
    private MapConfigServiceBean mapConfigServiceBean;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
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
        MapConfigDto mapConfigDto = mapConfigServiceBean.getReportConfig(1, getConfig("src/test/resources/UserConfig.json"), getConfig("src/test/resources/Config.json"), "rep_power");
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
    @Ignore
    public void testGetMapConfigWithoutDefaultConfig() throws IOException, ServiceException {
        //mock
        mockGenMapProjectionWithoutDefaultConfig();

        //Given
        MapConfigDto mapConfigDto = mapConfigServiceBean.getReportConfig(1, getConfig("src/test/resources/UserConfig.json"), getConfig("src/test/resources/Config.json"), "rep_power");
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
        Mockito.when(repository.findProjectionByMap(Mockito.any(Integer.class))).thenReturn(null);
        Mockito.when(repository.findProjectionById(Mockito.any(Long.class))).thenReturn(Arrays.asList(getProjectionDto()));
        Mockito.when(repository.findReportConnectServiceAreas(Mockito.any(Integer.class))).thenReturn(null);
        Mockito.when(repository.findReportConnectSpatialBy(Mockito.any(Long.class))).thenReturn(null);
        Mockito.when(repository.findSystemConfigByName(Mockito.any(Map.class))).thenReturn("http://localhost:8080/geoserver/");
        Mockito.when(repository.findServiceLayerEntityByIds(Mockito.any(List.class))).thenReturn(getServiceLayers());
    }
    private void mockGenMapProjectionWithoutDefaultConfig() throws IOException, ServiceException {
        Mockito.when(repository.findProjectionByMap(Mockito.any(Integer.class))).thenReturn(Arrays.asList(getProjectionDto()));
        Mockito.when(repository.findReportConnectSpatialBy(Mockito.any(Long.class))).thenReturn(getReportConnectSpatialEntity());
        Mockito.when(repository.findReportConnectServiceAreas(Mockito.any(Integer.class))).thenReturn(getReportConnectServiceAreas());
        Mockito.when(repository.findSystemConfigByName(Mockito.any(Map.class))).thenReturn("http://localhost:8080/geoserver/");
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
        entityOne.setId(1);
        entityOne.setIsBackground(true);
        entityOne.setLayerOrder(3);
        entityOne.setSqlFilter("Test filter");
        entityOne.setServiceLayer(getServiceLayerEntity("OSM", 4));

        // second object
        ReportConnectServiceAreasEntity entityTwo = new ReportConnectServiceAreasEntity();
        entityTwo.setId(2);
        entityTwo.setIsBackground(true);
        entityTwo.setLayerOrder(1);
        entityTwo.setSqlFilter("Test filter");
        entityTwo.setServiceLayer(getServiceLayerEntity("WMS",  1));

        //third object
        ReportConnectServiceAreasEntity entityThree = new ReportConnectServiceAreasEntity();
        entityThree.setId(3);
        entityThree.setIsBackground(false);
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
