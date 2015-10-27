package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.MapConfig;

public interface MapConfigService {
    MapConfig getMockReportConfig(int reportId);
}
