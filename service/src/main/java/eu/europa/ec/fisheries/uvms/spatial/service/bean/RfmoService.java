package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.RfmoDto;

public interface RfmoService {
    long createRfmo(RfmoDto rfmoDto) throws ServiceException;
}
