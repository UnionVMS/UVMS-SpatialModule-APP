package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;

import java.util.Map;

public interface AreaService {

    AreaDetails getAreaDetailsById(AreaTypeEntry areaTypeEntry) throws ServiceException;

    public Map<String, String> getAllCountriesDesc();

}
