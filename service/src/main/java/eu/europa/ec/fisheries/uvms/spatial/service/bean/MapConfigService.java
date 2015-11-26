package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.MapConfigurationType;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialSaveMapConfigurationRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.SpatialSaveMapConfigurationRS;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.MapConfigDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.config.ProjectionDto;

import javax.xml.ws.Service;
import java.util.List;

public interface MapConfigService {

    MapConfigDto getReportConfig(int reportId);

    MapConfigurationType getMapConfigurationType(final Long reportId) throws ServiceException;

    List<ProjectionDto> getAllProjections();

    void saveMapConfiguration(SpatialSaveMapConfigurationRQ spatialSaveMapConfigurationRQ);
}
