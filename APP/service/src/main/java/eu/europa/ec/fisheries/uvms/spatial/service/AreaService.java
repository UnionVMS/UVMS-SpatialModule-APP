package eu.europa.ec.fisheries.uvms.spatial.service;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.model.upload.UploadMetadata;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.opengis.referencing.FactoryException;

public interface AreaService {

    List<Map<String, String>> getSelectedAreaColumns(List<AreaTypeEntry> areaTypes) throws ServiceException;

    void uploadArea(byte[] data, String areaType, int crsCode) throws ServiceException;

    UploadMetadata metadata(byte[] data, String areaType) throws ServiceException;

    AreaDetails getAreaDetailsById(AreaTypeEntry areaTypeEntry) throws ServiceException;

    public Map<String, String> getAllCountriesDesc() throws ServiceException;

    void upload(UploadMetadata metadata, String type, String code) throws ServiceException, FactoryException, IOException;
}
