package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.PortLocationDto;

public interface PortLocationService extends AreaDisableService { // FIXME illegal Ejb should only have max 2 interfaces LOCAL AND/OR REMOTE
    long createPortLocation(PortLocationDto portLocationDto) throws ServiceException;
}
