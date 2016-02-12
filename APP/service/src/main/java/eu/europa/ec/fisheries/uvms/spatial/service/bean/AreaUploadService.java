package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import org.opengis.referencing.FactoryException;

import java.io.IOException;

public interface AreaUploadService {
    void uploadArea(byte[] data, String areaType, int crsCode) throws ServiceException;
}
