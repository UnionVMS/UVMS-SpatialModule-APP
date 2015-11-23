package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.LayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.MapConfigDto;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by padhyad on 11/20/2015.
 */
@RunWith(Arquillian.class)
public class MapConfigServiceIT extends AbstractArquillianIT {

    @EJB
    private MapConfigService mapConfigService;

    @Test
    public void testGetMapConfig() {
        //given
        MapConfigDto mapConfigDto = mapConfigService.getReportConfig(1);

        //test
        assertNotNull(mapConfigDto.getMap().getProjectionDto());
        List<LayerDto> layers =  mapConfigDto.getMap().getLayers();
        assertNotNull(layers);
        assertFalse(layers.isEmpty());
    }

    @Test
    public void testInvalidMapConfig() {
        //given
        MapConfigDto mapConfigDto = mapConfigService.getReportConfig(1000000);

        //test
        assertNull(mapConfigDto.getMap().getProjectionDto());
        List<LayerDto> layers =  mapConfigDto.getMap().getLayers();
        assertNull(layers);
    }
}
