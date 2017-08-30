/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.service.dto.config.MapConfigDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.config.ServiceLayersDto;
import eu.europa.ec.fisheries.uvms.spatial.service.dto.usm.ConfigurationDto;
import org.apache.commons.io.IOUtils;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

@RunWith(Arquillian.class)
public class MapConfigServiceIT extends BaseSpatialArquillianTest {

    @EJB
    private MapConfigService mapConfigService;

    @Test
    public void testGetAdminConfig() throws IOException {
        //Given
        ConfigurationDto configurationDto = mapConfigService.retrieveAdminConfiguration(getConfig("/Config.json"), new ArrayList<String>());

        //test
        assertNotNull(configurationDto);
        assertNotNull(configurationDto.getLayerSettings());
    }

    @Test
    public void testGetUserConfig() throws IOException {
        //Given
        ConfigurationDto configurationDto = mapConfigService.retrieveUserConfiguration(getConfig("/UserConfig.json"), getConfig("/Config.json"), "rep_power", new ArrayList<String>());

        //test
        assertNotNull(configurationDto);
        assertNotNull(configurationDto.getLayerSettings());
    }

    @Test
    public void testGetMapConfig() throws IOException {
        //given
        MapConfigDto mapConfigDto = mapConfigService.getReportConfig(1, getConfig("/UserConfig.json"), getConfig("/Config.json"), "rep_power", "EC", new Date().toString(), new ArrayList<String>());

        //test
        assertNotNull(mapConfigDto.getMap().getProjectionDto());
        ServiceLayersDto serviceLayersDto =  mapConfigDto.getMap().getServiceLayers();
        assertNotNull(serviceLayersDto);
    }

    @Test
    public void testInvalidMapConfig() throws IOException {
        //given
        MapConfigDto mapConfigDto = mapConfigService.getReportConfig(-11000000, getConfig("/UserConfig.json"), getConfig("/Config.json"), "rep_power", "EC", new Date().toString(), new ArrayList<String>());

        //test
        assertNotNull(mapConfigDto.getMap().getProjectionDto());
        ServiceLayersDto serviceLayersDto =  mapConfigDto.getMap().getServiceLayers();
        assertNotNull(serviceLayersDto);
        assertTrue(serviceLayersDto.getAdditionalLayers().isEmpty());
        assertTrue(serviceLayersDto.getBaseLayers().isEmpty());
        assertTrue(serviceLayersDto.getPortLayers().isEmpty());
        assertTrue(serviceLayersDto.getSystemLayers().isEmpty());
    }

    private String getConfig(String file) throws IOException {
        //InputStream is = new FileInputStream(file);
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(file);
        return IOUtils.toString(is);
    }
}