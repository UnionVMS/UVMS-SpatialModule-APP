package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import java.util.List;

import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.AreaLayerDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.UserAreaLayerDto;

public interface AreaTypeNamesService {
    List<String> listAllAreaTypeNames();
    List<AreaLayerDto> listSystemAreaLayerMapping();
    List<UserAreaLayerDto> listUserAreaLayerMapping();
}
