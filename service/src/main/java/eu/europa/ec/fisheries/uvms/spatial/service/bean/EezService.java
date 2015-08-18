package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezSpatialRS;

public interface EezService {

    EezSpatialRS getExclusiveEconomicZoneById(EezSpatialRQ getEezSpatialRQ);
}
