package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.areaServices.RfmoDto;

public interface RfmoService extends AreaDisableService { // FIXME illegal Ejb should only have max 2 interfaces LOCAL AND/OR REMOTE
    long createRfmo(RfmoDto rfmoDto) throws ServiceException;
}
