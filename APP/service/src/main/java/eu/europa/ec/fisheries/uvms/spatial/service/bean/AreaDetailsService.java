package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;

public interface AreaDetailsService {

    AreaDetails getAreaDetailsById(AreaTypeEntry areaTypeEntry) throws ServiceException;

}
