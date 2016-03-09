package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import java.util.List;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetailsSpatialRequest;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;

public interface AreaDetailsService {

    AreaDetails getAreaDetails(AreaDetailsSpatialRequest areaDetailsSpatialRequest) throws ServiceException;
    
    AreaDetails getAreaDetailsById(AreaTypeEntry areaTypeEntry) throws ServiceException;
    
    List<AreaDetails> getAreaDetailsByLocation(AreaTypeEntry areaTypeEntry) throws ServiceException;

}
