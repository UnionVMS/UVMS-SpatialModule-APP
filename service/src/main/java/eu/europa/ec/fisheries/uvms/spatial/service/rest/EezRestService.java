package eu.europa.ec.fisheries.uvms.spatial.service.rest;

import eu.europa.ec.fisheries.uvms.spatial.dto.EezDto;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.EezSpatialRS;

public interface EezRestService {

    public EezDto getEezById(Integer id);

}
