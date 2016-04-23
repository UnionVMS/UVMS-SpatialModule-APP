package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.service.MapConfigService;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.MapConfigDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.ServiceLayersDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm.ConfigurationDto;
import org.apache.commons.io.IOUtils;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(Arquillian.class)
public class MapConfigServiceIT extends BaseSpatialArquillianTest {

    @EJB
    private MapConfigService mapConfigService;

    @Test
    public void testGetAdminConfig() throws IOException {
        //Given
        ConfigurationDto configurationDto = mapConfigService.retrieveAdminConfiguration(getConfig("/Config.json"));

        //test
        assertNotNull(configurationDto);
        assertNotNull(configurationDto.getLayerSettings());
    }

    @Test
    public void testGetUserConfig() throws IOException {
        //Given
        ConfigurationDto configurationDto = mapConfigService.retrieveUserConfiguration(getConfig("/UserConfig.json"), getConfig("/Config.json"), "rep_power");

        //test
        assertNotNull(configurationDto);
        assertNotNull(configurationDto.getLayerSettings());
    }

    @Test
    public void testGetMapConfig() throws IOException {
        //given
        MapConfigDto mapConfigDto = mapConfigService.getReportConfig(1, getConfig("/UserConfig.json"), getConfig("/Config.json"), "rep_power", "EC", new Date().toString());

        //test
        assertNotNull(mapConfigDto.getMap().getProjectionDto());
        ServiceLayersDto serviceLayersDto =  mapConfigDto.getMap().getServiceLayers();
        assertNotNull(serviceLayersDto);
    }

    @Test
    public void testInvalidMapConfig() throws IOException {
        //given
        MapConfigDto mapConfigDto = mapConfigService.getReportConfig(1000000, getConfig("/UserConfig.json"), getConfig("/Config.json"), "rep_power", "EC", new Date().toString());

        //test
        assertNull(mapConfigDto.getMap().getProjectionDto());
        ServiceLayersDto serviceLayersDto =  mapConfigDto.getMap().getServiceLayers();
        assertNull(serviceLayersDto);
    }

    private String getConfig(String file) throws IOException {
        //InputStream is = new FileInputStream(file);
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(file);
        return IOUtils.toString(is);
    }
}
