package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.AreaServiceLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.LayerTypeEnum;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.ServiceLayerDto;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.AreaLayerDto;

import javax.ejb.EJB;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Created by kopyczmi on 04-Aug-15.
 */
@RunWith(Arquillian.class)
public class AreaTypeNamesServiceIT extends AbstractArquillianIT {

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
    public void shouldReturnAreaLayerDescription() {
        List<ServiceLayerDto> serviceLayerDtos = areaTypeNamesService.getAreaLayerDescription(LayerTypeEnum.SYSAREA);
        assertNotNull(serviceLayerDtos);

        serviceLayerDtos = areaTypeNamesService.getAreaLayerDescription(LayerTypeEnum.BACKGROUND);
        assertNotNull(serviceLayerDtos);

        serviceLayerDtos = areaTypeNamesService.getAreaLayerDescription(LayerTypeEnum.ADDITIONAL);
        assertNotNull(serviceLayerDtos);

        serviceLayerDtos = areaTypeNamesService.getAreaLayerDescription(LayerTypeEnum.PORT);
        assertNotNull(serviceLayerDtos);

        serviceLayerDtos = areaTypeNamesService.getAreaLayerDescription(LayerTypeEnum.USERAREA);
        assertNotNull(serviceLayerDtos);
    }

    @Test
    public void shouldReturnAllAreaLayerDescription() {
        List<AreaServiceLayerDto> areaServiceLayerDtos = areaTypeNamesService.getAllAreasLayerDescription(LayerTypeEnum.USERAREA, "rep_power");
        assertNotNull(areaServiceLayerDtos);
        assertFalse(areaServiceLayerDtos.isEmpty());
    }
}