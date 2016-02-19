package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import java.util.List;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.AreaLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.AreaServiceLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.LayerTypeEnum;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.layers.ServiceLayerDto;

import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UserAreaLayerDto;


public interface AreaTypeNamesService {
    List<String> listAllAreaTypeNames();
    List<String> listAllAreaAndLocationTypeNames();
    List<AreaLayerDto> listSystemAreaLayerMapping();
    List<ServiceLayerDto> getAreaLayerDescription(LayerTypeEnum layerTypeEnum) throws ServiceException;
    List<AreaServiceLayerDto> getAllAreasLayerDescription(LayerTypeEnum layerTypeEnum, String userName) throws ServiceException;
    List<UserAreaLayerDto> listUserAreaLayerMapping();
}
