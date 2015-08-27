package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.AreaTypeNamesSpatialRS;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.ClosestAreaSpatialRS;

import java.util.List;

public interface AreaTypeNamesService {
    AreaTypeNamesSpatialRS getAreaTypes();

    List<String> getAreaTypesRest();
}
