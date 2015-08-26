package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.EezDto;

public interface EezService {
    EezSpatialRS getEezById(EezSpatialRQ getEezSpatialRQ);
    EezDto getEezByIdRest(int id);
}
