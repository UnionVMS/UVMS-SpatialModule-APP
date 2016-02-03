package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.PortAreaDto;

import javax.ejb.Local;

public interface PortAreaService {
    long updatePortArea(PortAreaDto portAreaDto) throws ServiceException;

    void deletePortArea(Long portAreaId) throws ServiceException;
}
