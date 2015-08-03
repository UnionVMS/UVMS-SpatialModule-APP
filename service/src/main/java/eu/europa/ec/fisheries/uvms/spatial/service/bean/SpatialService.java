package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.schema.spatial.source.GetAreaTypesSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.dto.SpatialDto;
import eu.europa.ec.fisheries.uvms.spatial.entity.Country;
import eu.europa.ec.fisheries.uvms.spatial.entity.ExclusiveEconomicZone;
import eu.europa.ec.fisheries.uvms.spatial.service.exception.SpatialServiceException;

import java.util.List;

public interface SpatialService {

    List<SpatialDto> getListAsRestDto(String spatialQuery) throws SpatialServiceException;

    Country getCountryById(int id);

    ExclusiveEconomicZone getExclusiveEconomicZoneById(int id);

    GetAreaTypesSpatialRS getAreaTypes();
}
