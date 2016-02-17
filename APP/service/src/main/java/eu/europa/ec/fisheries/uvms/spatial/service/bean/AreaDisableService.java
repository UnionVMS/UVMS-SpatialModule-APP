package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;

public interface AreaDisableService {
    int disableAllAreas() throws ServiceException;
}
