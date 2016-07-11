/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */
package eu.europa.ec.fisheries.uvms.spatial.service.bean;

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
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
public class AreaTypeNamesServiceIT extends BaseSpatialArquillianTest {

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
        List<AreaLayerDto> areaLayerMappings = areaTypeNamesService.listSystemAreaLayerMapping(Collections.EMPTY_LIST);

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