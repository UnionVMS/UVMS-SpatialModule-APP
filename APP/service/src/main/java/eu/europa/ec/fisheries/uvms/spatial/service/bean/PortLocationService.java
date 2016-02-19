package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.PortLocationDto;

public interface PortLocationService extends AreaDisableService {
    long createPortLocation(PortLocationDto portLocationDto) throws ServiceException;
}
