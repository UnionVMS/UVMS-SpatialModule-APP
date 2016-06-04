package eu.europa.ec.fisheries.uvms.spatial.service;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.model.upload.UploadMapping;
import eu.europa.ec.fisheries.uvms.spatial.model.upload.UploadMetadata;
import java.util.List;
import java.util.Map;

public interface AreaService {

    List<Map<String, String>> getSelectedAreaColumns(List<AreaTypeEntry> areaTypes) throws ServiceException;

    UploadMetadata metadata(byte[] data, String areaType) throws ServiceException;

    AreaDetails getAreaDetailsById(AreaTypeEntry areaTypeEntry) throws ServiceException;

    public Map<String, String> getAllCountriesDesc() throws ServiceException;

    void upload(UploadMapping mapping, String type, Integer code) throws ServiceException;
}
