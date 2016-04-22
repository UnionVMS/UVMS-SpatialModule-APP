package eu.europa.ec.fisheries.uvms.spatial.service;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaDetails;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeEntry;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.geojson.PortAreaGeoJsonDto;

import java.util.List;
import java.util.Map;

public interface AreaService {

    List<Map<String, String>> getSelectedAreaColumns(List<AreaTypeEntry> areaTypes) throws ServiceException;

    void uploadArea(byte[] data, String areaType, int crsCode) throws ServiceException;

    Long updatePortArea(PortAreaGeoJsonDto portAreaGeoJsonDto) throws ServiceException;

    void deletePortArea(Long portAreaId) throws ServiceException;

    AreaDetails getAreaDetailsById(AreaTypeEntry areaTypeEntry) throws ServiceException;

    public Map<String, String> getAllCountriesDesc();

}