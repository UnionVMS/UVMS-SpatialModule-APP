package eu.europa.ec.fisheries.uvms.spatial.service.bean;

import eu.europa.ec.fisheries.uvms.spatial.model.schemas.*;

/**
 * Created by Cegeka on 09-Oct-15.
 */
public interface FilterAreasService {
    FilterAreasSpatialRS filterAreas(FilterAreasSpatialRQ filterAreasSpatialRQ);
}
