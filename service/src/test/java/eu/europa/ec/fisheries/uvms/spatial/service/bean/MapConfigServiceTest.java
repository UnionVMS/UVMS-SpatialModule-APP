package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.entity.AreaLocationTypesEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.ProviderFormatEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.ReportConnectServiceAreasEntity;
import eu.europa.ec.fisheries.uvms.spatial.entity.ServiceLayerEntity;
import eu.europa.ec.fisheries.uvms.spatial.repository.SpatialRepository;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.LayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.MapConfigDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.ProjectionDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

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
    public void testGetMapConfig() {
        //mock
        mockLayers();
        mockProjection();

        //Given
        MapConfigDto mapConfigDto = mapConfigServiceBean.getReportConfig(1);
        List<LayerDto> layers = mapConfigDto.getMap().getLayers();

        //Test
        assertNotNull(layers);
        assertFalse(layers.isEmpty());
        assertEquals("WMS", layers.get(0).getType());
    }

    private void mockProjection() {
        ProjectionDto projectionDto = new ProjectionDto(3857, "m", true);
        Mockito.when(repository.findProjectionByMap(Mockito.any(Integer.class))).thenReturn(Arrays.asList(projectionDto));
    }

    private void mockLayers() {
        Mockito.when(repository.findReportConnectServiceAreas(Mockito.any(Integer.class))).thenReturn(getReportConnectServiceAreas());
    }

    private ServiceLayerEntity getServiceLayerEntity(String serviceType, String groupType) {
        ServiceLayerEntity serviceLayerEntity = new ServiceLayerEntity();
        ProviderFormatEntity providerFormatEntity = new ProviderFormatEntity();
        providerFormatEntity.setServiceType(serviceType);
        serviceLayerEntity.setProviderFormat(providerFormatEntity);
        AreaLocationTypesEntity areaLocationTypesEntity = new AreaLocationTypesEntity();
        areaLocationTypesEntity.setAreaGroupType(groupType);
        serviceLayerEntity.setAreaType(areaLocationTypesEntity);
        return serviceLayerEntity;
    }

    private List<ReportConnectServiceAreasEntity> getReportConnectServiceAreas() {

        //first object
        ReportConnectServiceAreasEntity entityOne = new ReportConnectServiceAreasEntity();
        entityOne.setId(1);
        entityOne.setIsBackground(true);
        entityOne.setLayerOrder(3);
        entityOne.setSqlFilter("Test filter");
        entityOne.setServiceLayer(getServiceLayerEntity("OSM", "others"));

        // second object
        ReportConnectServiceAreasEntity entityTwo = new ReportConnectServiceAreasEntity();
        entityTwo.setId(2);
        entityTwo.setIsBackground(true);
        entityTwo.setLayerOrder(1);
        entityTwo.setSqlFilter("Test filter");
        entityTwo.setServiceLayer(getServiceLayerEntity("WMS", "sysarea"));

        //third object
        ReportConnectServiceAreasEntity entityThree = new ReportConnectServiceAreasEntity();
        entityThree.setId(3);
        entityThree.setIsBackground(false);
        entityThree.setLayerOrder(2);
        entityThree.setSqlFilter("Test filter");
        entityThree.setServiceLayer(getServiceLayerEntity("OSM", "others"));

        return Arrays.asList(entityOne, entityTwo, entityThree);
    }
}
