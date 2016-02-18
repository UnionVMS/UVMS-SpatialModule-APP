package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.PortAreaDto;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.PortAreaGeomDto;

public interface PortAreaService extends AreaDisableService {
    long updatePortArea(PortAreaGeomDto portAreaGeomDto) throws ServiceException;

    void deletePortArea(Long portAreaId) throws ServiceException;

    long createPortArea(PortAreaDto portAreaDto) throws ServiceException;
}
