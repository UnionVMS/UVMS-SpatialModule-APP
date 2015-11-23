package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialEnrichmentRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialSaveMapConfigurationRS;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.MapConfigDto;

public interface MapConfigService {

    MapConfigDto getReportConfig(int reportId);

    SpatialSaveMapConfigurationRS saveMapConfiguration(SpatialEnrichmentRQ spatialEnrichmentRQ);
}
