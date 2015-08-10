package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.schema.spatial.source.GetEezSpatialRS;

/**
 * //TODO create test
 */
public interface ExclusiveEconomicZoneService {

    GetEezSpatialRS getExclusiveEconomicZoneById(int id);
}
