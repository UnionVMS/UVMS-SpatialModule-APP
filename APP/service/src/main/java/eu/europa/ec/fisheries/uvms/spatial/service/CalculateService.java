package eu.europa.ec.fisheries.uvms.spatial.service;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;

public interface CalculateService {

    String calculateBuffer(Double latitude, Double longitude, Double buffer);

    String translate(Double tx, Double ty, String wkt) throws ServiceException;

}
