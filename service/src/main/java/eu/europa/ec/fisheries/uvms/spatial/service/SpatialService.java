package eu.europa.ec.fisheries.uvms.spatial.service;

import eu.europa.ec.fisheries.schema.spatial.source.GetEezSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.dto.SpatialDto;
import eu.europa.ec.fisheries.uvms.spatial.entity.CountryEntity;
import eu.europa.ec.fisheries.uvms.spatial.service.exception.SpatialServiceException;

import java.util.List;

public interface SpatialService {

    List<SpatialDto> getListAsRestDto(String spatialQuery) throws SpatialServiceException;

    // TODO change return type for DTO
    CountryEntity getCountryById(int id);

    GetEezSpatialRS getExclusiveEconomicZoneById(long id);

    // TODO change return type
    Object getAreasByLocation(double lat, double lon, int crs);
}
