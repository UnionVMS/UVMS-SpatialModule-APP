package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialEnrichmentRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialSaveMapConfigurationRS;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.MapConfig;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.Projection;

import java.util.List;

public interface MapConfigService {
    List<Projection> getAllProjections();

    MapConfig getMockReportConfig(int reportId);

    SpatialSaveMapConfigurationRS saveMapConfiguration(SpatialEnrichmentRQ spatialEnrichmentRQ);
}
