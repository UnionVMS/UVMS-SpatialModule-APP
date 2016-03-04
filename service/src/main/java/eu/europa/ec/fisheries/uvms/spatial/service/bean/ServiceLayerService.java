package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.layer.ServiceLayer;

public interface ServiceLayerService {

    public ServiceLayer findBy(String locationType) throws ServiceException;

    void update(ServiceLayer serviceLayer) throws ServiceException;
}
