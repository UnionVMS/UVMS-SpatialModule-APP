package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import org.opengis.feature.Property;

import java.util.List;
import java.util.Map;

public interface AreaService {

    AreaDetails getAreaDetailsById(AreaTypeEntry areaTypeEntry) throws ServiceException;

    public Map<String, String> getAllCountriesDesc();

    void replaceEezArea(Map<String, List<Property>> features);

    void replaceRfmo(Map<String, List<Property>> features);

    void replacePort(Map<String, List<Property>> features);

    void replacePortArea(Map<String, List<Property>> features);

}
