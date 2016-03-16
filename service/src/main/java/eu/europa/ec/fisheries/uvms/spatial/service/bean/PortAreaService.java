package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.PortAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.geojson.PortAreaGeoJsonDto;

public interface PortAreaService extends AreaDisableService { // FIXME illegal Ejb should only have max 2 interfaces LOCAL AND/OR REMOTE
    long updatePortArea(PortAreaGeoJsonDto portAreaGeoJsonDto) throws ServiceException;

    void deletePortArea(Long portAreaId) throws ServiceException;

    long createPortArea(PortAreaDto portAreaDto) throws ServiceException;
}
