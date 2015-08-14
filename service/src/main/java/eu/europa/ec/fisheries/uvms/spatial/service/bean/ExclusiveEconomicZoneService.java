package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.GetEezSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.GetEezSpatialRS;

public interface ExclusiveEconomicZoneService {

    GetEezSpatialRS getExclusiveEconomicZoneById(GetEezSpatialRQ getEezSpatialRQ);
}
