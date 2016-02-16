package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.RfmoDto;

public interface RfmoService {
    long createRfmo(RfmoDto rfmoDto) throws ServiceException;

    int disableAllRfmoAreas() throws ServiceException;
}
