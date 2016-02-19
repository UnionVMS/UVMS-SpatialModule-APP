package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.RfmoDto;

public interface RfmoService extends AreaDisableService {
    long createRfmo(RfmoDto rfmoDto) throws ServiceException;
}
