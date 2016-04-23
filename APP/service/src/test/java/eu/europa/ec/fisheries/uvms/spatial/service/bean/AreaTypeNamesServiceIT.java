package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.AbstractArquillianTest;
import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.AreaTypeNamesService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.AreaServiceLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.LayerSubTypeEnum;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.ServiceLayerDto;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.AreaLayerDto;

import javax.ejb.EJB;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
public class AreaTypeNamesServiceIT extends AbstractArquillianTest {

    @EJB
    private AreaTypeNamesService areaTypeNamesService;

    @Test
    public void shouldReturnAreaTypes() throws Exception {
        // when
        List<String> areaTypeNames = areaTypeNamesService.listAllAreaTypeNames();

        //then
        assertNotNull(areaTypeNames);
        assertFalse(areaTypeNames.isEmpty());
    }
    
    @Test
    public void shouldReturnAreaLayerMappings() {
        // when
        List<AreaLayerDto> areaLayerMappings = areaTypeNamesService.listSystemAreaLayerMapping();

        //then
        assertNotNull(areaLayerMappings);
        assertFalse(areaLayerMappings.isEmpty());
    }

    @Test
    public void shouldReturnAreaLayerDescription() throws ServiceException {
        List<ServiceLayerDto> serviceLayerDtos = areaTypeNamesService.getAreaLayerDescription(LayerSubTypeEnum.SYSAREA);
        assertNotNull(serviceLayerDtos);

        serviceLayerDtos = areaTypeNamesService.getAreaLayerDescription(LayerSubTypeEnum.BACKGROUND);
        assertNotNull(serviceLayerDtos);

        serviceLayerDtos = areaTypeNamesService.getAreaLayerDescription(LayerSubTypeEnum.ADDITIONAL);
        assertNotNull(serviceLayerDtos);

        serviceLayerDtos = areaTypeNamesService.getAreaLayerDescription(LayerSubTypeEnum.PORT);
        assertNotNull(serviceLayerDtos);

        serviceLayerDtos = areaTypeNamesService.getAreaLayerDescription(LayerSubTypeEnum.USERAREA);
        assertNotNull(serviceLayerDtos);
    }

    @Test
    public void shouldReturnAllAreaLayerDescription() throws ServiceException {
        List<AreaServiceLayerDto> areaServiceLayerDtos = areaTypeNamesService.getAllAreasLayerDescription(LayerSubTypeEnum.USERAREA, "rep_power", "EC");
        assertNotNull(areaServiceLayerDtos);
        assertFalse(areaServiceLayerDtos.isEmpty());
    }
}