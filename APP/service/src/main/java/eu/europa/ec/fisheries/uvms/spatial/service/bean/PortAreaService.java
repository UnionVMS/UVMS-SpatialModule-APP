package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.PortAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.geojson.PortAreaGeoJsonDto;

public interface PortAreaService extends AreaDisableService {
    long updatePortArea(PortAreaGeoJsonDto portAreaGeoJsonDto) throws ServiceException;

    void deletePortArea(Long portAreaId) throws ServiceException;

    long createPortArea(PortAreaDto portAreaDto) throws ServiceException;
}
