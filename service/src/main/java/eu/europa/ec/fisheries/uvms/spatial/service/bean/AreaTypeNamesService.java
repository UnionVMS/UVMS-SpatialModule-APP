package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import java.util.List;

import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.AreaLayerDto;

public interface AreaTypeNamesService {
    List<String> listAllAreaTypeNames();
    List<AreaLayerDto> listSystemAreaLayerMapping();
}
