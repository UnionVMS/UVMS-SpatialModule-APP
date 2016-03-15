package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.FilterAreasSpatialRQ;
import eu.europa.ec.fisheries.uvms.spatial.model.schemas.FilterAreasSpatialRS;

public interface FilterAreasService {

    FilterAreasSpatialRS filterAreas(FilterAreasSpatialRQ filterAreasSpatialRQ);

}
