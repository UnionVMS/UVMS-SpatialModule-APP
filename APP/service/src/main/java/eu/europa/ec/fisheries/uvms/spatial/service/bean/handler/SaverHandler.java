package eu.europa.ec.fisheries.uvms.spatial.service.bean.handler;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import org.opengis.feature.Property;

import java.util.List;
import java.util.Map;

public interface SaverHandler {
    void save(Map<String, List<Property>> features) throws ServiceException;
}
