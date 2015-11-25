package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialSaveMapConfigurationRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialSaveMapConfigurationRS;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.MapConfigDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.ProjectionDto;

import java.util.List;

public interface MapConfigService {

    MapConfigDto getReportConfig(int reportId);

    List<ProjectionDto> getAllProjections();

    void saveMapConfiguration(SpatialSaveMapConfigurationRQ spatialSaveMapConfigurationRQ);
}
